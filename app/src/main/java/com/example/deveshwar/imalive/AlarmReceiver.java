package com.example.deveshwar.imalive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    private ArrayList<Integer> reminderDeliveryDays = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {

        final Cursor data = context.getContentResolver().query(
                RemindersContract.buildGetAllRemindersUri(), null, null, null, null);

        if (data == null) return;

        String deliveryTime[];
        JSONObject deliveryDays;

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        data.moveToFirst();
        while (!data.isAfterLast()) {

            Reminder reminder = Reminder.from(data);
            deliveryTime = reminder.getDeliveryTime().split(":");

            try {
                deliveryDays = new JSONObject(reminder.getDeliveryDays());
                reminderDeliveryDays.clear();

                if (deliveryDays.getBoolean("Sunday")) {
                    reminderDeliveryDays.add(1);
                }
                if (deliveryDays.getBoolean("Monday")) {
                    reminderDeliveryDays.add(2);
                }
                if (deliveryDays.getBoolean("Tuesday")) {
                    reminderDeliveryDays.add(3);
                }
                if (deliveryDays.getBoolean("Wednesday")) {
                    reminderDeliveryDays.add(4);
                }
                if (deliveryDays.getBoolean("Thursday")) {
                    reminderDeliveryDays.add(5);
                }
                if (deliveryDays.getBoolean("Friday")) {
                    reminderDeliveryDays.add(6);
                }
                if (deliveryDays.getBoolean("Saturday")) {
                    reminderDeliveryDays.add(7);
                }

                if (Integer.parseInt(deliveryTime[0]) == hour
                        && Integer.parseInt(deliveryTime[1]) == minute
                        && reminderDeliveryDays.contains(day)) {
                    Util.sendReminderNotification(context, reminder.getContactName(),
                            reminder.getId());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            data.moveToNext();
        }

    }

}