package com.example.reminder;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerDialogFragment extends DialogFragment implements OnTimeSetListener {
    public static final String TIME_PICKER_TAG = TimePickerDialogFragment.class.getSimpleName();
    public static final String ACTION_SET_TIME = "com.example.reminder.SET_TIME";
    public static final String EXTRA_TIME = "com.example.reminder.TIME";
    public static final String EXTRA_TIME_MILLS = "com.example.reminder.TIME_MILLS";
    public static final String CATEGORY_TIME_SET = "com.example.reminder.TIME_SET";

    @Override
    public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, arg1);
        calendar.set(Calendar.MINUTE, arg2);
        Intent timeIntent = new Intent(ACTION_SET_TIME);
        long time = calendar.getTimeInMillis() - System.currentTimeMillis();
        timeIntent.addCategory(CATEGORY_TIME_SET);
        timeIntent.putExtra(EXTRA_TIME, arg1 + ":" + arg2);
        timeIntent.putExtra(EXTRA_TIME_MILLS, time);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(timeIntent);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

}
