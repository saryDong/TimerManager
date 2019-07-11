package com.abu.timermanager.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.abu.timermanager.R;
import com.abu.timermanager.bean.HolidayDetail;
import com.abu.timermanager.data.HolidayInfoInterface;
import com.abu.timermanager.util.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HolidayDetailActivity extends BaseActivity {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.holiday_name)
    TextView holidayName;
    @BindView(R.id.avoid)
    TextView avoid;
    @BindView(R.id.animalsYear)
    TextView animalsYear;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.suit)
    TextView suit;
    @BindView(R.id.lunarYear)
    TextView lunarYear;
    @BindView(R.id.lunar)
    TextView lunar;
    @BindView(R.id.holiday_time)
    TextView holidayTime;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_holiday_detail;
    }

    @Override
    protected void init() {
        super.init();
        setStatusBarTransparent();
        Intent intent = getIntent();
        if (intent != null) {
            String holiday_name = intent.getStringExtra("holiday_name");
            String holiday_time = intent.getStringExtra("holiday_time");
            initData(holiday_name, holiday_time);
            if (holidayTime!=null){
                holidayTime.setText("假日起始时间："+intent.getStringExtra("holiday_time"));
            }else {
                holidayTime.setVisibility(View.GONE);
            }
        }
    }

    private void initData(String holiday_name, String holiday_time) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.HOLIIDAY_INFO_ROOT)
                .build();

        HolidayInfoInterface service = retrofit.create(HolidayInfoInterface.class);
        Call<ResponseBody> net_result = service.getHolidayDetail(holiday_time);
        net_result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    parse(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void parse(String result) {
        HolidayDetail holidayDetail = new HolidayDetail();
        try {
            JSONObject object = new JSONObject(result);
            JSONObject object1 = object.getJSONObject("result");
            JSONObject object2 = object1.getJSONObject("data");
            holidayDetail.setAvoids(object2.optString("avoid"));
            holidayDetail.setAnimalsYears(object2.optString("animalsYear"));
            holidayDetail.setDes(object2.optString("desc"));
            holidayDetail.setHoliday(object2.optString("holiday"));
            holidayDetail.setSuits(object2.getString("suit"));
            holidayDetail.setLunarYears(object2.getString("lunarYear"));
            holidayDetail.setLunar(object2.optString("lunar"));

            if (!TextUtils.isEmpty(holidayDetail.getAnimalsYears())){
                animalsYear.setText("年份属相："+holidayDetail.getAnimalsYears());
            }else {
                animalsYear.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(holidayDetail.getAvoids())){
                avoid.setText("禁忌："+holidayDetail.getAvoids());
            }else {
                avoid.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(holidayDetail.getDes())){
                desc.setText("假期安排："+holidayDetail.getDes());
            }else {
                desc.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(holidayDetail.getHoliday())){
                holidayName.setText(holidayDetail.getHoliday());
            }else {
                holidayName.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(holidayDetail.getLunarYears())){
               lunarYear.setText("纪念："+holidayDetail.getLunarYears());
            }else {
                lunarYear.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(holidayDetail.getLunar())){
                lunar.setText("农历："+holidayDetail.getLunar());
            }else {
                lunar.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(holidayDetail.getSuits())){
               suit.setText("适宜："+holidayDetail.getSuits());
            }else {
                suit.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ib_back)
    public void onclick(){
        onBackPressed();
    }

}
