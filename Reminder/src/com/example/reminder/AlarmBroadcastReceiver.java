package com.example.reminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
	private String notificationTitle;
	private String notificationText;
	public static final String RECEIVER_TAG = AlarmBroadcastReceiver.class
			.getSimpleName();

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

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Log.d(RECEIVER_TAG, "receive alarm");
		notificationTitle = arg1.getStringExtra(ReminderFragment.EXTRA_TITLE);
		notificationText = arg1
				.getStringExtra(ReminderFragment.EXTRA_DESCRIPTION);
		NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(
				arg0).setContentTitle(notificationTitle)
				.setContentText(notificationText)
				.setSmallIcon(R.drawable.ic_launcher);
		NotificationManager notifyManager = (NotificationManager) arg0
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent(arg0, ReminderActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(arg0, 0,
				intent, 0);
		notifyBuilder.setContentIntent(pendingIntent);
		notifyManager.notify(1764, notifyBuilder.build());

	}
}
