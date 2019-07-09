package com.abu.timermanager.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.abu.timermanager.R;
import com.abu.timermanager.bean.Memo;
import com.abu.timermanager.ui.activity.AddMemoActivity;
import com.abu.timermanager.ui.adapter.MemoAdapter;
import com.abu.timermanager.util.LitePalUtil;
import com.abu.timermanager.util.LogUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 备忘录
 */
public class MemoFragment extends Fragment {

    @BindView(R.id.lv_memo)
    ListView lvMemo;
    @BindView(R.id.ib_add_memo)
    ImageButton ibAddMemo;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    /**
     * 初始化
     */
    private void init() {
        ibAddMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2AddMemoActivity();
            }
        });

        //初始化数据
        List<Memo> allMemo = LitePalUtil.findAllMemo();
        if (allMemo.size() != 0) {
            tvEmpty.setVisibility(View.GONE);
            MemoAdapter adapter = new MemoAdapter(allMemo,getActivity());
            lvMemo.setAdapter(adapter);
            lvMemo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    jump2MemoActivity();
                }
            });
        }else {
            tvEmpty.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 跳转到备忘录界面
     */
    private void jump2MemoActivity() {

    }

    /**
     * 跳转到添加备忘录界面
     */
    private void jump2AddMemoActivity() {
        Intent intent = new Intent(getContext(), AddMemoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
