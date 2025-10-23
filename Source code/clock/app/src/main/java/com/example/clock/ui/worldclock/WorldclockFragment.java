package com.example.clock.ui.worldclock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.clock.R;
import com.example.clock.databinding.FragmentWorldclockBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class WorldclockFragment extends Fragment {
    Calendar current;
    Spinner spinner;
    TextView timezone, txtCurrentTime, txtTimeZoneTime;
    long milliseconds;
    ArrayAdapter<String> idAdapter;
    SimpleDateFormat sdf;
    Date resultDate;
    private WorldclockViewModel worldclockViewModel;
    private FragmentWorldclockBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        worldclockViewModel =
                new ViewModelProvider(this).get(WorldclockViewModel.class);

        binding = FragmentWorldclockBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        spinner=(Spinner) getView().findViewById(R.id.spinner);

        timezone=(TextView) getView().findViewById(R.id.timezone);
        txtCurrentTime=(TextView) getView().findViewById(R.id.txtCurrentTime);
        txtTimeZoneTime=(TextView) getView().findViewById(R.id.txtTimeZoneTime);

        String[] idArray = TimeZone.getAvailableIDs();
        sdf=new SimpleDateFormat("EEEE, dd 10001 yyyy HH:mm:ss");
        idAdapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, idArray);
        idAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(idAdapter);
        getGMTtime();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getGMTtime();
                String selectedID=(String) (parent.getItemAtPosition(position));
                TimeZone timeZone=TimeZone.getTimeZone(selectedID);
                String TimeZoneName = timeZone.getDisplayName();
                int TimeZoneOffset=timeZone.getRawOffset()/(60*1000);
                int hrs=TimeZoneOffset/60;
                int mins=TimeZoneOffset%60;
                milliseconds=milliseconds+timeZone.getRawOffset();
                resultDate=new Date(milliseconds);
                System.out.println(sdf.format(resultDate));
                timezone.setText(TimeZoneName+" : GMT "+ hrs +":"+mins);
                txtTimeZoneTime.setText(""+sdf.format(resultDate));
                milliseconds=0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void getGMTtime(){
        current = Calendar.getInstance();
        txtCurrentTime.setText(""+current.getTime());
        milliseconds=current.getTimeInMillis();
        TimeZone tzCurrent = current.getTimeZone();
        int offset=tzCurrent.getRawOffset();
        if(tzCurrent.inDaylightTime(new Date())){
            offset=offset+tzCurrent.getDSTSavings();
        }
        milliseconds=milliseconds-offset;
        resultDate=new Date(milliseconds);
        System.out.println(sdf.format(resultDate));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}