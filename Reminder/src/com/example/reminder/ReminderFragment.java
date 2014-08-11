package com.example.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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

	private EditText timeEdit;
	private EditText dateEdit;
	private EditText descriptionEdit;
	private EditText titleEdit;
	private Button saveButton;
	private BroadcastReceiver setBroadcastReceiver;
	private Intent alarmIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setBroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction() == DatePickerFragment.ACTION_SET_DATA) {
					dateEdit.setText(intent
							.getStringExtra(DatePickerFragment.EXTRA_DATA));
				}
				if (intent.getAction() == TimePickerFragment.ACTION_SET_TIME)
					timeEdit.setText(intent
							.getStringExtra(TimePickerFragment.EXTRA_TIME));
			}

		};
		IntentFilter setDataIntent = new IntentFilter();
		setDataIntent.addCategory(DatePickerFragment.CATEGORY_DATA_SET);
		setDataIntent.addCategory(TimePickerFragment.CATEGORY_TIME_SET);
		setDataIntent.addAction(DatePickerFragment.ACTION_SET_DATA);
		setDataIntent.addAction(TimePickerFragment.ACTION_SET_TIME);
		alarmIntent = new Intent(getActivity(), ReminderService.class);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				setBroadcastReceiver, setDataIntent);
		super.onCreate(savedInstanceState);
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
				SharedPreferences sharedPreferences = getActivity()
						.getSharedPreferences(PREFS_DATE, 0);
				Editor editor = sharedPreferences.edit();
				editor.putString(PREFS_DATE, dateEdit.getText().toString());
				editor.commit();
				sharedPreferences = getActivity().getSharedPreferences(
						PREFS_TIME, 0);
				editor = sharedPreferences.edit();
				editor.putString(PREFS_TIME, timeEdit.getText().toString());
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
				Toast.makeText(getActivity(), "Сохранено", Toast.LENGTH_SHORT)
						.show();
				getActivity().startService(alarmIntent);

			}
			break;
		default:
			break;
		}

	}
}
