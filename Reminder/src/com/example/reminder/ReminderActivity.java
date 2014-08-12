package com.example.reminder;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ReminderActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getReminderFragment() == null) {
            ReminderFragment photoFragment = new ReminderFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, photoFragment, ReminderFragment.FRAGMENT_TAG).commit();
        }
    }

    public ReminderFragment getReminderFragment() {
        return (ReminderFragment) getSupportFragmentManager().findFragmentByTag(ReminderFragment.FRAGMENT_TAG);
    }
}
