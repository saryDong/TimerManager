package com.abu.timermanager.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abu.timermanager.R;
import com.abu.timermanager.data.HolidayItem;

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
    }

    @Override
    public int getItemCount() {
        return mHolidayItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView holiday_name;
        TextView holiday_time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            holiday_name=itemView.findViewById(R.id.holiday_name);
            holiday_time=itemView.findViewById(R.id.holiday_time);
        }
    }
}
