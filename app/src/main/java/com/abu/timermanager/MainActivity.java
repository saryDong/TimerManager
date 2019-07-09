package com.abu.timermanager;


import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.abu.timermanager.event.ScrollEvent;
import com.abu.timermanager.ui.activity.BaseActivity;
import com.abu.timermanager.ui.fragment.CountdownFragment;
import com.abu.timermanager.ui.fragment.MemoFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.fragment_frame)
    FrameLayout mFragmentFrame;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigation;

    @Override
    protected void init() {
        super.init();
        //监听底部导航条切换事件
        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mBottomNavigation.setSelectedItemId(R.id.main_home);

        //注册EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    /**
     * 底部导航条切换监听器
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //切换页卡
            switchPage(item.getItemId());
            return true;
        }
    };

    /**
     * 切换页面
     * @param itemId 底部导航条菜单选项的id
     */
    private void switchPage(int itemId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (itemId) {
            case R.id.main_home:
                fragmentTransaction.replace(R.id.fragment_frame, new MemoFragment());
                Toast.makeText(this, "first fragment", Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_article:
                fragmentTransaction.setCustomAnimations(R.anim.slide_right_in,R.anim.slide_left_out);
                if (mBottomNavigation.getSelectedItemId()!=R.id.main_article){
                    fragmentTransaction.replace(R.id.fragment_frame, new CountdownFragment());
                }
                break;
            case R.id.artical_write:
                Toast.makeText(this, "last fragment", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        fragmentTransaction.commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScrollChange(ScrollEvent scrollEvent) {
        if (scrollEvent.getDirection() == ScrollEvent.Direction.UP) {
            hideNavigationView();
        } else {
            showNavigationView();
        }
    }

    private void showNavigationView() {
        animationNavigationView(mBottomNavigation.getHeight(), 0);
    }


    private void hideNavigationView() {
        animationNavigationView(0, mBottomNavigation.getHeight());
    }

    private void animationNavigationView(float from ,float to) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mBottomNavigation, "translationY",
                from, to);
        objectAnimator.setDuration(500);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
