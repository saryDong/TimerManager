package com.abu.timermanager.bao;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/11/21.
 */

public class SplashBean implements Serializable {

    /**
     * status : 1
     * data : {"app_id":"20170913001","version":"1.0","kc_url":"","home_url":"","service_url":"","buttonArr":"","buttonImage":""}
     */

    private int status;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * app_id : 20170913001
         * version : 1.0
         * kc_url :
         * home_url :
         * service_url :
         * buttonArr :
         * buttonImage :
         */

        private String app_id;
        private String version;
        private String kc_url;
        private String home_url;
        private String service_url;
        private String buttonarr;
        private String buttonimage;
        private String active;

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public String getApp_id() {
            return app_id;
        }

        public void setApp_id(String app_id) {
            this.app_id = app_id;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getKc_url() {
            return kc_url;
        }

        public void setKc_url(String kc_url) {
            this.kc_url = kc_url;
        }

        public String getHome_url() {
            return home_url;
        }

        public void setHome_url(String home_url) {
            this.home_url = home_url;
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
    }
}
