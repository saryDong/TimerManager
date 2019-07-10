package com.abu.timermanager.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abu.timermanager.R;
import com.abu.timermanager.bean.HolidayItem;
import com.abu.timermanager.widget.MultistageProgress;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @date: 2019/7/9 10:55
 * @author: 董长峰
 * @blog: https://www.jianshu.com/u/04a705fae99b
 * @description:  节假日列表适配器
 */
public class CountdownFragmentAdapter extends RecyclerView.Adapter<CountdownFragmentAdapter.ViewHolder> {
    private Context mContext;
    private List<HolidayItem> mHolidayItems;

    public CountdownFragmentAdapter(Context context, List<HolidayItem> holidayItems) {
        mContext = context;
        mHolidayItems = holidayItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_countdown_item,viewGroup,false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.holiday_time.setText(mHolidayItems.get(i).getHolidayTime());
        viewHolder.holiday_name.setText(mHolidayItems.get(i).getHolidayName());
        Date date1 = null;
        Date date2=null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mHolidayItems.get(i).getHolidayTime()+" "+"00:00:00");
            date2 = new Date(System.currentTimeMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }
       /* String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date1);
        String later=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date2);*/
        viewHolder.distance_time.setText(getDatePoor(date1,date2));
        Date date =new Date();
        //%tj表示一年中的第几天
        String strDate =String.format("%tj",date);
        float cur=Float.parseFloat(strDate);
        float ainm=Float.parseFloat(String.format("%tj",date1));
        float left=365-cur-ainm;
        //viewHolder.mProgressBar.setMaxProgress(365);
        //viewHolder.mProgressBar.setColors(new int[]{R.color.c1,R.color.c2,R.color.c3},new float[]{10,35,20});
    }

    @Override
    public int getItemCount() {
        return mHolidayItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView holiday_name;
        TextView holiday_time;
        MultistageProgress mProgressBar;
        TextView distance_time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            holiday_name=itemView.findViewById(R.id.holiday_name);
            holiday_time=itemView.findViewById(R.id.holiday_time);
            mProgressBar=itemView.findViewById(R.id.progressBar);
            distance_time=itemView.findViewById(R.id.distance_time);
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
