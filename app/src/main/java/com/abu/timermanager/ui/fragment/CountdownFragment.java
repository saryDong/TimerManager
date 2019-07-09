package com.abu.timermanager.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abu.timermanager.R;
import com.abu.timermanager.adapter.CountdownFragmentAdapter;
import com.abu.timermanager.data.HolidayItem;
import com.abu.timermanager.event.ScrollEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @date: 2019/7/9 10:55
 * @author: 董长峰
 * @blog: https://www.jianshu.com/u/04a705fae99b
 * @description: 节假日列表fragment
 */
public class CountdownFragment extends Fragment {
    String HOLIDAY_Msg = "{\n" +
            "  \"reason\": \"Success\",\n" +
            "  \"result\": {\n" +
            "    \"data\": {\n" +
            "      \"holidaylist\": \"[{\\\"name\\\":\\\"元旦\\\",\\\"startday\\\":\\\"2018-1-1\\\"},{\\\"name\\\":\\\"除夕\\\",\\\"startday\\\":\\\"2018-2-15\\\"},{\\\"name\\\":\\\"春节\\\",\\\"startday\\\":\\\"2018-2-16\\\"},{\\\"name\\\":\\\"清明节\\\",\\\"startday\\\":\\\"2018-4-5\\\"},{\\\"name\\\":\\\"劳动节\\\",\\\"startday\\\":\\\"2018-5-1\\\"},{\\\"name\\\":\\\"端午节\\\",\\\"startday\\\":\\\"2018-6-18\\\"},{\\\"name\\\":\\\"中秋节\\\",\\\"startday\\\":\\\"2018-9-24\\\"},{\\\"name\\\":\\\"国庆节\\\",\\\"startday\\\":\\\"2018-10-1\\\"}]\",\n" +
            "      \"year\": \"2018\",\n" +
            "      \"holiday_list\": [\n" +
            "        {\n" +
            "          \"name\": \"元旦\",\n" +
            "          \"startday\": \"2018-1-1\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"除夕\",\n" +
            "          \"startday\": \"2018-2-15\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"春节\",\n" +
            "          \"startday\": \"2018-2-16\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"清明节\",\n" +
            "          \"startday\": \"2018-4-5\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"劳动节\",\n" +
            "          \"startday\": \"2018-5-1\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"端午节\",\n" +
            "          \"startday\": \"2018-6-18\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"中秋节\",\n" +
            "          \"startday\": \"2018-9-24\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"国庆节\",\n" +
            "          \"startday\": \"2018-10-1\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  },\n" +
            "  \"error_code\": 0\n" +
            "}";
    @BindView(R.id.countdouwn_list)
    RecyclerView countdouwnList;

    private CountdownFragmentAdapter mFragmentAdapter;
    private List<HolidayItem> mHolidayItems;
    private LinearLayoutManager manager;
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
        mHolidayItems=new ArrayList<>();

        try {
            JSONObject object=new JSONObject(HOLIDAY_Msg);
            JSONObject object1=object.getJSONObject("result");
            JSONObject object2=object1.getJSONObject("data");
            JSONArray array=object2.optJSONArray("holiday_list");
            for (int i=0;i<array.length();i++){
                HolidayItem holidayItem=new HolidayItem();
                holidayItem.setHolidayName(array.getJSONObject(i).getString("name"));
                holidayItem.setHolidayTime(array.getJSONObject(i).getString("startday"));
                mHolidayItems.add(holidayItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mFragmentAdapter=new CountdownFragmentAdapter(getContext(),mHolidayItems);
        countdouwnList.addOnScrollListener(mOnScrollListener);
        manager=new LinearLayoutManager(getActivity());
        mLayoutManager=new GridLayoutManager(getContext(),2);
        countdouwnList.setLayoutManager(mLayoutManager);
        countdouwnList.setAdapter(mFragmentAdapter);
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
}
