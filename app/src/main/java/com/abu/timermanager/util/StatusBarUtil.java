package com.abu.timermanager.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 状态栏工具
 */
public class StatusBarUtil {

    /**
     * 状态栏背景颜色为浅色时，字体图标改为深色，背景为深色，字体图标改为浅色
     *
     * @param window         window
     * @param lightStatusBar 是否为浅色背景
     */
    public static void setLightStatusBar(Window window, boolean lightStatusBar) {

        //decorView窗口最顶层的视图，取最顶层的视图
        View decor = window.getDecorView();

        //getSystemUiVisibility()状态栏是否显示。
        int ui = decor.getSystemUiVisibility();
        if (lightStatusBar) {

            //View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR状态栏字体颜色标记为亮色
            ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            //判断状态栏颜色，如果为深色，直接将状态栏字体颜色改为浅色
        } else {
            ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        //判断状态栏颜色后，直接将状态栏字体颜色改为ui传的的值
        decor.setSystemUiVisibility(ui);
    }

    /**
     * 小米手机设置状态栏字体颜色
     *
     * @param window
     * @param dark
     * @return
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 魅族手机设置状态栏字体颜色。
     *
     * @param window
     * @param dark
     * @return
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏颜色
     *
     * @param window window
     * @param color  颜色
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(Window window, int color) {

        //取消设置透明状态栏，是contentView内容不在覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //需要设置这个flag，才能调用setStatusBarColor
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        //设置状态栏颜色
        window.setStatusBarColor(color);
    }

    /**
     * 通过style设置状态栏透明后，布局会延伸到状态栏，为了保留状态栏原本的位置
     * 通过设置fitsSystemWindow，在顶部预留出状态栏高度的padding。
     *
     * @param activity activity（在baseActivity中无效）
     * @param value    是否设置fitsSystemWindow
     */
    public static void setFitsSystemWindow(Activity activity, boolean value) {

        //拿到整个布局
        ViewGroup contentFrameLayout = activity.findViewById(android.R.id.content);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(value);
        }
    }

    /**
     * 将占位View高度设置为状态栏的高度
     *
     * @param context       上下文
     * @param statusBarView 占位View
     */
    public static void setStatusBarViewHeight(Context context, View statusBarView) {

        //获取状态栏高度
        int result = 0;
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);

            //设置View的高度
            ViewGroup.LayoutParams layoutParams = statusBarView.getLayoutParams();
            layoutParams.height = result;
        }
    }

    /**
     * 代码添加状态栏占位View
     * （注：最好是在跟布局中添加一个状态栏高度的paddingTop，如果布局是RelativeLayout，根布局会和
     * 占位View重合。）
     *
     * @param activity 当前activity
     * @param color    状态栏颜色
     */
    public static void addStatusBarViewWithColor(Activity activity, int color) {

        //contentView注意和下面的decorView区分开
        ViewGroup contentView = activity.findViewById(android.R.id.content);

        //添加paddingTop
        contentView.setPadding(0, getStatusBarHeight(activity), 0, 0);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {

            //5.0以上直接设置状态栏颜色
            activity.getWindow().setStatusBarColor(color);
        } else {
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View statusBarView = new View(activity);

            //设置view的高宽
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity)
            );
            statusBarView.setBackgroundColor(color);
            decorView.addView(statusBarView, layoutParams);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param activity 当前acitivity
     * @return 状态栏高度
     */
    private static int getStatusBarHeight(Activity activity) {

        //获取状态栏高度
        int result = 0;
        int resourceId = activity.getResources().getIdentifier(
                "status_bar_height",
                "dimen",
                "android"
        );
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * px转dp
     *
     * @param context 上下文
     * @param pxValue px值
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 代码设置沉浸式状态栏
     *
     * @param activity 当前activity
     */
    public static void setStatusBarTransparent(Activity activity) {

        //KITKAT API 19
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            //半透明状态栏和半透明导航栏
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;

            //LOLLIPOP API 21
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();

                //返回当前窗口属性
                WindowManager.LayoutParams attributes = window.getAttributes();

                //a = a | b;
                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
                activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.flags |= flagTranslucentStatus | flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    /**
     * 通过设置全屏设置状态栏透明
     *
     * @param activity 当前activity
     */
    public static void setStatusBarTransulcentWithFullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                //5.x开始需要把导航栏设置成透明，否则会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();

                //两个flag要结合使用，表示让应用的主体内容占用系统状态栏的控件
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
                window.setAttributes(attributes);
            }
        }
    }

}
