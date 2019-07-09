package com.abu.timermanager.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.abu.timermanager.R;
import com.abu.timermanager.bean.Memo;
import com.abu.timermanager.util.DateUtil;
import com.abu.timermanager.util.LitePalUtil;
import com.abu.timermanager.util.ToastUtil;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.util.Date;

import butterknife.BindView;

/**
 * 添加备忘录
 */
public class AddMemoActivity extends BaseActivity {

    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_complete)
    TextView tvComplete;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.switch_remind)
    Switch switchRemind;

    private Date remindDate;                //提醒时间

    @Override
    public int getLayoutResId() {
        return R.layout.activity_add_memo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void init() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvComplete.setOnClickListener(new View.OnClickListener() {
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
                } else {
                    remindDate = null;
                }
            }
        });
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
            memo.setRemindTime(remindDate.toString());
        }
        if (LitePalUtil.addMemo(memo)) {
            finish();
        } else {
            ToastUtil.showToast("编辑失败");
        }
    }
}
