package com.abu.timermanager.data;

import com.abu.timermanager.util.API;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * @date: 2019/7/11 11:37
 * @author: 董长峰
 * @blog: https://www.jianshu.com/u/04a705fae99b
 * @description:
 */
public interface HolidayInfoInterface {
    /**
     *  获取节假日列表
     * @param year  查询年份
     * @return   节假日列表信息
     */
    @GET("year?key="+ API.KEY)
    Call<ResponseBody> getHolidayInfo(@Query("year") String year);

    /**
     *  获取节假日详情
     * @param date 查询日期
     * @return  节假日详情
     */
    @GET("day?key="+API.KEY)
    Call<ResponseBody> getHolidayDetail(@Query("date") String date);
}
