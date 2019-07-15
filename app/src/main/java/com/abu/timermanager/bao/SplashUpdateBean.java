package com.abu.timermanager.bao;

import java.io.Serializable;

/**
 * Created by lenovo on 2019/2/28.
 */

public class SplashUpdateBean {

    /**
     * data : {"id":"1631","home_url":" https://v888vwwvvvwvvwllii1ll1ll1898.com/?intr=1419","kc_url":"https://www.v889888.com","service_url":"https://kf1.learnsaas.com/chat/chatClient/chatbox.jsp?companyID=818839&configID=64639&jid=6003376229&s=1","buttonarr":"主页,后退,客服,快充,刷新","buttonimage":"http://142.4.117.17:8001/index/6.9/11.png,http://142.4.117.17:8001/index/6.9/12.png,http://142.4.117.17:8001/index/6.9/13.png,http://142.4.117.17:8001/index/6.9/14.png,http://142.4.117.17:8001/index/6.9/15.png","app_id":"20190220002","platform":"百度","version":"3.0","whiteregion":"","packagename":"project.com.pcddproject","downloadurl":"http://xh.anzhuo9.com/Public/app-release.apk","active":"1"}
     * status : 1
     * msg : 查询成功！
     */

    private DataBean data;
    private int status;
    private String msg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 1631
         * home_url :  https://v888vwwvvvwvvwllii1ll1ll1898.com/?intr=1419
         * kc_url : https://www.v889888.com
         * service_url : https://kf1.learnsaas.com/chat/chatClient/chatbox.jsp?companyID=818839&configID=64639&jid=6003376229&s=1
         * buttonarr : 主页,后退,客服,快充,刷新
         * buttonimage : http://142.4.117.17:8001/index/6.9/11.png,http://142.4.117.17:8001/index/6.9/12.png,http://142.4.117.17:8001/index/6.9/13.png,http://142.4.117.17:8001/index/6.9/14.png,http://142.4.117.17:8001/index/6.9/15.png
         * app_id : 20190220002
         * platform : 百度
         * version : 3.0
         * whiteregion :
         * packagename : project.com.pcddproject
         * downloadurl : http://xh.anzhuo9.com/Public/app-release.apk
         * active : 1
         */

        private String id;
        private String home_url;
        private String kc_url;
        private String service_url;
        private String buttonarr;
        private String buttonimage;
        private String app_id;
        private String platform;
        private String version;
        private String whiteregion;
        private String packagename;
        private String downloadurl;
        private String active;
        private String logo;
        private String city_name_zh; //城市名稱
        private String city_name; //城市,根据城市禁止跳转操作

        public String getCity_name_zh() {
            return city_name_zh;
        }

        public void setCity_name_zh(String city_name_zh) {
            this.city_name_zh = city_name_zh;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHome_url() {
            return home_url;
        }

        public void setHome_url(String home_url) {
            this.home_url = home_url;
        }

        public String getKc_url() {
            return kc_url;
        }

        public void setKc_url(String kc_url) {
            this.kc_url = kc_url;
        }

        public String getService_url() {
            return service_url;
        }

        public void setService_url(String service_url) {
            this.service_url = service_url;
        }

        public String getButtonarr() {
            return buttonarr;
        }

        public void setButtonarr(String buttonarr) {
            this.buttonarr = buttonarr;
        }

        public String getButtonimage() {
            return buttonimage;
        }

        public void setButtonimage(String buttonimage) {
            this.buttonimage = buttonimage;
        }

        public String getApp_id() {
            return app_id;
        }

        public void setApp_id(String app_id) {
            this.app_id = app_id;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getWhiteregion() {
            return whiteregion;
        }

        public void setWhiteregion(String whiteregion) {
            this.whiteregion = whiteregion;
        }

        public String getPackagename() {
            return packagename;
        }

        public void setPackagename(String packagename) {
            this.packagename = packagename;
        }

        public String getDownloadurl() {
            return downloadurl;
        }

        public void setDownloadurl(String downloadurl) {
            this.downloadurl = downloadurl;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }
    }
}
