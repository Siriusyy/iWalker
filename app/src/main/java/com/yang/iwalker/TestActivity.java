package com.yang.iwalker;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.RadioGroup;

import com.yang.iwalker.adapter.HomeAdapter;
import com.yang.iwalker.fragment.BlankFragment;
import com.yang.iwalker.fragment.FriendsFragment;
import com.yang.iwalker.fragment.MapFragment;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        *//*setContentView(R.layout.login);
        Button login = findViewById(R.id.btn_login);
        login.setOnClickListener((view)->{
            Toast.makeText(this,"hello",Toast.LENGTH_LONG).show();
        });
        Button regist = findViewById(R.id.btn_reg);
        regist.setOnClickListener((view)->{
            Intent intent=new Intent(this,RegActivity.class);

            startActivity(intent);
        });*//*


    }*/


    private RadioGroup mTabRadioGroup;
    private SparseArray<Fragment> mFragmentSparseArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        initView();


    }

    private void initView() {
        mTabRadioGroup = findViewById(R.id.tabs_rg);
        mFragmentSparseArray = new SparseArray<>();
        mFragmentSparseArray.append(R.id.home_tab, BlankFragment.newInstance(""));
        mFragmentSparseArray.append(R.id.friends_tab, FriendsFragment.newInstance(""));
        mFragmentSparseArray.append(R.id.map_tab, MapFragment.newInstance("地图"));
        mFragmentSparseArray.append(R.id.self_tab, BlankFragment.newInstance("我的"));
        mTabRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 具体的fragment切换逻辑可以根据应用调整，例如使用show()/hide()
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        mFragmentSparseArray.get(checkedId)).commit();
            }
        });
        // 默认显示第一个
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                mFragmentSparseArray.get(R.id.home_tab)).commit();
        findViewById(R.id.sign_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this, FriendInfoActivity.class));
            }
        });




    }


}
