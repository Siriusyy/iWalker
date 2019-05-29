package com.yang.iwalker;

import android.app.Activity;
import android.os.Bundle;

import com.xuexiang.xui.widget.activity.BaseGuideActivity;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseGuideActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected List<Integer> getGuidesResIdList() {
       List<Integer> list =new ArrayList<>();
       list.add(R.mipmap.guide1);
       list.add(R.mipmap.guide2);
       list.add(R.mipmap.guide3);

        return list;
    }

    @Override
    protected Class<? extends Activity> getSkipClass() {
        return TestActivity.class;
    }
}
