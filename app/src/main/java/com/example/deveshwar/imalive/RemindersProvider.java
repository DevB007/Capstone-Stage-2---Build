package com.example.deveshwar.imalive;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.deveshwar.imalive.RemindersContract.ReminderEntry;

import java.util.Arrays;

/**
 * Created by devashish.sharma on 12/27/2016.
 */
public class RemindersProvider extends ContentProvider {
    private static final String LOG_TAG = RemindersProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private ReminderDbHelper mDBHelper;

    static final int INSERT_REMINDER = 100;
    static final int REMINDER_WITH_ID = 101;
    static final int ALL_REMINDERS = 102;

    public static String[] sAllColumns = {
            ReminderEntry._ID,
            ReminderEntry.COL_CONTACT_NAME,
            ReminderEntry.COL_CONTACT_NUMBER,
            ReminderEntry.COL_CONTACT_PHOTO,
            ReminderEntry.COL_TEXT,
            ReminderEntry.COL_DELIVERY_DAYS,
            ReminderEntry.COL_DELIVERY_TIME
    };

    public static int COLUMN_IDX_ID = 0;
    public static int COLUMN_IDX_CONTACT_NAME = 1;
    public static int COLUMN_IDX_CONTACT_NUMBER = 2;
    public static int COLUMN_IDX_CONTACT_PHOTO = 3;
    public static int COLUMN_IDX_TEXT = 4;
    public static int COLUMN_IDX_DELIVERY_DAYS = 5;
    public static int COLUMN_IDX_DELIVERY_TIME = 6;

    private static final SQLiteQueryBuilder SQLITE_QUERY_BUILDER;

    static {
        SQLITE_QUERY_BUILDER = new SQLiteQueryBuilder();
        SQLITE_QUERY_BUILDER.setTables(ReminderEntry.TABLE_NAME);
    }

    private static String[] getIdFromUri(Uri uri) {
        return new String[]{RemindersContract.getIdFromUri(uri)};
    }

    private static final String S_SELECTION_ID = ReminderEntry._ID + " = ?";

    private Cursor getReminders(Uri uri) {
        return SQLITE_QUERY_BUILDER.query(mDBHelper.getReadableDatabase(),
                sAllColumns,
                null, null,
                null, null, ReminderEntry._ID + " ASC");
    }

    private Cursor getReminder(Uri uri) {
        return SQLITE_QUERY_BUILDER.query(mDBHelper.getReadableDatabase(),
                sAllColumns,
                ReminderEntry._ID + " = ?", getIdFromUri(uri),
                null, null, null);
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RemindersContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, RemindersContract.PATH_REMINDER, INSERT_REMINDER);
        uriMatcher.addURI(authority, RemindersContract.PATH_REMINDER + "/#", REMINDER_WITH_ID);
        uriMatcher.addURI(authority, RemindersContract.PATH_REMINDER + "/all", ALL_REMINDERS);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new ReminderDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Log.i(LOG_TAG, "querying: " + uri);

        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case REMINDER_WITH_ID:
                cursor = getReminder(uri);
                break;
            case ALL_REMINDERS:
                cursor = getReminders(uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case REMINDER_WITH_ID:
                return ReminderEntry.CONTENT_ITEM_TYPE;
            case ALL_REMINDERS:
                return ReminderEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case INSERT_REMINDER:
                long _id = db.insert(ReminderEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = RemindersContract.buildReminderUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(ReminderEntry.CONTENT_URI, null);
        db.close();
        return returnUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete(" + uri + ", " + selection + ", " + Arrays.toString(selectionArgs));

        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case REMINDER_WITH_ID:
                selection = ReminderEntry._ID + " = ?";
                selectionArgs = new String[]{RemindersContract.getIdFromUri(uri)};
                rowsDeleted = db.delete(ReminderEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            Log.d(LOG_TAG, "notifying content resolver that " + rowsDeleted + " rows were deleted");
            getContext().getContentResolver().notifyChange(ReminderEntry.CONTENT_URI, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "update " + uri + ", values: " + values);
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case REMINDER_WITH_ID:
                selection = ReminderEntry._ID + " = ?";
                selectionArgs = new String[]{RemindersContract.getIdFromUri(uri)};
                rowsUpdated = db.update(ReminderEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(ReminderEntry.CONTENT_URI, null);
        }

        return rowsUpdated;
    }
}
