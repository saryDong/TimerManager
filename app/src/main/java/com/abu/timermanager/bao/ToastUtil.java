package com.abu.timermanager.bao;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.Toast;


public class ToastUtil {
    private static Toast toast;

    /**
     * 强大的吐司，能够连续弹的吐司
     *
     * @param text
     */
    @SuppressLint("WrongConstant")
    public static void showToast(Activity activity, String text) {
        if (toast == null) {
            toast = Toast.makeText(activity.getApplication(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);//如果不为空，则直接改变当前toast的文本
        }
        toast.show();
    }
}
