package com.abu.timermanager;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.abu.timermanager.ui.activity.BaseActivity;
import com.abu.timermanager.ui.fragment.CountdownFragment;
import com.abu.timermanager.ui.fragment.MemoFragment;

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
                break;
            case R.id.main_article:
                fragmentTransaction.replace(R.id.fragment_frame, new CountdownFragment());
                Toast.makeText(this, "倒计时", Toast.LENGTH_SHORT).show();
            case R.id.artical_write:
                fragmentTransaction.replace(R.id.fragment_frame, new CountdownFragment());
                Toast.makeText(this, "日历", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        fragmentTransaction.commit();
    }
}
