package com.abu.timermanager.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abu.timermanager.R;
import com.abu.timermanager.adapter.CountdownFragmentAdapter;
import com.abu.timermanager.bean.HolidayItem;
import com.abu.timermanager.data.HolidayInfoInterface;
import com.abu.timermanager.event.ScrollEvent;
import com.abu.timermanager.util.API;
import com.abu.timermanager.util.Configer;
import com.abu.timermanager.util.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @date: 2019/7/9 10:55
 * @author: 董长峰
 * @blog: https://www.jianshu.com/u/04a705fae99b
 * @description: 节假日列表fragment
 */
public class CountdownFragment extends Fragment {

    @BindView(R.id.countdouwn_list)
    RecyclerView countdouwnList;

    private CountdownFragmentAdapter mFragmentAdapter;
    private List<HolidayItem> mHolidayItems;
    private GridLayoutManager mLayoutManager;

    private int mScrollState = 0;
    private static final int SCROLL_UP = 1;
    private static final int SCROLL_DOWN = 2;

    public CountdownFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_countdown, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        String result=SPUtils.getStringFromSP(Configer.HOLIDAY_INFO_LIST, null);
        String  SavedYear=SPUtils.getStringFromSP(Configer.HOLIDAY_YEAR,null);
        Date  date=new Date(System.currentTimeMillis());
        String year=String.format("%tY", date);
        if (year.equals(SavedYear)&&result!=null){
            parse(result);
        }else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API.HOLIIDAY_INFO_ROOT)
                    .build();

            HolidayInfoInterface service = retrofit.create(HolidayInfoInterface.class);
            Call<ResponseBody> net_result=service.getHolidayInfo(year);
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
    }

    private void parse(String result) {
        mHolidayItems=new ArrayList<>();
        try {
            JSONObject object=new JSONObject(result);
            JSONObject object1=object.getJSONObject("result");
            JSONObject object2=object1.getJSONObject("data");
            JSONArray array=object2.optJSONArray("holiday_list");
            for (int i=0;i<array.length();i++){
                HolidayItem holidayItem=new HolidayItem();
                holidayItem.setName(array.getJSONObject(i).getString("name"));
                holidayItem.setStartday(array.getJSONObject(i).getString("startday"));
                mHolidayItems.add(holidayItem);
            }
            mFragmentAdapter=new CountdownFragmentAdapter(getContext(),mHolidayItems);
            countdouwnList.addOnScrollListener(mOnScrollListener);
            mLayoutManager=new GridLayoutManager(getContext(),1);
            countdouwnList.setLayoutManager(mLayoutManager);
            countdouwnList.setAdapter(mFragmentAdapter);
            SPUtils.setString2SP(Configer.HOLIDAY_YEAR,object2.optString("year"));
            SPUtils.setString2SP(Configer.HOLIDAY_INFO_LIST,result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if ( dy != 0) {
                postScrollEvent(dy);
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (mFragmentAdapter == null || mFragmentAdapter.getItemCount() == 0) {
                return;
            }
        }
    };

    /**
     * 发布滚动事件
     * @param dy RecyclerView在y轴上的变化量
     */
    private void postScrollEvent(int dy) {
        if (dy > 0) {//RecyclerView向上滚动
            if (mScrollState != SCROLL_UP) {
                EventBus.getDefault().post(new ScrollEvent(ScrollEvent.Direction.UP));
                mScrollState = SCROLL_UP;
            }
        } else {//RecyclerView向下滚动
            if (mScrollState != SCROLL_DOWN) {
                EventBus.getDefault().post(new ScrollEvent(ScrollEvent.Direction.DOWN));
                mScrollState = SCROLL_DOWN;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放所有倒计时
        if (mFragmentAdapter != null) {
            mFragmentAdapter.cancelAllTimers();
        }
    }
}
