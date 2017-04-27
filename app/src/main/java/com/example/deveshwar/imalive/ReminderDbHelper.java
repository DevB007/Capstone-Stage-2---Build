package com.example.deveshwar.imalive;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.deveshwar.imalive.RemindersContract.ReminderEntry;

/**
 * @Author deepankar
 * @date 27/4/17.
 */

public class ReminderDbHelper extends SQLiteOpenHelper {

    private static final String CREATE_REMINDERS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + ReminderEntry.TABLE_NAME + "("
            + ReminderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ReminderEntry.COL_CONTACT_NAME + " TEXT NOT NULL,"
            + ReminderEntry.COL_CONTACT_NUMBER + " TEXT NOT NULL,"
            + ReminderEntry.COL_CONTACT_PHOTO + " TEXT NOT NULL,"
            + ReminderEntry.COL_TEXT + " TEXT NOT NULL,"
            + ReminderEntry.COL_DELIVERY_DAYS + " TEXT NOT NULL,"
            + ReminderEntry.COL_DELIVERY_TIME + " TEXT NOT NULL"
            + ")";

    private static final String DB_NAME = "reminders.db";
    private static final int DB_VERSION = 1;

    public ReminderDbHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ReminderDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_REMINDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
