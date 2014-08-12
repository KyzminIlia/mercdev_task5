package com.example.reminder;

import com.example.reminder.AlarmService.LocalBinder;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;

public class BootBroatcastReceiver extends BroadcastReceiver {
	private Context mContext;
	boolean isConnected = false;
	AlarmService mService;

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ReminderFragment.PREFS_IS_ALARM_SET, 0);
		if (sharedPreferences.getBoolean(ReminderFragment.PREFS_IS_ALARM_SET,
				false)) {
			Intent intentAlarm = new Intent(context.getApplicationContext(),
					AlarmService.class);
			context.getApplicationContext().bindService(intent, connection,
					context.BIND_AUTO_CREATE);
			context.bindService(intentAlarm, connection,
					context.BIND_AUTO_CREATE);
			sharedPreferences = context.getSharedPreferences(
					ReminderFragment.PREFS_TIME_IN_MILLS, 0);
			long reminderTimeMills = sharedPreferences.getLong(
					ReminderFragment.PREFS_TIME_IN_MILLS, 0);
			Intent alarmIntent = new Intent(context,
					AlarmBroadcastReceiver.class);
			alarmIntent.setAction(ReminderFragment.ACTION_ALARM_RECEIVE);
			sharedPreferences = context.getSharedPreferences(
					ReminderFragment.PREFS_TITLE, 0);
			alarmIntent.putExtra(ReminderFragment.EXTRA_TITLE,
					sharedPreferences.getString(ReminderFragment.PREFS_TITLE,
							""));
			sharedPreferences = context.getSharedPreferences(
					ReminderFragment.PREFS_TITLE, 0);
			alarmIntent.putExtra(ReminderFragment.EXTRA_DESCRIPTION,
					sharedPreferences.getString(
							ReminderFragment.PREFS_DESCRIPTION, ""));
			alarmIntent.putExtra(ReminderFragment.PREFS_TIME_IN_MILLS,
					reminderTimeMills);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					0, alarmIntent, 0);
			mService.setAlarm(pendingIntent, reminderTimeMills);
		}

	}

	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			isConnected = false;

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			isConnected = true;
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService();
		}
	};

}
