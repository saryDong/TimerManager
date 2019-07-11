package com.abu.timermanager.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.abu.timermanager.R;
import com.abu.timermanager.bean.Memo;
import com.abu.timermanager.util.DateUtil;
import com.abu.timermanager.util.LitePalUtil;
import com.abu.timermanager.util.StatusBarUtil;
import com.abu.timermanager.util.ToastUtil;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * 添加备忘录
 */
public class AddMemoActivity extends BaseActivity {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.ib_complete)
    ImageButton ibComplete;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.switch_remind)
    Switch switchRemind;
    @BindView(R.id.iv_selected_01)
    ImageView ivSelected01;
    @BindView(R.id.rl_bg_01)
    RelativeLayout rlBg01;
    @BindView(R.id.iv_selected_02)
    ImageView ivSelected02;
    @BindView(R.id.rl_bg_02)
    RelativeLayout rlBg02;
    @BindView(R.id.iv_selected_03)
    ImageView ivSelected03;
    @BindView(R.id.rl_bg_03)
    RelativeLayout rlBg03;
    @BindView(R.id.iv_selected_04)
    ImageView ivSelected04;
    @BindView(R.id.rl_bg_04)
    RelativeLayout rlBg04;
    @BindView(R.id.iv_selected_05)
    ImageView ivSelected05;
    @BindView(R.id.rl_bg_05)
    RelativeLayout rlBg05;
    @BindView(R.id.iv_selected_06)
    ImageView ivSelected06;
    @BindView(R.id.rl_bg_06)
    RelativeLayout rlBg06;
    @BindView(R.id.tv_remind_time)
    TextView tvRemindTime;
    @BindView(R.id.btn_modify_time)
    Button btnModifyTime;
    @BindView(R.id.rl_remind_time)
    RelativeLayout rlRemindTime;

    private Date remindDate;                            //提醒时间
    private List<ImageView> ivs = new ArrayList<>();    //背景选择按钮集合

    @Override
    public int getLayoutResId() {
        return R.layout.activity_add_memo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        initView();
        initData();
        initClickListener();
    }

    private void initClickListener() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ibComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMemo();
            }
        });
        switchRemind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showTimeDialog();
                    rlRemindTime.setVisibility(View.VISIBLE);
                } else {
                    remindDate = null;
                    rlRemindTime.setVisibility(View.GONE);
                }
            }
        });
        btnModifyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });
        rlBg01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBg(0);
            }
        });
        rlBg02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBg(1);
            }
        });
        rlBg03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBg(2);
            }
        });
        rlBg04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBg(3);
            }
        });
        rlBg05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBg(4);
            }
        });
        rlBg06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBg(5);
            }
        });
    }

    private void initData() {

        //判断是否有数据传递过来
        Intent getIntent = getIntent();
        if (getIntent != null) {
            Memo memo = (Memo) getIntent.getSerializableExtra("memo");
            if (memo != null) {
                etContent.setText(memo.getContent());
                etTitle.setText(memo.getTitle());
                if (memo.getRemindTime() != null) {
                    switchRemind.setChecked(true);
                    remindDate = new Date(memo.getRemindTime());
                }
            }
        }

        //将选择标记按钮添加到集合
        ivs.add(ivSelected01);
        ivs.add(ivSelected02);
        ivs.add(ivSelected03);
        ivs.add(ivSelected04);
        ivs.add(ivSelected05);
        ivs.add(ivSelected06);
    }

    private void initView() {

        //导航栏颜色
        StatusBarUtil.setStatusBarColor(getWindow(), Color.rgb(61, 50, 66));
    }

    /**
     * 根据选择位置确定背景
     *
     * @param position
     */
    private void selectBg(int position) {
        for (int i = 0; i < ivs.size(); i++) {
            ivs.get(i).setVisibility(View.INVISIBLE);
        }
        ivs.get(position).setVisibility(View.VISIBLE);
    }

    /**
     * 提醒时间的选择器
     */
    private void showTimeDialog() {
        TimePickerView timePickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                remindDate = date;
            }
        }).setType(new boolean[]{true, true, true, true, true, true})       //年月日时分秒
                .isDialog(true)                                             //以dialog形式显示
                .setOutSideCancelable(false)                                //点击外部不能取消

                //时间选择
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        if (rlRemindTime.getVisibility() != View.VISIBLE) {
                            rlRemindTime.setVisibility(View.VISIBLE);
                        }
                        tvRemindTime.setText(DateUtil.date2String(date));
                    }
                })
                .isCyclic(true).build();                                    //循环显示
        timePickerView.show();
    }

    /**
     * 保存单条备忘录
     */
    private void saveMemo() {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.showToast("内容不能为空！");
            return;
        }
        Memo memo = new Memo();
        memo.setContent(content);
        memo.setCreateTime(DateUtil.getCurrentTime());
        memo.setTitle(title);
        if (remindDate != null) {
            memo.setRemindTime(DateUtil.date2String(remindDate));
        }

        //背景
        for (int i = 0; i < ivs.size(); i++) {
            if (ivs.get(i).getVisibility() == View.VISIBLE) {
                memo.setBgColor(i + 1);
            }
        }
        if (LitePalUtil.addMemo(memo)) {
            finish();
        } else {
            ToastUtil.showToast("编辑失败");
        }
    }
}
