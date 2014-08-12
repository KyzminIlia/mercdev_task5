package com.example.reminder;

import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AlarmService extends Service {
	private final IBinder binder = new LocalBinder();
	private static final String SERVICE_TAG = AlarmService.class
			.getSimpleName();

	public AlarmService getService() {
		return AlarmService.this;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		Log.d(SERVICE_TAG, "onBind()");
		return binder;
	}

	@Override
	public void onCreate() {
		Log.d(SERVICE_TAG, "onCreate()");
		super.onCreate();
	}

	public class LocalBinder extends Binder {
		public AlarmService getService() {
			return AlarmService.this;
		}

	}

	public void setAlarm(PendingIntent pIntent, long timeMills) {
		AlarmManager alarmManager = (AlarmManager) this
				.getSystemService(Context.ALARM_SERVICE);
		Calendar calendar = Calendar.getInstance();
		alarmManager.set(
				AlarmManager.RTC_WAKEUP,
				timeMills + calendar.getTimeInMillis()
						- calendar.get(Calendar.MILLISECOND)
						- calendar.get(Calendar.SECOND) * 1000, pIntent);
		Log.d(SERVICE_TAG,
				"alarm set on = "
						+ (timeMills + calendar.getTimeInMillis()
								- calendar.get(Calendar.MILLISECOND) - calendar
									.get(Calendar.SECOND)));
	}
}
