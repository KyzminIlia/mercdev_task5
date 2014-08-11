package com.example.reminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class ReminderService extends Service implements {
	private String notificationTitle;
	private String notificationText;

	@Override
	public IBinder onBind(Intent arg0) {

		return null;
	}

	@Override
	public void onCreate() {
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(ReminderFragment.PREFS_DESCRIPTION, 0);
		notificationText = sharedPreferences.getString(
				ReminderFragment.PREFS_DESCRIPTION, "");
		sharedPreferences = getApplicationContext().getSharedPreferences(
				ReminderFragment.PREFS_TITLE, 0);
		notificationTitle = sharedPreferences.getString(
				ReminderFragment.PREFS_TITLE, "");

		NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(
				this).setContentTitle(notificationTitle)
				.setContentText(notificationText)
				.setSmallIcon(R.drawable.ic_launcher);
		NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent(this, ReminderActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, intent, 0);
		notifyBuilder.setContentIntent(pendingIntent);
		notifyManager.notify(1764, notifyBuilder.build());
		AlarmManager alarmManager;
		
		alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, operation)
		super.onCreate();
	}

	public String getNotificationTitle() {
		return notificationTitle;
	}

	public void setNotificationTitle(String notificationTitle) {
		this.notificationTitle = notificationTitle;
	}

	public String getNotificationText() {
		return notificationText;
	}

	public void setNotificationText(String notificationText) {
		this.notificationText = notificationText;
	}

}
