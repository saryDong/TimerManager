package com.abu.timermanager.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abu.timermanager.R;
import com.abu.timermanager.bean.Memo;
import com.abu.timermanager.cons.DPMode;
import com.abu.timermanager.ui.activity.AddMemoActivity;
import com.abu.timermanager.util.DateUtil;
import com.abu.timermanager.view.DatePicker;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by xiao on 2019/7/9.
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

        Calendar calendar=Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        final String year=String.valueOf(calendar.get(Calendar.YEAR));
        final String month=String.valueOf(calendar.get(Calendar.MONTH)+1);
        final String day=String.valueOf(calendar.get(Calendar.DATE));
        final String hour=String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        final String minute=String.valueOf(calendar.get(Calendar.MINUTE));

        datepicker.setMode(DPMode.SINGLE);
        datepicker.setDate(2019,7);
        datepicker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                Intent intent = new Intent();
                intent.setClass(getContext(), AddMemoActivity.class);
                intent.putExtra("remind_date","2019-7-20 00:00:00");
                startActivity(intent);
            }
        });
        return view;

    }




}


