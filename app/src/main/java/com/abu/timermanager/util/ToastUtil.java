package com.abu.timermanager.util;

import android.widget.Toast;

import com.abu.timermanager.app.TimerManagerApplication;

import org.litepal.LitePalApplication;

/**
 * toast提示语句
 */
public class ToastUtil {

    private static Toast toast;

    public static void showToast(String text){
        if (toast == null){
            toast = Toast.makeText(TimerManagerApplication.getContext(),text, Toast.LENGTH_SHORT);
        }else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void showToast(int text){
        if (toast == null){
            toast = Toast.makeText(TimerManagerApplication.getContext(),text, Toast.LENGTH_SHORT);
        }else {
            toast.setText(text);
        }
        toast.show();
    }
}
