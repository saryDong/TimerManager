package com.abu.timermanager.adapter;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abu.timermanager.R;
import com.abu.timermanager.bean.Memo;

import java.util.List;

/**
 * 备忘录适配器
 */
public class MemoAdapter extends BaseAdapter {

    private List<Memo> memos;           //备忘录列表
    private FragmentActivity context;       //上下文

    public MemoAdapter(List<Memo> memos, FragmentActivity context) {
        this.memos = memos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return memos.size();
    }

    @Override
    public Object getItem(int position) {
        return memos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = context.getLayoutInflater().inflate(R.layout.item_memo, null);
            holder.tvContent = convertView.findViewById(R.id.tv_content);
            holder.tvTime = convertView.findViewById(R.id.tv_time);
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
    }
}
