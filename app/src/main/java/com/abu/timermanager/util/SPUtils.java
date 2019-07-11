package com.abu.timermanager.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.abu.timermanager.app.TimerManagerApplication;

public class SPUtils {

  private static final String SPNAME = SPUtils.class.getSimpleName() + "_SP";

  /**
   * 保存String
   */
  public static void setString2SP(String key, String value) {
    if (TextUtils.isEmpty(key) || value == null) {
      return;
    }
    SharedPreferences sp =
            TimerManagerApplication.getInstance().getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
    Editor editor = sp.edit();
    editor.putString(key, value);
    editor.apply();
  }
  /**
   * 保存String 带返回值
   */
  public static boolean setString2SPForBoolean(String key, String value) {
    if (TextUtils.isEmpty(key) || value == null) {
      return false;
    }
    SharedPreferences sp =
            TimerManagerApplication.getInstance().getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
    Editor editor = sp.edit();
    editor.putString(key, value);
    return editor.commit();
  }

  /**
   * 获取String
   */
  public static String getStringFromSP(String key, String defaultValue) {
    if (TextUtils.isEmpty(key)) {
      return defaultValue;
    }
    SharedPreferences sp =
            TimerManagerApplication.getInstance().getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
    return sp.getString(key, defaultValue);
  }

  /**
   * 保存int
   */
  public static void setInt2SP(String key, int value) {
    if (TextUtils.isEmpty(key)) {
      return;
    }
    SharedPreferences sp =
            TimerManagerApplication.getInstance().getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
    Editor editor = sp.edit();
    editor.putInt(key, value);
    editor.apply();
  }

  /**
   * 获取int
   */
  public static int getIntFromSP(String key, int defaultValue) {
    if (TextUtils.isEmpty(key)) {
      return defaultValue;
    }
    SharedPreferences sp =
            TimerManagerApplication.getInstance().getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
    return sp.getInt(key, defaultValue);
  }

  /**
   * 保存Boolean
   */
  public static void setBoolean2SP(String key, boolean value) {
    SharedPreferences sp =
            TimerManagerApplication.getInstance().getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
    Editor editor = sp.edit();
    editor.putBoolean(key, value);
    editor.apply();
  }

  /**
   * 获取Boolean
   */
  public static boolean getBooleanFromSP(String key, boolean defaultValue) {
    if (TextUtils.isEmpty(key)) {
      return defaultValue;
    }
    SharedPreferences sp =
            TimerManagerApplication.getInstance().getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
    return sp.getBoolean(key, defaultValue);
  }
}
