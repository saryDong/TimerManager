package com.abu.timermanager.adapter;

import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abu.timermanager.R;
import com.abu.timermanager.bean.Memo;
import com.abu.timermanager.util.DateUtil;

import java.util.List;

/**
 * 备忘录适配器
 */
public class MemoAdapter extends BaseAdapter {

    private List<Memo> memos;                                   //备忘录列表
    private FragmentActivity context;                           //上下文
    private SparseArray<CountDownTimer> downTimerSparseArray;   //计时器集合，用于销毁计时器

    public MemoAdapter(List<Memo> memos, FragmentActivity context) {
        this.memos = memos;
        this.context = context;
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

    @Override
    public int getCount() {
        if (memos != null && !memos.isEmpty()) {
            return memos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (memos != null && !memos.isEmpty()) {
            return memos.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = context.getLayoutInflater().inflate(R.layout.item_memo, null);
            holder.tvContent = convertView.findViewById(R.id.tv_content);
            holder.tvTime = convertView.findViewById(R.id.tv_time);
            holder.tvCountdown = convertView.findViewById(R.id.tv_countdown);
            holder.tvHint = convertView.findViewById(R.id.tv_hint);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Memo memo = memos.get(position);
        holder.tvTime.setText(memo.getCreateTime());
        if (TextUtils.isEmpty(memo.getTitle())) {
            holder.tvContent.setText(memo.getContent());
        } else {
            holder.tvContent.setText(memo.getTitle());
        }

        //倒计时
        if (!TextUtils.isEmpty(memo.getRemindTime())) {
            CountDownTimer timer = downTimerSparseArray.get(holder.tvCountdown.hashCode());
            if (timer != null) {

                //将复用的倒计时清除
                timer.cancel();
            }
            long time = DateUtil.computationTime(memo.getRemindTime());
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
                        holder.tvCountdown.setText(day + "天" + hour + "小时" + min + "分钟" + second + "秒");
                        holder.tvHint.setText("还有");
                    }

                    @Override
                    public void onFinish() {
                        holder.tvCountdown.setText("已结束");
                        holder.tvHint.setText("");
                    }
                }.start();
            } else {
                holder.tvCountdown.setText("已结束");
                holder.tvHint.setText("");
            }
            downTimerSparseArray.put(holder.tvCountdown.hashCode(), timer);
        } else {
            holder.tvHint.setVisibility(View.GONE);
            holder.tvCountdown.setText("已结束");
        }

        //手动选择背景
        if (memo.getBgColor() == 1) {
            convertView.setBackgroundResource(R.drawable.corners_bg_01);
        } else if (memo.getBgColor() == 2) {
            convertView.setBackgroundResource(R.drawable.corners_bg_02);
        } else if (memo.getBgColor() == 3) {
            convertView.setBackgroundResource(R.drawable.corners_bg_03);
        } else if (memo.getBgColor() == 4) {
            convertView.setBackgroundResource(R.drawable.corners_bg_04);
        } else if (memo.getBgColor() == 5) {
            convertView.setBackgroundResource(R.drawable.corners_bg_04);
        } else {
            convertView.setBackgroundResource(R.drawable.corners_bg_06);
        }
        return convertView;
    }

    class Holder {
        TextView tvContent;
        TextView tvTime;
        TextView tvCountdown;
        TextView tvHint;
    }
}
