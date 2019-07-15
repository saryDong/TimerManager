package com.abu.timermanager.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abu.timermanager.R;
import com.abu.timermanager.cons.DPMode;
import com.abu.timermanager.ui.activity.AddMemoActivity;
import com.abu.timermanager.util.DateUtil;
import com.abu.timermanager.view.DatePicker;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 日历fragment
 */
public class CalendarFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        final DatePicker datepicker = view.findViewById(R.id.main_dp);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        datepicker.setMode(DPMode.SINGLE);
        datepicker.setDate(year, month);
        datepicker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                Intent intent = new Intent();
                intent.setClass(getContext(), AddMemoActivity.class);
                intent.putExtra("remind_date", DateUtil.getNextHour(DateUtil.getCurrentTime()));
                startActivity(intent);
            }
        });
        return view;
    }
}


