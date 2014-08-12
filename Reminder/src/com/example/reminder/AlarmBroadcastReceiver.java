package com.example.reminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private String notificationTitle;
    private String notificationText;
    public static final String RECEIVER_TAG = AlarmBroadcastReceiver.class.getSimpleName();

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
    public void onReceive(Context context, Intent intent) {
        Log.d(RECEIVER_TAG, "receive alarm");
        WakeLocker.acquire(context);
        notificationTitle = intent.getStringExtra(ReminderFragment.EXTRA_TITLE);
        notificationText = intent.getStringExtra(ReminderFragment.EXTRA_DESCRIPTION);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(notificationTitle).setContentText(notificationText)
                .setSmallIcon(R.drawable.ic_launcher);
        notifyBuilder.setAutoCancel(true);
        NotificationManager notifyManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notifyIntent = new Intent(context, ReminderActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, 0);
        notifyBuilder.setContentIntent(pendingIntent);
        notifyManager.notify(1764, notifyBuilder.build());
        SharedPreferences sharedPreferences = context.getSharedPreferences(ReminderFragment.PREFS_IS_ALARM_SET, 0);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(ReminderFragment.PREFS_IS_ALARM_SET, false);
        editor.commit();

    }
}
