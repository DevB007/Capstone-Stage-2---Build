package com.example.deveshwar.imalive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;


public class BootReceiver extends BroadcastReceiver {


    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            final Cursor data = context.getContentResolver().query(
                    RemindersContract.buildGetAllRemindersUri(), null, null, null, null);
            data.moveToFirst();
            while (!data.isAfterLast()) {
                Reminder reminder = Reminder.from(data);
                String[] deliveryTime = reminder.getDeliveryTime().split(":");
                int hour = Integer.parseInt(deliveryTime[0]);
                int minute = Integer.parseInt(deliveryTime[1]);
                Util.setAlarm(context, hour, minute);
                data.moveToNext();
            }
        }
    }
}