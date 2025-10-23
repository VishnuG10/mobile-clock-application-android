package com.example.alarm20.ui.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.alarm20.R;
import com.example.alarm20.databinding.ActivityMainBinding;
import com.example.alarm20.databinding.FragmentStopwatchBinding;
import com.example.alarm20.databinding.FragmentWorldclockBinding;

public class StopwatchFragment extends Fragment implements View.OnClickListener {
    ImageButton buttonStart, buttonStop;
    Button reset;
    TextView hour,minute,second;
    public int seconds=0,minutes=0,hours=0;
    Boolean running=false;

    private StopwatchViewModel stopwatchViewModel;
    private FragmentStopwatchBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        stopwatchViewModel =
                new ViewModelProvider(this).get(StopwatchViewModel.class);
        binding = FragmentStopwatchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buttonStart=(ImageButton) getView().findViewById(R.id.imageButton);
        buttonStart.setOnClickListener(this);
        buttonStop=(ImageButton) getView().findViewById(R.id.imageButton2);
        buttonStop.setOnClickListener(this);
        reset=(Button) getView().findViewById(R.id.reset);
        reset.setOnClickListener(this);
        hour=(TextView)getView().findViewById(R.id.textView);
        minute=(TextView)getView().findViewById(R.id.textView5);
        second=(TextView)getView().findViewById(R.id.textView7);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(buttonStart)){
            counterStart();
        } else if(v.equals(buttonStop)) {
            counterStop();
        }
        else if(v.equals(reset)){
            hour.setText("00");
            minute.setText("00");
            second.setText("00");
            seconds=0;hours=0;minutes=0;
            counterStop();
        }

    }

    private void counterStop() {
        this.running=false;
        buttonStart.setEnabled(true);
        buttonStop.setEnabled(false);
    }

    private void counterStart() {
        running=true;
        System.out.println("Start ->"+Thread.currentThread().getName());
        new MyCounter().start();
        buttonStart.setEnabled(false);
        buttonStop.setEnabled(true);

    }

    Handler secondhandler = new Handler(Looper.getMainLooper())
    {
        public void handleMessage(Message mes){
            second.setText(String.valueOf(mes.what));
        }
    };
    Handler hourhandler = new Handler(Looper.getMainLooper())
    {
        public void handleMessage(Message mes){
            hour.setText(String.valueOf(mes.what));
        }
    };
    Handler minutehandler = new Handler(Looper.getMainLooper())
    {
        public void handleMessage(Message mes){
            minute.setText(String.valueOf(mes.what));
        }
    };


    class MyCounter extends Thread{
        public void run()
        {
            System.out.println("MyCounter ->"+Thread.currentThread().getName());
            while(running){
                seconds++;
                if(seconds>59){
                    seconds=00;
                    minutes++;
                }
                if(minutes>59){
                    hours++;
                    minutes=00;
                }
                secondhandler.sendEmptyMessage(seconds);
                minutehandler.sendEmptyMessage(minutes);
                hourhandler.sendEmptyMessage(hours);
                try{
                    Thread.sleep(1000);
                } catch(Exception e){}

            }
        }
    }

}