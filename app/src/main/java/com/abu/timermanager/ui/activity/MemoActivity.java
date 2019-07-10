package com.abu.timermanager.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.abu.timermanager.R;
import com.abu.timermanager.bean.Memo;
import com.abu.timermanager.util.LitePalUtil;
import com.abu.timermanager.util.StatusBarUtil;
import com.abu.timermanager.util.ToastUtil;

import butterknife.BindView;

/**
 * 备忘录界面
 */
public class MemoActivity extends BaseActivity {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.ib_setting)
    ImageButton ibSetting;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.tv_remind_time)
    TextView tvRemindTime;

    private Memo memo;

    @Override
    protected void init() {
        super.init();

        //透明状态栏
        StatusBarUtil.setStatusBarTransulcentWithFullScreen(this);

        //判断是否数据传递过来
        Intent getIntent = getIntent();
        if (getIntent != null) {
            memo = (Memo) getIntent.getSerializableExtra("memo");
            tvCreateTime.setText(memo.getCreateTime());
            if (TextUtils.isEmpty(memo.getTitle())) {
                tvTitle.setText(memo.getContent());
            } else {
                tvTitle.setText(memo.getTitle());
            }
        }

        ibSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog builder = new AlertDialog.Builder(MemoActivity.this)
                        .setItems(new String[]{"修改", "删除", "取消"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        jump2AddMemoActivity(memo);
                                        break;
                                    case 1:
                                        boolean b = LitePalUtil.deleteMemo(memo);
                                        if (b) {
                                            ToastUtil.showToast("删除成功");
                                            finish();
                                        } else {
                                            ToastUtil.showToast("删除失败");
                                        }
                                        break;
                                    case 2:
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).create();
                builder.show();
            }
        });

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 跳转到AddMemoActivity
     *
     * @param memo memo
     */
    private void jump2AddMemoActivity(Memo memo) {
        Intent intent = new Intent(MemoActivity.this, AddMemoActivity.class);
        intent.putExtra("memo", memo);
        startActivity(intent);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_memo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
