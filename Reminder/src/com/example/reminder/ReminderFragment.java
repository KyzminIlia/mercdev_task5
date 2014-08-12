package com.example.reminder;

import com.example.reminder.AlarmService.LocalBinder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ReminderFragment extends Fragment implements OnClickListener {
	public static final String FRAGMENT_TAG = ReminderFragment.class
			.getSimpleName();
	public static final String PREFS_TIME = "com.example.reminder.TIME";
	public static final String PREFS_DATE = "com.example.reminder.DATE";
	public static final String PREFS_TITLE = "com.example.reminder.TITLE";
	public static final String PREFS_DESCRIPTION = "com.example.reminder.DESCRIPTION";
	public static final String PREFS_TIME_IN_MILLS = "com.example.reminder.TIME_IN_MILLS";
	public static final String EXTRA_TITLE = "com.example.reminder.TITLE";
	public static final String EXTRA_DESCRIPTION = "com.example.reminder.DESCRIPTION";
	public static final String PREFS_CURRENT_TIME_MILLS = "com.example.reminder.SCURRENT_TIME_MILLS";
	public static final String PREFS_CURRENT_DATE_MILLS = "com.example.reminder.SCURRENT_DATE_MILLS";
	public static final String ACTION_ALARM_RECEIVE = "com.example.reminder.ALARM_RECEIVE";
	public static final String PREFS_IS_ALARM_SET = "com.example.reminder.IS_ALARM_SET";

	private EditText timeEdit;
	private EditText dateEdit;
	private EditText descriptionEdit;
	private EditText titleEdit;
	private Button saveButton;
	private BroadcastReceiver setBroadcastReceiver;
	private Intent alarmIntent;
	private PendingIntent pendingIntent;
	private long reminderTimeMills = 0;
	private long currentTimeMills = 0;
	private long currentDataMills = 0;
	AlarmService mService;
	private boolean isConnected = false;
	private boolean isAlarmSet = false;

	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			isConnected = false;

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			isConnected = true;
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService();
		}
	};

	@Override
	public void onStart() {
		Intent intent = new Intent(getActivity().getApplicationContext(),
				AlarmService.class);
		getActivity().getApplicationContext().bindService(intent, connection,
				getActivity().BIND_AUTO_CREATE);
		super.onStart();
	}

	@Override
	public void onStop() {
		if (isConnected) {
			getActivity().getApplicationContext().unbindService(connection);
			isConnected = false;
		}
		super.onStop();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setBroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction() == DatePickerFragment.ACTION_SET_DATA) {
					dateEdit.setText(intent
							.getStringExtra(DatePickerFragment.EXTRA_DATA));
					currentDataMills = intent.getLongExtra(
							DatePickerFragment.EXTRA_DATA_MILLS, 0);
				}
				if (intent.getAction() == TimePickerFragment.ACTION_SET_TIME)
					timeEdit.setText(intent
							.getStringExtra(TimePickerFragment.EXTRA_TIME));
				currentTimeMills = intent.getLongExtra(
						TimePickerFragment.EXTRA_TIME_MILLS, 0);

			}

		};
		IntentFilter setDataIntent = new IntentFilter();
		setDataIntent.addCategory(DatePickerFragment.CATEGORY_DATA_SET);
		setDataIntent.addCategory(TimePickerFragment.CATEGORY_TIME_SET);
		setDataIntent.addAction(DatePickerFragment.ACTION_SET_DATA);
		setDataIntent.addAction(TimePickerFragment.ACTION_SET_TIME);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				setBroadcastReceiver, setDataIntent);
		setRetainInstance(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		WakeLocker.release();
		super.onResume();
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		timeEdit = (EditText) view.findViewById(R.id.time_edit);
		dateEdit = (EditText) view.findViewById(R.id.date_edit);
		descriptionEdit = (EditText) view.findViewById(R.id.description_edit);
		titleEdit = (EditText) view.findViewById(R.id.title_edit);
		saveButton = (Button) view.findViewById(R.id.save_button);
		saveButton.setOnClickListener(this);
		timeEdit.setOnClickListener(this);
		dateEdit.setOnClickListener(this);
		SharedPreferences sharedPreferences = getActivity()
				.getSharedPreferences(PREFS_DATE, 0);
		dateEdit.setText(sharedPreferences.getString(PREFS_DATE, ""));
		sharedPreferences = getActivity().getSharedPreferences(PREFS_TIME, 0);
		timeEdit.setText(sharedPreferences.getString(PREFS_TIME, ""));
		sharedPreferences = getActivity().getSharedPreferences(PREFS_TITLE, 0);
		titleEdit.setText(sharedPreferences.getString(PREFS_TITLE, ""));
		sharedPreferences = getActivity().getSharedPreferences(
				PREFS_DESCRIPTION, 0);
		descriptionEdit.setText(sharedPreferences.getString(PREFS_DESCRIPTION,
				""));
		sharedPreferences = getActivity().getSharedPreferences(
				PREFS_TIME_IN_MILLS, 0);
		reminderTimeMills = sharedPreferences.getLong(PREFS_TIME_IN_MILLS, 0);
		sharedPreferences = getActivity().getSharedPreferences(
				PREFS_CURRENT_DATE_MILLS, 0);
		currentDataMills = sharedPreferences.getLong(PREFS_CURRENT_DATE_MILLS,
				0);
		sharedPreferences = getActivity().getSharedPreferences(
				PREFS_CURRENT_TIME_MILLS, 0);
		currentTimeMills = sharedPreferences.getLong(PREFS_CURRENT_TIME_MILLS,
				0);

		Log.d(FRAGMENT_TAG, "share time in mills = " + reminderTimeMills
				+ " time:" + currentTimeMills + "/" + currentDataMills);
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.f_reminder, null);
		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.time_edit:
			TimePickerFragment timePickerFragment = new TimePickerFragment();
			timePickerFragment.show(getActivity().getSupportFragmentManager(),
					TimePickerFragment.TIME_PICKER_TAG);
			break;
		case R.id.date_edit:
			DatePickerFragment datePickerFragment = new DatePickerFragment();
			datePickerFragment.show(getActivity().getSupportFragmentManager(),
					DatePickerFragment.DATE_PICKER_TAG);
			break;
		case R.id.save_button:
			if (titleEdit.getText().toString().equals("")
					|| timeEdit.getText().toString().equals("")
					|| dateEdit.getText().toString().equals("")) {
				Toast.makeText(getActivity(),
						"Ошибка: Имя, дата или время не введены",
						Toast.LENGTH_SHORT).show();
			} else {
				isAlarmSet = true;
				SharedPreferences sharedPreferences = getActivity()
						.getSharedPreferences(PREFS_IS_ALARM_SET, 0);
				Editor editor = sharedPreferences.edit();
				editor.putBoolean(PREFS_IS_ALARM_SET, isAlarmSet);
				editor.commit();
				reminderTimeMills = currentDataMills + currentTimeMills;
				sharedPreferences = getActivity().getSharedPreferences(
						PREFS_DATE, 0);
				editor = sharedPreferences.edit();
				editor.putString(PREFS_DATE, dateEdit.getText().toString());
				editor.commit();
				sharedPreferences = getActivity().getSharedPreferences(
						PREFS_TIME, 0);
				editor = sharedPreferences.edit();
				editor.putString(PREFS_TIME, timeEdit.getText().toString());
				editor.commit();
				sharedPreferences = getActivity().getSharedPreferences(
						PREFS_CURRENT_DATE_MILLS, 0);
				editor = sharedPreferences.edit();
				editor.putLong(PREFS_CURRENT_DATE_MILLS, currentDataMills);
				editor.commit();
				sharedPreferences = getActivity().getSharedPreferences(
						PREFS_CURRENT_TIME_MILLS, 0);
				editor = sharedPreferences.edit();
				editor.putLong(PREFS_CURRENT_TIME_MILLS, currentTimeMills);
				editor.commit();
				sharedPreferences = getActivity().getSharedPreferences(
						PREFS_TITLE, 0);
				editor = sharedPreferences.edit();
				editor.putString(PREFS_TITLE, titleEdit.getText().toString());
				editor.commit();
				sharedPreferences = getActivity().getSharedPreferences(
						PREFS_DESCRIPTION, 0);
				editor = sharedPreferences.edit();
				editor.putString(PREFS_DESCRIPTION, descriptionEdit.getText()
						.toString());
				editor.commit();
				sharedPreferences = getActivity().getSharedPreferences(
						PREFS_TIME_IN_MILLS, 0);
				editor = sharedPreferences.edit();
				editor.putLong(PREFS_TIME_IN_MILLS, reminderTimeMills);
				editor.commit();

				Toast.makeText(getActivity(), "Сохранено", Toast.LENGTH_SHORT)
						.show();
				alarmIntent = new Intent(getActivity(),
						AlarmBroadcastReceiver.class);
				alarmIntent.setAction(ACTION_ALARM_RECEIVE);
				alarmIntent.putExtra(EXTRA_TITLE, titleEdit.getText()
						.toString());
				alarmIntent.putExtra(EXTRA_DESCRIPTION, descriptionEdit
						.getText().toString());
				alarmIntent.putExtra(PREFS_TIME_IN_MILLS, reminderTimeMills);
				pendingIntent = PendingIntent.getBroadcast(getActivity(), 0,
						alarmIntent, 0);
				mService.setAlarm(pendingIntent, reminderTimeMills);
				Log.d(FRAGMENT_TAG, "" + currentDataMills + "+"
						+ currentTimeMills + "=" + reminderTimeMills);

			}
			break;
		default:
			break;
		}

	}
}
