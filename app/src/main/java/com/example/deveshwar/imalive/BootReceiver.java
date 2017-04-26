package com.example.deveshwar.imalive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;


public class BootReceiver extends BroadcastReceiver {


    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            // TODO read reminders from db
            List<Reminder> reminders = new ArrayList<>();
            Reminder reminder;
            String deliveryTime[];

            for (int i = 0; i < reminders.size(); i++) {
                reminder = reminders.get(i);
                deliveryTime = reminder.getDeliveryTime().split(":");
                int hour = Integer.parseInt(deliveryTime[0]);
                int minute = Integer.parseInt(deliveryTime[1]);
                Util.setAlarm(context, hour, minute);
            }
        }
    }
}