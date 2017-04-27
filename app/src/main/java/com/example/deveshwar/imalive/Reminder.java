package com.example.deveshwar.imalive;

import android.content.ContentValues;
import android.database.Cursor;

public class Reminder {

    public int id;
    public String contactName;
    public String contactNumber;
    public String contactPhoto;
    public String text;
    public String deliveryTime;
    public String deliveryDays;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactPhoto() {
        return contactPhoto;
    }

    public void setContactPhoto(String contactPhoto) {
        this.contactPhoto = contactPhoto;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryDays() {
        return deliveryDays;
    }

    public void setDeliveryDays(String deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    public static Reminder from(Cursor cursor) {
        Reminder reminder = new Reminder();
        reminder.setId(cursor.getInt(RemindersProvider.COLUMN_IDX_ID));
        reminder.setContactName(cursor.getString(RemindersProvider.COLUMN_IDX_CONTACT_NAME));
        reminder.setContactNumber(cursor.getString(RemindersProvider.COLUMN_IDX_CONTACT_NUMBER));
        reminder.setContactPhoto(cursor.getString(RemindersProvider.COLUMN_IDX_CONTACT_PHOTO));
        reminder.setText(cursor.getString(RemindersProvider.COLUMN_IDX_TEXT));
        reminder.setDeliveryDays(cursor.getString(RemindersProvider.COLUMN_IDX_DELIVERY_DAYS));
        reminder.setDeliveryTime(cursor.getString(RemindersProvider.COLUMN_IDX_DELIVERY_TIME));
        return reminder;
    }

    public ContentValues toContentValues() {
        ContentValues v = new ContentValues();
        // Skip id
        //v.put(RemindersContract.ReminderEntry._ID, getId());
        v.put(RemindersContract.ReminderEntry.COL_CONTACT_NAME, getContactName());
        v.put(RemindersContract.ReminderEntry.COL_CONTACT_NUMBER, getContactNumber());
        v.put(RemindersContract.ReminderEntry.COL_CONTACT_PHOTO, getContactPhoto());
        v.put(RemindersContract.ReminderEntry.COL_TEXT, getText());
        v.put(RemindersContract.ReminderEntry.COL_DELIVERY_DAYS, getDeliveryDays());
        v.put(RemindersContract.ReminderEntry.COL_DELIVERY_TIME, getDeliveryTime());
        return v;
    }
}