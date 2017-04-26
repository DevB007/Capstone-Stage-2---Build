package com.example.deveshwar.imalive;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class ReminderComposerActivity extends AppCompatActivity implements TimePickerFragment
        .OnTimeSetListener {

    private Intent intentExtras;

    @BindView(R.id.save_reminder_fab)
    FloatingActionButton saveReminderFAB;

    @BindView(R.id.reminder_time_picker_button)
    Button reminderTimePickerButton;

    @BindView(R.id.reminder_delete_button)
    Button reminderDeleteButton;

    @BindView(R.id.reminder_message)
    EditText reminderMessage;

    @BindView(R.id.message_delivery_day_sunday_button)
    CheckBox reminderDeliveryDaySundayButton;

    @BindView(R.id.message_delivery_day_monday_button)
    CheckBox reminderDeliveryDayMondayButton;

    @BindView(R.id.message_delivery_day_tuesday_button)
    CheckBox reminderDeliveryDayTuesdayButton;

    @BindView(R.id.message_delivery_day_wednesday_button)
    CheckBox reminderDeliveryDayWednesdayButton;

    @BindView(R.id.message_delivery_day_thursday_button)
    CheckBox reminderDeliveryDayThursdayButton;

    @BindView(R.id.message_delivery_day_friday_button)
    CheckBox reminderDeliveryDayFridayButton;

    @BindView(R.id.message_delivery_day_saturday_button)
    CheckBox reminderDeliveryDaySaturdayButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.contact_photo)
    ImageView contactPhotoIv;

    private String contactName;
    private String contactNumber;
    private String contactPhoto;

    private boolean isEditing;

    private Reminder reminder;

    private String reminderDeliveryTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_composer);
        ButterKnife.bind(this);

        intentExtras = getIntent();

        if (intentExtras.hasExtra("reminderId")) {
            isEditing = true;
            //reminder = realm.where(Reminder.class).equalTo("id", intentExtras.getIntExtra
            // ("reminderId", -1)).findFirst();
            if (reminder != null) {
                reminderDeleteButton.setVisibility(View.VISIBLE);
                reminderDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO delete reminder from db
                        finish();
                    }
                });
                contactName = reminder.getContactName();
                contactNumber = reminder.getContactNumber();
                contactPhoto = reminder.getContactPhoto();
                reminderMessage.setText(reminder.getText());
                reminderDeliveryTime = reminder.getDeliveryTime();
                reminderTimePickerButton.setText(Util.getHumanFormattedTime(reminderDeliveryTime));
                JSONObject deliveryDays;
                try {
                    deliveryDays = new JSONObject(reminder.getDeliveryDays());
                    if (!deliveryDays.getBoolean("Sunday")) {
                        reminderDeliveryDaySundayButton.setChecked(false);
                    }
                    if (!deliveryDays.getBoolean("Monday")) {
                        reminderDeliveryDayMondayButton.setChecked(false);
                    }
                    if (!deliveryDays.getBoolean("Tuesday")) {
                        reminderDeliveryDayTuesdayButton.setChecked(false);
                    }
                    if (!deliveryDays.getBoolean("Wednesday")) {
                        reminderDeliveryDayWednesdayButton.setChecked(false);
                    }
                    if (!deliveryDays.getBoolean("Thursday")) {
                        reminderDeliveryDayThursdayButton.setChecked(false);
                    }
                    if (!deliveryDays.getBoolean("Friday")) {
                        reminderDeliveryDayFridayButton.setChecked(false);
                    }
                    if (!deliveryDays.getBoolean("Saturday")) {
                        reminderDeliveryDaySaturdayButton.setChecked(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            contactName = intentExtras.getStringExtra("contactName");
            contactNumber = intentExtras.getStringExtra("contactNumber");
            contactPhoto = intentExtras.getStringExtra("contactPhoto");
        }

        toolbar.setTitle(contactName);
        setSupportActionBar(toolbar);

        if (contactPhoto != null) {
            contactPhotoIv.setImageURI(Uri.parse(contactPhoto));
        }

        saveReminderFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMessageCompositionValid()) {
                    if (!isEditing) {
                        reminder = new Reminder();
                        //realm.where(Reminder.class).findAll().max("id");
                        // TODO read last object to get next insert key
                        Number lastIndex = 0;
                        if (lastIndex == null) {
                            lastIndex = 0;
                        }
                        reminder.setId(lastIndex.intValue() + 1);
                        reminder.setContactName(contactName);
                        reminder.setContactNumber(contactNumber);
                        reminder.setContactPhoto(contactPhoto);
                    }
                    reminder.setText(reminderMessage.getText().toString());
                    reminder.setDeliveryTime(reminderDeliveryTime);
                    reminder.setDeliveryDays(getReminderDeliveryDays());
                    // TODO write db object update code

                    String deliveryTime[] = reminder.getDeliveryTime().split(":");
                    int hour = Integer.parseInt(deliveryTime[0]);
                    int minute = Integer.parseInt(deliveryTime[1]);
                    Util.setAlarm(ReminderComposerActivity.this, hour, minute);
                    finish();
                }
            }
        });

        reminderTimePickerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TimePickerFragment timerPickerFragment = new TimePickerFragment();
                timerPickerFragment.bind(ReminderComposerActivity.this);
                timerPickerFragment.show(getFragmentManager(), "timePicker");
            }
        });
    }

    @OnCheckedChanged({R.id.message_delivery_day_sunday_button,
            R.id.message_delivery_day_monday_button,
            R.id.message_delivery_day_tuesday_button,
            R.id.message_delivery_day_wednesday_button,
            R.id.message_delivery_day_thursday_button,
            R.id.message_delivery_day_friday_button,
            R.id.message_delivery_day_saturday_button})
    public void onReminderDeliveryDayCheckChanged(CompoundButton buttonView,
                                                  boolean isChecked ) {
        if (!isChecked) {
            buttonView.setTextColor(getResources().getColor(R.color.colorPrimaryText));
            buttonView.setBackgroundResource(R.drawable.reminder_day_background_transparent);
            buttonView.setTypeface(Typeface.DEFAULT);
        } else {
            buttonView.setTextColor(getResources().getColor(android.R.color.white));
            buttonView.setBackgroundResource(R.drawable.reminder_day_background);
            buttonView.setTypeface(Typeface.DEFAULT_BOLD);
        }

    }

    @Override
    public void onTimeSet(int hh, int mm) {
        reminderDeliveryTime = Util.getFormattedTime(hh, mm);
        reminderTimePickerButton.setText(Util.getHumanFormattedTime(reminderDeliveryTime));
    }

    public boolean isMessageCompositionValid() {
        String messageTextStr = reminderMessage.getText().toString();
        if (!(reminderDeliveryDaySundayButton.isChecked()
                || reminderDeliveryDayMondayButton.isChecked()
                || reminderDeliveryDayTuesdayButton.isChecked()
                || reminderDeliveryDayWednesdayButton.isChecked()
                || reminderDeliveryDayThursdayButton.isChecked()
                || reminderDeliveryDayFridayButton.isChecked() || reminderDeliveryDaySaturdayButton
                .isChecked())) {
            Snackbar.make(saveReminderFAB, getString(R.string.error_invalid_reminder_day)
                    + " " + contactName, Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (reminderDeliveryTime == null) {
            Snackbar.make(saveReminderFAB, getString(R.string.error_invalid_reminder_time)
                    + " " + contactName, Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (!(messageTextStr.length() > 0)) {
            Snackbar.make(saveReminderFAB, getString(R.string.error_invalid_reminder_message)
                    + " " + contactName, Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private String getReminderDeliveryDays() {
        JSONObject reminderDeliveryDays = new JSONObject();
        try {
            reminderDeliveryDays.put("Sunday", reminderDeliveryDaySundayButton.isChecked());
            reminderDeliveryDays.put("Monday", reminderDeliveryDayMondayButton.isChecked());
            reminderDeliveryDays.put("Tuesday", reminderDeliveryDayTuesdayButton.isChecked());
            reminderDeliveryDays.put("Wednesday", reminderDeliveryDayWednesdayButton.isChecked());
            reminderDeliveryDays.put("Thursday", reminderDeliveryDayThursdayButton.isChecked());
            reminderDeliveryDays.put("Friday", reminderDeliveryDayFridayButton.isChecked());
            reminderDeliveryDays.put("Saturday", reminderDeliveryDaySaturdayButton.isChecked());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reminderDeliveryDays.toString();
    }

}
