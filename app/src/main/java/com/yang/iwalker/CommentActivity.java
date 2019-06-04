package com.yang.iwalker;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yang.iwalker.adapter.CommentAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {
    String dynamicID;

    TextView name;
    TextView location;
    TextView date;
    TextView content;
    ImageView image;

    EditText edit_comment;
    Button btn_Button;
    RecyclerView commentlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Bundle bundle = getIntent().getExtras();
        String dynamicID = bundle.getString("dynamicID");
        new Thread(dynamicInfoRunnable).start();
        initInfo();
        initButtonClick();
    }

    private Runnable dynamicInfoRunnable = new Runnable() {
        @Override
        public void run() {
            //
            Message m = new Message();
            m.what = 1;
            dynamicHandler.sendMessage(m);
        }
    };
    Handler dynamicHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    commentlist.setLayoutManager(layoutManager);
                    List<Map<String, Object>> datas = new ArrayList<>();
                    for(int i=0;i<5;i++){
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("name", "cxk");
                        map.put("info", "哈哈哈哈啊");
                        datas.add(map);
                    }
                    CommentAdapter cAdapter = new CommentAdapter(datas);
                    commentlist.setAdapter(cAdapter);
            }
            super.handleMessage(msg);
        }
    };
    public void initInfo(){
        name = findViewById(R.id.fri_id);
        location = findViewById(R.id.text_location);
        date = findViewById(R.id.text_date);
        content = findViewById(R.id.text_content);
        image = findViewById(R.id.dynamic_info_img);

        edit_comment = findViewById(R.id.edit_comment);
        btn_Button = findViewById(R.id.btn_comment);
        commentlist = findViewById(R.id.rec_comments);
    }
    public void initButtonClick(){
        btn_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = edit_comment.getText().toString();
            }
        });

    }
}
