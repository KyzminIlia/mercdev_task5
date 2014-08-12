package com.example.reminder;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements
		OnDateSetListener {
	public static final String DATE_PICKER_TAG = DatePickerFragment.class
			.getSimpleName();
	public static final String ACTION_SET_DATA = "com.example.reminder.SET_DATA";
	public static final String EXTRA_DATA = "com.example.reminder.DATA";
	public static final String CATEGORY_DATA_SET = "com.example.reminder.DATA_SET";
	public static final String EXTRA_DATA_MILLS = "com.example.reminder.DATA_MILLS";

	@Override
	public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, arg1);
		calendar.set(Calendar.MONTH, arg2);
		calendar.set(Calendar.DAY_OF_MONTH, arg3);
		long date = calendar.getTimeInMillis()- System.currentTimeMillis();
		Intent dataIntent = new Intent(ACTION_SET_DATA);
		dataIntent.addCategory(CATEGORY_DATA_SET);
		dataIntent.putExtra(EXTRA_DATA, arg3 + "." + arg2 + "." + arg1);
		dataIntent.putExtra(EXTRA_DATA_MILLS, date);
		LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(
				dataIntent);
	}

	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

}
