package com.example.alarm20.ui.alarm;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.alarm20.R;
import com.example.alarm20.databinding.FragmentAlarmBinding;

import java.util.Calendar;

public class AlarmFragment extends Fragment {
    private long mStartTimeInMillis = 0;
    private long mTimeLeftInMillis = 0;
    private long mEndTime = 0;
    private FragmentAlarmBinding binding;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    TimePicker alarmTimePicker;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AlarmViewModel alarmViewModel=
                new ViewModelProvider(this).get(AlarmViewModel.class);
        binding = FragmentAlarmBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ToggleButton toggleButton=(ToggleButton) view.findViewById(R.id.toggleButton);
        alarmTimePicker=getView().findViewById(R.id.timePicker);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()  {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            long time;
            if (((ToggleButton) view).isChecked()) {
                Toast.makeText(getActivity(), "ALARM ON", Toast.LENGTH_SHORT).show();
                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
                Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                 pendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
                time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
                if (System.currentTimeMillis() > time) {
                    // setting time as AM and PM
                    if (calendar.AM_PM == 0)
                        time = time + (1000 * 60 * 60 * 12);
                    else
                        time = time + (1000 * 60 * 60 * 24);
                }
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,time,10000, pendingIntent);
            } else {
                alarmManager.cancel(pendingIntent);
                Toast.makeText(getActivity(), "ALARM OFF", Toast.LENGTH_SHORT).show();
            }
        }});
    }

        @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}