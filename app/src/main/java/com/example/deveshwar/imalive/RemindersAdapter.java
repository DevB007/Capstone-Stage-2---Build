package com.example.deveshwar.imalive;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;


public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.ViewHolder> {

    private List<Reminder> mReminders;

    public RemindersAdapter(List<Reminder> reminders) {
        mReminders = reminders;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Context context;
        public TextView contactName;
        public ImageView contactPhoto;
        public TextView reminderText;
        public TextView reminderDeliveryTime;
        public TextView reminderDeliveryDays;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            contactName = (TextView) itemView.findViewById(R.id.contact_name);
            contactPhoto = (ImageView) itemView.findViewById(R.id.contact_photo);
            reminderText = (TextView) itemView.findViewById(R.id.reminder_text);
            reminderDeliveryTime = (TextView) itemView.findViewById(R.id.reminder_delivery_time);
            reminderDeliveryDays = (TextView) itemView.findViewById(R.id.reminder_delivery_days);
        }

    }

    @Override
    public RemindersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_reminder, parent, false);
        ViewHolder viewHolder = new ViewHolder(context, contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RemindersAdapter.ViewHolder viewHolder, final int position) {
        Reminder reminder = mReminders.get(position);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForPosition(position, viewHolder, ReminderComposerActivity.class);
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivityForPosition(position, viewHolder, MessageComposerActivity.class);
                return true;
            }
        });

        viewHolder.contactName.setText(reminder.getContactName());
        viewHolder.reminderText.setText(reminder.getText());

        if (reminder.getContactPhoto() != null) {
            viewHolder.contactPhoto.setImageURI(Uri.parse(reminder.getContactPhoto()));
        }

        viewHolder.reminderDeliveryTime.setText(Util.getHumanFormattedTime(reminder.getDeliveryTime()));

        String reminderDeliveryDaysStr = "";
        try {
            JSONObject messageDeliveryDaysObj = new JSONObject(
                    reminder.getDeliveryDays());
            Iterator<String> Iterator = messageDeliveryDaysObj.keys();
            while (Iterator.hasNext()) {
                String day = Iterator.next();
                boolean shouldDeliver = (Boolean) messageDeliveryDaysObj.get(day);
                if (shouldDeliver) {
                    reminderDeliveryDaysStr = reminderDeliveryDaysStr + " "
                            + day.substring(0, 3).toUpperCase(Locale.getDefault());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        viewHolder.reminderDeliveryDays.setText(reminderDeliveryDaysStr);

    }

    @Override
    public int getItemCount() {
        return mReminders.size();
    }

    private void startActivityForPosition(int position, ViewHolder viewHolder, Class clazz) {
        Reminder reminder = mReminders.get(position);
        Intent intent = new Intent(viewHolder.context, clazz);
        intent.putExtra("reminderId", reminder.getId());
        viewHolder.context.startActivity(intent);
    }
}