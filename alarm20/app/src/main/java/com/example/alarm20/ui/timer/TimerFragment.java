package com.example.alarm20.ui.timer;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.alarm20.R;
import com.example.alarm20.databinding.FragmentTimerBinding;
import java.util.Locale;

public class TimerFragment extends Fragment {
    private EditText mEditTexthour,mEditTextmin,mEditTextsec;
    private TextView mTextViewCountDown;
    private Button mButtonSet;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;
TextView txt6,txt7;
    private boolean mTimerRunning;

    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;
    private TimerViewModel timerViewModel;
    private FragmentTimerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        timerViewModel =
                new ViewModelProvider(this).get(TimerViewModel.class);
        binding = FragmentTimerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;}
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        mEditTexthour =(EditText) getView().findViewById(R.id.hour);
        mEditTextmin =(EditText) getView().findViewById(R.id.min);
        mEditTextsec =(EditText) getView().findViewById(R.id.second);
        mTextViewCountDown = getView().findViewById(R.id.text_view_countdown);
        txt7=getView().findViewById(R.id.textView7);
        txt6=getView().findViewById(R.id.textView6);
        mButtonSet = getView().findViewById(R.id.button_set);
        mButtonStartPause = getView().findViewById(R.id.button_start_pause);
        mButtonReset = getView().findViewById(R.id.button_reset);
        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long hrm=0;
                long minm=0;
                long secm=0;
                String hour = mEditTexthour.getText().toString();
                String min= mEditTextmin.getText().toString();
                String sec=mEditTextsec.getText().toString();
                    hrm = Long.parseLong(hour) * 60 * 60000;
                    minm= Long.parseLong(min) * 60000;
                    secm= Long.parseLong(sec) * 1000;
                    long millisInput=hrm+minm+secm;
                    if(millisInput==0){
                        Toast.makeText(getContext(),"Timer cannot be 0",Toast.LENGTH_LONG).show();
                    }
                    setTime(millisInput);

            }
        });

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }

    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
        mEditTexthour.setText("0");
        mEditTextmin.setText("0");
        mEditTextsec.setText("0");
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
                Toast.makeText(getContext(),"Timer Expired",Toast.LENGTH_LONG).show();
            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }

    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }

    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else{
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }
        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateWatchInterface() {
        if (mTimerRunning) {
            mEditTextmin.setVisibility(View.INVISIBLE);
            mEditTexthour.setVisibility(View.INVISIBLE);
            mEditTextsec.setVisibility(View.INVISIBLE);
            mButtonSet.setVisibility(View.INVISIBLE);
            txt6.setVisibility(View.INVISIBLE);
            txt7.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
        } else {
            mEditTexthour.setVisibility(View.VISIBLE);
            mEditTextmin.setVisibility(View.VISIBLE);
            mEditTextsec.setVisibility(View.VISIBLE);
            mButtonSet.setVisibility(View.VISIBLE);
            txt6.setVisibility(View.VISIBLE);
            txt6.setVisibility(View.VISIBLE);
            mButtonStartPause.setText("Start");
            if (mTimeLeftInMillis < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftInMillis < mStartTimeInMillis) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void closeKeyboard() {
        View view = this.getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);

        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateWatchInterface();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                startTimer();
            }
        }
    }
}