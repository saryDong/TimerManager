package com.abu.timermanager.bao;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by lenovo on 2019/3/14.
 */

public class OkGoDownloadFile {
    public static  DownloadProgressListening mProgressListening;
    public interface DownloadProgressListening{
        /**
         *
         * @param progress 百分比
         */
        void setProgress(int progress);
        void goJumpMain();
    }

    /**
     *
     * @param activity
     * @param fileUrl  下载链接
     * @param progressListening 进度监听
     */
    public static void onDownloadFile(final Activity activity, String fileUrl, final String packageName, DownloadProgressListening progressListening){
       // getFilesDir().getAbsolutePath()
        File sdDir = activity.getFilesDir();
        final File myDir = new File(sdDir.toString() + "/saved_images");
        myDir.mkdirs();
        myDir.delete();

        final String sName = SystemClock.uptimeMillis() + ".apk";
        final File file = new File(sdDir, sName);
        if (file.exists()) file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mProgressListening=progressListening;
        //activity.getFilesDir().getAbsolutePath() 必须加 不然路径出错
      //getExternalCacheDir()
        OkGo.<File>get(fileUrl).tag(activity).execute(
                new FileCallback(activity.getExternalCacheDir().getAbsolutePath() ,sName) {
            private long mCurrentSize=0;
            @Override
            public void onStart(Request<File, ? extends Request> request) {
                super.onStart(request);
                Log.e("okgo", "开始下载文件" + "DDDDD");
            }

            @Override
            public void onSuccess(Response<File> response) {

              String cmd = "chmod 777 " + response.body().getAbsolutePath();
               /// String cmd = "chmod 777 " + response.body().getAbsoluteFile();
                try {
                    Runtime.getRuntime().exec(cmd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("okgo", "下载文件完成" + "cmd==1==="+cmd);
                Log.e("okgo", "下载文件完成" + "cmd==2==="+activity.getFilesDir().getAbsolutePath());
                Log.e("okgo", "下载文件完成" + "cmd==3==="+response.body().getAbsolutePath());
                Log.e("okgo", "下载文件完成" + "cmd==4==="+response.body().getAbsoluteFile());
                Log.e("okgo", "下载文件完成" + "cmd==5==Environment==="+  Environment.getExternalStorageDirectory().getAbsolutePath());
                Log.e("okgo", "下载文件完成" + "cmd==6==="+response.body().getAbsoluteFile());
                Log.e("okgo", "下载文件完成" + "cmd==7===getExternalCacheDir=="+activity.getExternalCacheDir().getAbsolutePath());

                //TODO 广播监听新app安装,以便于卸载旧app  暂时不需要所以隐藏掉了
               /* try {
                    installedReceiver = new MyInstalledReceiver();
                    IntentFilter filter = new IntentFilter();
                    filter.addAction("android.intent.action.PACKAGE_ADDED");
                    filter.addAction("android.intent.action.PACKAGE_REMOVED");
                    filter.addDataScheme("package");
                    registerReceiver(installedReceiver, filter);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
               /* try {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
                    intent.setDataAndType(Uri.fromFile(response.body().getAbsoluteFile()), "application/vnd.android.package-archive");
                    activity.startActivity(intent);
                    activity.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                installApk(activity,response.body().getAbsoluteFile());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.e("okgo", "下载文件完成" + "DDDDD");

            }

            @Override
            public void onError(Response<File> response) {
                super.onError(response);
                Log.e("okgo", "下载文件出错" + "DDDDD" + response.message());
                if (mProgressListening!=null)
                    mProgressListening.goJumpMain();
            }

            @Override
            public void downloadProgress(final Progress progress) {
                super.downloadProgress(progress);
                double div = div(progress.currentSize, progress.totalSize, 2);
                int i = (int) (div * 100);
                if (mProgressListening!=null)
                mProgressListening.setProgress(i);

               /* pos.setText(i+ "%");
                seekBar.setProgress((int)i);*/
                //Log.d("okgo", "当前下载了:"+progress.currentSize + ",总共有:" + progress.totalSize+",下载速度为:"+progress.speed);//这个totalSize一直是初始值-1，很尴尬

            }
        });
    }

    /**
     * 安装 apk 文件
     *
     * @param apkFile
     */
    public static void installApk(Activity activity, File apkFile) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Log.i("TAG", "installApk: ==="+activity.getApplicationContext().getPackageName() +".provider");
            Uri contentUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() +".provider",apkFile);
            //Uri contentUri = FileProvider.getUriForFile(activity,"com.androidstudyio.compiler.provider",apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            activity.startActivity(intent);
        } else {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            activity.startActivity(intent);
        }
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    private static double div(long v1, long v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Long.toString(v1));
        BigDecimal b2 = new BigDecimal(Long.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }



    //代码进度条的文件下载
    public void jindutiao(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkGo.<File>get("http://39.107.104.102:8080/downloadkejian.jsp").headers("user","18435997685_02-18-22:55:31_03-03-16:09:57.pdf")
                        .execute(new FileCallback("kejian.pdf") {//在下载中，可以设置下载下来的文件的名字
                            @Override
                            public void downloadProgress(Progress progress) {
                                super.downloadProgress(progress);
                                Log.d("EasyHttpActivity", "当前下载了:"+progress.currentSize + ",总共有:" + progress.totalSize+",下载速度为:"+progress.speed);//这个totalSize一直是初始值-1，很尴尬
                                Log.d("xinxi", progress.toString());
                            }

                            @Override
                            public void onSuccess(Response<File> response) {
                                Log.d("EasyHttpActivity", "下载完成");
                            }
                        });
            }
        }).start();
    }


}
