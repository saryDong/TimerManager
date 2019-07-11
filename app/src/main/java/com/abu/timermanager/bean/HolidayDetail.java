package com.abu.timermanager.bean;

/**
 * @date: 2019/7/11 13:38
 * @author: 董长峰
 * @blog: https://www.jianshu.com/u/04a705fae99b
 * @description:
 */
public class HolidayDetail {
    public String holiday; //假日
    public String avoids;  //禁忌
    public String animalsYears; //属相
    public String des; //详情
    public String suits; //适宜
    public String lunarYears; //纪年
    public String lunar;

    public String getLunar() {
        return lunar;
    }

    public void setLunar(String lunar) {
        this.lunar = lunar;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getAvoids() {
        return avoids;
    }

    public void setAvoids(String avoids) {
        this.avoids = avoids;
    }

    public String getAnimalsYears() {
        return animalsYears;
    }

    public void setAnimalsYears(String animalsYears) {
        this.animalsYears = animalsYears;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getSuits() {
        return suits;
    }

    public void setSuits(String suits) {
        this.suits = suits;
    }

    public String getLunarYears() {
        return lunarYears;
    }

    public void setLunarYears(String lunarYears) {
        this.lunarYears = lunarYears;
    }
}
