package com.abu.timermanager.bao;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

/**
 * @author Wolf on 2019/2/27.
 * @name
 */
public class LocationUtils {

    private static final String TAG = "LocationUtils";

    /**
     * http://ip-api.com/json/58.192.32.1?fields=520191&lang=en
     * 根据ip获取位置信息
     *
     * @param ip
     * @return {"accuracy":50,"as":"AS4538 China Education and Research Network Center",
     * "city":"Nanjing","country":"China","countryCode":"CN","isp":
     * "China Education and Research Network Center","lat":32.0617,"lon":118.7778,"mobile":false,
     * "org":"China Education and Research Network Center","proxy":false,"query":"58.192.32.1",
     * "region":"JS","regionName":"Jiangsu","status":"success","timezone":"Asia/Shanghai","zip":""}
     */
    public static JSONObject Ip2Location(String ip) {
        JSONObject jsonObject = null;

        String urlStr = "http://ip-api.com/json/" + ip + "?fields=520191&lang=en";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(5000);//读取超时
            urlConnection.setConnectTimeout(5000); // 连接超时
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //找到服务器的情况下,可能还会找到别的网站返回html格式的数据
                InputStream is = urlConnection.getInputStream();

                BufferedReader buff = new BufferedReader(new InputStreamReader(is, "UTF-8"));//注意编码，会出现乱码
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = buff.readLine()) != null) {
                    builder.append(line);
                }
                buff.close();//内部会关闭InputStream
                urlConnection.disconnect();

                String res = builder.toString();

                Log.i(TAG, "Ip2Location: res -- " + res);
                if (isJson(res)) {
                    jsonObject = new JSONObject(res);

                }
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    /**
     * 根据ip通过百度api去获取城市
     *
     * @param ip
     * @return
     */
    public static String Ip2LocationByBaiduApi(String ip) {
        try {
            URL url = new URL("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=" + ip);
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line = null;
            StringBuffer res = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                res.append(line);
            }
            reader.close();
            String ipAddr = res.toString();
            if (isJson(ipAddr)) {
                JSONObject jsonObject = new JSONObject(ipAddr);
                if ("1".equals(jsonObject.get("ret").toString())) {
                    return jsonObject.get("city").toString();
                } else {
                    return "读取失败";
                }

            } else {
                return "访问后得到的不是json数据, res -- " + ipAddr;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "读取失败 e -- " + e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "读取失败 e -- " + e.getMessage();
        } catch (JSONException e) {
            e.printStackTrace();
            return "读取失败 e -- " + e.getMessage();
        }
    }

    /**
     * 判斷是否為json
     *
     * @param content
     * @return
     */
    public static boolean isJson(String content) {
        try {
            JSONObject jsonStr = new JSONObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取IP地址(内网)
     *
     * @param context
     * @return
     */
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }


            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }


    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * 获取外网IP地址
     *
     * @param handler
     * @return
     */
    public static void GetNetIp(final Handler handler) {
        new Thread() {
            @Override
            public void run() {
                String line = "";
                URL infoUrl = null;
                InputStream inStream = null;
                try {
                    infoUrl = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
                    URLConnection connection = infoUrl.openConnection();
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;
                    int responseCode = httpConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        inStream = httpConnection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
                        StringBuilder strber = new StringBuilder();
                        while ((line = reader.readLine()) != null)
                            strber.append(line + "\n");
                        inStream.close();
                        // 从反馈的结果中提取出IP地址
                        int start = strber.indexOf("{");
                        int end = strber.indexOf("}");
                        String json = strber.substring(start, end + 1);
                        if (json != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                line = jsonObject.optString("cip");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        Log.i("WOLF", "============line:" + line);

                        IpToLocation(line, handler);

                    }
                } catch (MalformedURLException e) {
                    Message msg = new Message();
                    msg.what = -1;
                    //向主线程发送消息
                    handler.sendMessage(msg);
                    e.printStackTrace();
                } catch (IOException e) {
                    Message msg = new Message();
                    msg.what = -1;
                    //向主线程发送消息
                    handler.sendMessage(msg);
                    e.printStackTrace();
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = -1;
                    //向主线程发送消息
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private static void IpToLocation(final String line, final Handler handler) {
        OkGo.<String>get("http://apis.juhe.cn/ip/ipNew?ip=" + line + "&key=52e9525a7fd3ee50cfbf07189bc87181")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = response.body();
                        //向主线程发送消息
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                        Message msg = new Message();
                        msg.what = -1;
                        //向主线程发送消息
                        handler.sendMessage(msg);
                    }
                });
    }

    private static void NewIpToLocation(final String line, final Handler handler) {
        String url = "http://apis.juhe.cn/ip/ipNew?ip=" + line + "&key=52e9525a7fd3ee50cfbf07189bc87181";
        String jsonByInternet = getJsonByInternet(url);
        if (jsonByInternet == null) {
            Log.i("WOLF", "========IpToLocation==onRequestError===:" + jsonByInternet);
            Message msg = new Message();
            msg.what = -1;
            //向主线程发送消息
            handler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = jsonByInternet;
            Log.i("WOLF", "==========(String )result.getObj()===:" + jsonByInternet);
            //向主线程发送消息
            handler.sendMessage(msg);
        }
    }

    public static String getJsonByInternet(String path) {
        try {
            URL url = new URL(path.trim());
            //打开连接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (200 == urlConnection.getResponseCode()) {
                //得到输入流
                InputStream is = urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while (-1 != (len = is.read(buffer))) {
                    baos.write(buffer, 0, len);
                    baos.flush();
                }
                return baos.toString("utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}