package com.yang.iwalker;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yang.iwalker.NetWork.DoOkHttp;
import com.yang.iwalker.adapter.CommentAdapter;
import com.yang.iwalker.adapter.HomeAdapter;

public class CommentActivity extends AppCompatActivity {
    DoOkHttp client;
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
        dynamicID = bundle.getString("dynamicID");
        initInfo();
        initButtonClick();
        client = new DoOkHttp();
        new Thread(dynamicInfoRunnable).start();
    }

    JsonObject dynamic;
    JsonArray commentsdatas;
    Bitmap bitmap = null;

    private Runnable dynamicInfoRunnable = new Runnable() {
        @Override
        public void run() {
            dynamic = client.getActivityInfo(dynamicID);
            commentsdatas = client.getCommit(dynamicID, "0", "10");
            if(!dynamic.get("images").isJsonNull()) {
                JsonArray jArray = dynamic.get("images").getAsJsonArray();
                if (jArray.size() > 0) {
                    JsonObject j = jArray.get(0).getAsJsonObject();
                    bitmap = BitmapTool.returnBitMap(j.get("image").getAsString());
                }
            }
            Message m = new Message();
            m.what = 1;
            dynamicHandler.sendMessage(m);
        }
    };
    Handler dynamicHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            LinearLayoutManager layoutManager;
            CommentAdapter cAdapter;
            switch (msg.what){
                case 1:
                    name.setText(dynamic.get("userName").getAsString());

                    String l = dynamic.get("locationName").getAsString();
                    String location1 = l.split(",")[3];
                    String s = location1.substring(1, location1.length()-1);
                    location.setText(s);

                    String d = HomeAdapter.stampToDate(dynamic.get("createTime").getAsString());
                    date.setText(d);

                    String c = dynamic.get("content").getAsString();
                    content.setText(c);

                    if(bitmap != null)
                        image.setImageBitmap(bitmap);

                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    commentlist.setLayoutManager(layoutManager);
                    cAdapter = new CommentAdapter(commentsdatas);
                    commentlist.setAdapter(cAdapter);
                    break;
                case 2:
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    commentlist.setLayoutManager(layoutManager);
                    cAdapter = new CommentAdapter(commentsdatas);
                    commentlist.setAdapter(cAdapter);
                    break;
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String s = edit_comment.getText().toString();
                        String status = client.addCommit(s, dynamicID);
                        if(status.equals("0")){
                            commentsdatas = client.getCommit(dynamicID, "0", "10");
                            Message msg = new Message();
                            msg.what = 2;
                            dynamicHandler.sendMessage(msg);
                        }
                        else{

                        }
                    }
                }).start();
                edit_comment.setText("");
                edit_comment.clearFocus();
                InputMethodManager manager = ((InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE));
                if(manager!=null){
                    manager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

    }
}
