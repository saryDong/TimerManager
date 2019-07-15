package com.abu.timermanager.bao;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abu.timermanager.R;
import com.just.library.AgentWeb;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lenovo on 2018/1/6.
 */

public class NewWebActivity extends AppCompatActivity implements View.OnClickListener {

    protected AgentWeb mAgentWeb;
    private ProgressDialog mProgressDialog;
    private AlertDialog mAlertDialog;
    private SplashUpdateBean.DataBean mBean;
    private RelativeLayout mLayoutHome;
    private RelativeLayout mLayoutTwo;
    private RelativeLayout mLayoutThree;
    private RelativeLayout mLayoutThree04;
    private RelativeLayout mLayoutThree05;
    private LinearLayout mLlButton;
    private TextView mTvHome;
    private TextView mTvTwo;
    private TextView mTvThree;
    private TextView mTvThree04;
    private TextView mTvThree05;
    private ImageView mIvHome;
    private ImageView mIvTwo;
    private ImageView mIvThree;
    private ImageView mIvThree04;
    private ImageView mIvThree05;
    private LinearLayout mLinl;
    private LinearLayout mLin02;
    private WebView mMWebView;

    public static void newInstance(Context context, SplashUpdateBean.DataBean person) {
        Intent intent = new Intent(context, NewWebActivity.class);
        intent.putExtra("bean", person);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webx5);
        initView();
        initWebView();
    }
    private boolean portrait;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        portrait = newConfig.orientation == Configuration.ORIENTATION_PORTRAIT;
        tryFullScreen(!portrait);
    }

    private void tryFullScreen(boolean fullScreen) {

        if (fullScreen) {
            mLlButton.setVisibility(View.GONE);
        } else {
            mLlButton.setVisibility(View.VISIBLE);
        }

        setFullScreen(fullScreen);
    }
    private void setFullScreen(boolean fullScreen) {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        if (fullScreen) {
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

    }

    private void initView() {
        mLayoutHome = (RelativeLayout) findViewById(R.id.layout_home);
        mLayoutTwo = (RelativeLayout) findViewById(R.id.layout_two);
        mLayoutThree = (RelativeLayout) findViewById(R.id.layout_three);
        mLayoutThree04 = (RelativeLayout) findViewById(R.id.layout_three04);
        mLayoutThree05 = (RelativeLayout) findViewById(R.id.layout_three05);
        mLlButton = (LinearLayout) findViewById(R.id.ll_button);


        mLayoutHome.setOnClickListener(this);
        mLayoutTwo.setOnClickListener(this);
        mLayoutThree.setOnClickListener(this);
        mLayoutThree04.setOnClickListener(this);
        mLayoutThree05.setOnClickListener(this);
        mBean = (SplashUpdateBean.DataBean) getIntent().getSerializableExtra("bean");


        mTvHome = (TextView) findViewById(R.id.tv_home);
        mTvTwo = (TextView) findViewById(R.id.tv_two);
        mTvThree = (TextView) findViewById(R.id.tv_three);
        mTvThree04 = (TextView) findViewById(R.id.tv_three04);
        mTvThree05 = (TextView) findViewById(R.id.tv_three05);


        mIvHome = (ImageView) findViewById(R.id.iv_home);
        mIvTwo = (ImageView) findViewById(R.id.iv_two);
        mIvThree = (ImageView) findViewById(R.id.iv_three);
        mIvThree04 = (ImageView) findViewById(R.id.iv_three04);
        mIvThree05 = (ImageView) findViewById(R.id.iv_three05);
        mLinl = (LinearLayout) findViewById(R.id.linl);
        mLin02 = (LinearLayout) findViewById(R.id.lin02);

        //设置图片
        /*
        //设置button名字
        String buttonArr = mBean.getButtonarr();
        String[] split1 = buttonArr.split(",");
         mTvHome.setText(split1[0]);
        mTvTwo.setText(split1[1]);
        mTvThree.setText(split1[2]);
        mTvThree04.setText(split1[3]);
        mTvThree05.setText(split1[4]);

        String buttonImage = mBean.getButtonimage();
        String[] split = buttonImage.split(",");
        try {
            ImageUtils.showImage(this, split[0], mIvHome);
            ImageUtils.showImage(this, split[1], mIvTwo);
            ImageUtils.showImage(this, split[2], mIvThree);
            ImageUtils.showImage(this, split[3], mIvThree04);
            ImageUtils.showImage(this, split[4], mIvThree05);
        } catch (Exception e) {
        }*/
    }

    /**
     * 设置底部导航图片选择颜色
     *
     * @param view
     * @param b
     */
    private void selectImageViewColor(ImageView view, boolean b) {
        if (b) {
            setIconColor(view, 255, 0, 0, 255); //选中红色
        } else {
            setIconColor(view, 0, 0, 0, 255);  //未选择 黑色
        }
    }


    //设置图标的颜色
    private void setIconColor(ImageView icon, int r, int g, int b, int a) {
        /*float[] colorMatrix = new float[]{
                0, 0, 0, 0, r,
                0, 0, 0, 0, g,
                0, 0, 0, 0, b,
                0, 0, 0, (float) a / 255, 0
        };
        */
        float[] colorMatrix = new float[]{
                1, 0, 0, 0, r,
                0, 1, 0, 0, g,
                0, 0, 1, 0, b,
                0, 0, 0, 1, 0};
        icon.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    }

    private void initWebView() {
        // showProgressBarDialog();

        //初始化首页
        setWebView(mBean.getHome_url());
        initBottomMenu();
    }

    private void setWebView(String bean) {
        if (mAgentWeb == null) {
            mAgentWeb = AgentWeb.with(this)//传入Activity or Fragment
                    .setAgentWebParent(mLinl, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                    .useDefaultIndicator()// 使用默认进度条
                    .defaultProgressBarColor()
                    .setWebChromeClient(mWebChromeClient)
                    .setWebViewClient(mWebViewClient)
                    .addDownLoadResultListener(null)
                    .createAgentWeb()//
                    .ready()
                    .go(bean);
        } else {
            mMWebView = mAgentWeb.getWebCreator().get();
            mMWebView.loadUrl(bean);
        }
        mMWebView = mAgentWeb.getWebCreator().get();

        mMWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                // url 你要访问的下载链接
                // userAgent 是HTTP请求头部用来标识客户端信息的字符串
                // contentDisposition 为保存文件提供一个默认的文件名
                // mimetype 该资源的媒体类型
                // contentLength 该资源的大小
                // 这几个参数都是可以通过抓包获取的
                // 用手机默认浏览器打开链接
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                ToastUtil.showToast(NewWebActivity.this, "请稍等!正在跳转下载页面,下载APP更新包");
            }
        });
    }


    @Override
    public void onClick(View v) {
        showProgressBarDialog();
        switch (v.getId()) {
            case R.id.layout_home://主页
                setWebView(mBean.getHome_url());
                checkTvIvIsSelected(0);
                break;
            case R.id.layout_two: //后退
                mMWebView.goBack();// 返回前一个页面
                if (!mMWebView.canGoBack()) {
                    if (mProgressDialog != null)
                        mProgressDialog.dismiss();
                }
                checkTvIvIsSelected(1);
                break;
            case R.id.layout_three: //客服
                String service_url = mBean.getService_url();
                String s = service_url.replace("amp;", "");
                setWebView(s);
                checkTvIvIsSelected(2);
                break;
            case R.id.layout_three04://快充
                setWebView(mBean.getKc_url());
                checkTvIvIsSelected(3);
                break;
            case R.id.layout_three05: //刷新
                mMWebView.reload();
                checkTvIvIsSelected(4);
                break;
            default:
                break;
        }

    }

    /**
     * 初始化主界面底部导航
     */
    private List<TextView> tvMenu;
    private List<ImageView> ivMenu;

    private void initBottomMenu() {
        tvMenu = new ArrayList<>();
        ivMenu = new ArrayList<>();
        // 添加底部导航文字
        tvMenu.add(mTvHome);
        tvMenu.add(mTvTwo);
        tvMenu.add(mTvThree);
        tvMenu.add(mTvThree04);
        tvMenu.add(mTvThree05);

        // tvMenu.add(mTvFive);
        // 添加底部导航图片
        ivMenu.add(mIvHome);
        ivMenu.add(mIvTwo);
        ivMenu.add(mIvThree);
        ivMenu.add(mIvThree04);
        ivMenu.add(mIvThree05);
        // 选中首页
        mTvHome.setSelected(true);

        // selectImageViewColor(mIvHome, true);
    }

    /**
     * 检查底部导航栏图标是否选中
     *
     * @param target
     */
    private void checkTvMenuIsSelected(TextView target) {
        for (TextView tv : tvMenu) {
            if (tv == target)
                tv.setSelected(true);
            else
                tv.setSelected(false);
        }
    }

    /**
     * 设置图标和文字选中的颜色
     *
     * @param index
     */
    private void checkTvIvIsSelected(int index) {
        for (int i = 0; i < tvMenu.size(); i++) {
            if (i == index) {
                tvMenu.get(i).setSelected(true);
                //  selectImageViewColor(ivMenu.get(i), true);
            } else {
                tvMenu.get(i).setSelected(false);
                // selectImageViewColor(ivMenu.get(i), false);
            }
        }
    }

    private void showProgressBarDialog() {
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("我们正为您选择最快的路线,\n请耐心等待");
        mProgressDialog.setCanceledOnTouchOutside(true); // 点击加载框以外的区域
        mProgressDialog.show();
    }

    private void showDialog() {
        if (mAlertDialog == null)
            mAlertDialog = new AlertDialog.Builder(this)
                    .setMessage("您确定要关闭该页面吗?")
                    .setNegativeButton("再逛逛", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mAlertDialog != null)
                                mAlertDialog.dismiss();
                        }
                    })//
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mAlertDialog != null)
                                mAlertDialog.dismiss();
                            finish();
                        }
                    }).create();
        mAlertDialog.show();

    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //do you work
            Log.i("Info", "progress:" + newProgress);
            if (newProgress == 100) {
                //mImageView.setVisibility(View.GONE);
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();
            }

        }

    };
    private boolean isSuccess = false;
    private boolean isError = false;
    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!isError) {
                isSuccess = true;
                //回调成功后的相关操作
                mMWebView.setVisibility(View.VISIBLE);
                mLinl.setVisibility(View.VISIBLE);
                mLin02.setVisibility(View.GONE);
            }
            isError = false;
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            //showErrorPage();//显示错误页面
            mMWebView.setVisibility(View.GONE);
            mLinl.setVisibility(View.GONE);
            mLin02.setVisibility(View.VISIBLE);
            isError = true;
            isSuccess = false;

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }
    };

    @Override
    protected void onPause() {
        if (mAgentWeb != null)
            mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        if (mAgentWeb != null)
            mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAgentWeb != null)
            mAgentWeb.getWebLifeCycle().onDestroy();
    }


    /**
     * 监听返回--是否退出程序
     */
    private long exitTime = 0;

    //改写物理按键——返回的逻辑，希望浏览的网页后退而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mMWebView.canGoBack()) {
                mMWebView.goBack();//返回上一页面
                return true;
            } else {
                //  System.exit(0);//退出程序
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if ((System.currentTimeMillis() - exitTime) > 2000) {
                        ToastUtil.showToast(NewWebActivity.this, "再按一次退出程序！");
                        exitTime = System.currentTimeMillis();
                    } else {
                        finish();
                    }
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
