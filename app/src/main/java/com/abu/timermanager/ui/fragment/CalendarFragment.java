package com.abu.timermanager.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abu.timermanager.MainActivity;
import com.abu.timermanager.R;
import com.abu.timermanager.cons.DPMode;
import com.abu.timermanager.ui.activity.AddMemoActivity;
import com.abu.timermanager.util.DateUtil;
import com.abu.timermanager.util.LogUtil;
import com.abu.timermanager.util.ToastUtil;
import com.abu.timermanager.view.DatePicker;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 日历fragment
 */
public class CalendarFragment extends Fragment {

//    private Context context;
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        this.context = context;
//    }

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

                /**
                 * getContext()有时为null
                 */
                try{
                    Intent intent = new Intent(getContext(), AddMemoActivity.class);
                    LogUtil.e(date);
                    intent.putExtra("remind_date", date + " 08:00:00");
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
}


