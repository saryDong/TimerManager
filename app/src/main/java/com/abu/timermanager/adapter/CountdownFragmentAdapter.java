package com.abu.timermanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abu.timermanager.R;
import com.abu.timermanager.bean.HolidayItem;
import com.abu.timermanager.ui.activity.HolidayDetailActivity;
import com.abu.timermanager.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @date: 2019/7/9 10:55
 * @author: 董长峰
 * @blog: https://www.jianshu.com/u/04a705fae99b
 * @description:  节假日列表适配器
 */
public class CountdownFragmentAdapter extends RecyclerView.Adapter<CountdownFragmentAdapter.ViewHolder> {
    private Context mContext;
    private List<HolidayItem> mHolidayItems;
    private SparseArray<CountDownTimer> downTimerSparseArray;   //计时器集合，用于销毁计时器

    public CountdownFragmentAdapter(Context context, List<HolidayItem> holidayItems) {
        mContext = context;
        mHolidayItems = holidayItems;
        this.downTimerSparseArray = new SparseArray<>();
    }

    /**
     * 销毁所有计时器，避免内存溢出
     */
    public void cancelAllTimers() {
        if (downTimerSparseArray == null) {
            return;
        }
        for (int i = 0; i < downTimerSparseArray.size(); i++) {
            CountDownTimer timer = downTimerSparseArray.get(i);
            if (timer != null) {
                timer.cancel();
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_countdown_item,viewGroup,false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.holiday_time.setText(mHolidayItems.get(i).getStartday());
        viewHolder.holiday_name.setText(mHolidayItems.get(i).getName());
        Date date1 = null;
        Date date2=null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mHolidayItems.get(i).getStartday()+" "+"00:00:00");
            date2 = new Date(System.currentTimeMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int radmon= new Random().nextInt(6)+1;
        //手动选择背景
        if (radmon == 1) {
            viewHolder.bg_parent.setBackgroundResource(R.drawable.corners_bg_01);
        } else if (radmon == 2) {
            viewHolder.bg_parent.setBackgroundResource(R.drawable.corners_bg_02);
        } else if (radmon == 3) {
            viewHolder.bg_parent.setBackgroundResource(R.drawable.corners_bg_03);
        } else if (radmon == 4) {
            viewHolder.bg_parent.setBackgroundResource(R.drawable.corners_bg_04);
        } else if (radmon == 5) {
            viewHolder.bg_parent.setBackgroundResource(R.drawable.corners_bg_04);
        } else {
            viewHolder.bg_parent.setBackgroundResource(R.drawable.corners_bg_06);
        }

        //viewHolder.distance_time.setText(getDatePoor(date1,date2));
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date1);
        //倒计时
        if (!TextUtils.isEmpty(mHolidayItems.get(i).getStartday())) {
            CountDownTimer timer = downTimerSparseArray.get(viewHolder.distance_time.hashCode());
            if (timer != null) {

                //将复用的倒计时清除
                timer.cancel();
            }
            long time = DateUtil.computationTime(now);
            if (time > 0) {
                timer = new CountDownTimer(time, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                        //以天数为单位取整
                        long day = millisUntilFinished / (1000 * 60 * 60 * 24);

                        //以小时为单位取整
                        long hour = (millisUntilFinished / (60 * 60 * 1000) - day * 24);

                        //以分钟为单位取整
                        long min = ((millisUntilFinished / (60 * 1000)) - day * 24 * 60 - hour * 60);

                        //秒
                        long second = (millisUntilFinished / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                        viewHolder.distance_time.setText(day + "天" + hour + "小时" + min + "分钟" + second + "秒");
                    }

                    @Override
                    public void onFinish() {
                        viewHolder.distance_time.setText("已结束");
                    }
                }.start();
            } else {
                viewHolder.distance_time.setText("已结束");
            }
            downTimerSparseArray.put(viewHolder.distance_time.hashCode(), timer);
        } else {
            viewHolder.distance_time.setText("已结束");
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, HolidayDetailActivity.class);
                intent.putExtra("holiday_name",mHolidayItems.get(i).getName());
                intent.putExtra("holiday_time",mHolidayItems.get(i).getStartday());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHolidayItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView holiday_name;
        TextView holiday_time;
        TextView distance_time;
        CardView bg_parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            holiday_name=itemView.findViewById(R.id.holiday_name);
            holiday_time=itemView.findViewById(R.id.holiday_time);
            distance_time=itemView.findViewById(R.id.distance_time);
            bg_parent=itemView.findViewById(R.id.bg_parent);
        }
    }

    public static String getDatePoor(Date endDate, Date nowDate) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        if (day<0||hour<0||min<0){
            return "节假日已过";
        }
        return "距离："+day + "天";
    }
}
