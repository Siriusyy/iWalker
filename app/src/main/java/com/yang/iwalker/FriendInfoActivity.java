package com.yang.iwalker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.xuexiang.xui.utils.StatusBarUtils;

public class FriendInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        /*沉浸式状态栏*/
        StatusBarUtils.translucent(this);
        View view = findViewById(R.id.text_del);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FriendInfoActivity.this,"为什么要删除我？",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
