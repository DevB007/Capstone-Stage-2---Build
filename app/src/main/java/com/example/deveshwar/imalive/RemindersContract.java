package com.example.deveshwar.imalive;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @Author deepankar
 * @date 27/4/17.
 */

public class RemindersContract {

    public static final String CONTENT_AUTHORITY = "com.deveshwar.imalive";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_REMINDER = "reminder";

    private RemindersContract() {
    }

    static abstract class ReminderEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REMINDER).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        PATH_REMINDER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        PATH_REMINDER;

        public static final String COL_CONTACT_NAME = "contact_name";
        public static final String COL_CONTACT_NUMBER = "contact_number";
        public static final String COL_CONTACT_PHOTO = "contact_photo";
        public static final String COL_TEXT = "text";
        public static final String COL_DELIVERY_TIME = "delivery_time";
        public static final String COL_DELIVERY_DAYS = "delivery_days";
        public static final String TABLE_NAME = "reminders";
    }

    public static Uri buildGetAllRemindersUri() {
        return buildUri(ReminderEntry.CONTENT_URI, "all");
    }

    public static Uri buildReminderUri(long id) {
        return ContentUris.withAppendedId(ReminderEntry.CONTENT_URI, id);
    }

    public static String getIdFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    private static Uri buildUri(Uri contentUri, String post) {
        return contentUri.buildUpon()
                .appendPath(post)
                .build();
    }
}
