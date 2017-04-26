package com.example.deveshwar.imalive;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * @Author deepankar
 * @date 26/4/17.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private OnTimeSetListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void bind(OnTimeSetListener listener) {
        this.listener = listener;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        listener.onTimeSet(hourOfDay, minute);
    }

    interface OnTimeSetListener {
        void onTimeSet(int hh, int mm);
    }
}
