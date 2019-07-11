package com.abu.timermanager.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abu.timermanager.R;
import com.abu.timermanager.bean.Memo;
import com.abu.timermanager.util.DateUtil;
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
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.rl_root2)
    RelativeLayout rlRoot2;
    @BindView(R.id.tv_content)
    TextView tvContent;

    private Memo memo;

    @Override
    protected void init() {
        super.init();
        initView();
        initData();
        initClickListener();
    }

    private void initClickListener() {
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

    private void initData() {
        //判断是否数据传递过来
        Intent getIntent = getIntent();
        if (getIntent != null) {
            memo = (Memo) getIntent.getSerializableExtra("memo");
            tvCreateTime.setText(memo.getCreateTime());

            //有标题显示标题和内容，没有标题显示内容
            if (TextUtils.isEmpty(memo.getTitle())) {
                tvTitle.setVisibility(View.GONE);
            } else {
                tvTitle.setText(memo.getTitle());
            }
            tvContent.setText(memo.getContent());

            //判断是否有提醒时间，没有提醒时间，时间显示为创建时间，有则显示为提醒时间
            if (TextUtils.isEmpty(memo.getRemindTime())) {
                tvCreateTime.setText(memo.getCreateTime());
                tvHint.setText("已结束");
                tvRemindTime.setVisibility(View.GONE);
            } else {

                //倒计时
                tvCreateTime.setText(memo.getRemindTime());
                tvHint.setText("还有");
                long l = DateUtil.computationTime(memo.getRemindTime());
                CountDownTimer timer = new CountDownTimer(l, 1000) {
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
                        tvRemindTime.setText(day + "天" + hour + "小时" + min + "分钟" + second + "秒");
                    }

                    @Override
                    public void onFinish() {
                        tvRemindTime.setText("已结束");
                        tvHint.setVisibility(View.GONE);
                    }
                };
                timer.start();
            }

            //背景
            if (memo.getBgColor() == 1) {
                rlRoot.setBackgroundResource(R.drawable.bg_01);
                rlRoot2.setBackgroundResource(R.drawable.corners_bg_01);
            } else if (memo.getBgColor() == 2) {
                rlRoot.setBackgroundResource(R.drawable.bg_02);
                rlRoot2.setBackgroundResource(R.drawable.corners_bg_02);
            } else if (memo.getBgColor() == 3) {
                rlRoot.setBackgroundResource(R.drawable.bg_03);
                rlRoot2.setBackgroundResource(R.drawable.corners_bg_03);
            } else if (memo.getBgColor() == 4) {
                rlRoot.setBackgroundResource(R.drawable.bg_04);
                rlRoot2.setBackgroundResource(R.drawable.corners_bg_04);
            } else if (memo.getBgColor() == 5) {
                rlRoot.setBackgroundResource(R.drawable.bg_05);
                rlRoot2.setBackgroundResource(R.drawable.corners_bg_05);
            } else {
                rlRoot.setBackgroundResource(R.drawable.bg_06);
                rlRoot2.setBackgroundResource(R.drawable.corners_bg_06);
            }
        }
    }

    private void initView() {

        //透明状态栏
        StatusBarUtil.setStatusBarTransulcentWithFullScreen(this);
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
