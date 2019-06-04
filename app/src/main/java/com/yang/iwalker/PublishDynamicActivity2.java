package com.yang.iwalker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yang.iwalker.NetWork.DoOkHttp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PublishDynamicActivity2 extends AppCompatActivity{
    /** 使用照相机拍照获取图片 */
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    /** 裁剪图片 */
    private static final int RESULT_REQUEST_CODE = 3;
    /** 图片名称 */
    private static final String IMAGE_FILE_NAME = "image.jpg";
    private Uri takePhotouri;
    Location location;
    ImageView iv;
    ImageView iv2;
    TextView tv;
    TextView back;
    Button publish;
    DoOkHttp client;
    EditText editText;
    boolean flat = false;
    Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_dynamic_onepic);
        iv = findViewById(R.id.add_one_pic);
        iv2 = findViewById(R.id.add_one_pic_img);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        location = new Location(getApplicationContext());
        location.initLocation();
        tv = findViewById(R.id.dynamic_self_loc);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initButton();
        editText = findViewById(R.id.editText1);
        client = new DoOkHttp();
        new Thread(loc_run).start();
    }

    //emoj加密
    //mContent = StringEscapeUtils.escapeJava(emojiEditText.getText().toString());
    // emoj解密
    //mComment.setContent(StringEscapeUtils.unescapeJava(json.getJSONObject(i).getString("content").replace("\\\\u","\\u")));


    Runnable loc_run = new Runnable() {
        @Override
        public void run() {
            while(location.city.equals("")){
            }
            Message m = new Message();
            m.what = 1;
            lHandler.sendMessage(m);
        }
    };

    Handler lHandler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    tv.setText(location.district);
                    break;
                case 2:
                    Toast.makeText(PublishDynamicActivity2.this, "位置信息为空", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Log.e("TAG","ActivityResult resultCode error");
            return;
        }
        switch (requestCode){
            case SELECT_PIC_BY_TACK_PHOTO:
                //裁剪图片
                startPhotoZoom(takePhotouri);
                break;
            case RESULT_REQUEST_CODE :
                if (data != null) {
                    Bundle extras = data.getExtras();
                    photo = extras.getParcelable("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0 - 100)压缩文件
                    iv.setImageBitmap(photo);
                    iv2.setVisibility(View.INVISIBLE);
                    flat = true;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
    private void takePhoto() {
        // 执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
            File path = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File file = new File(path, IMAGE_FILE_NAME);
            takePhotouri = Uri.fromFile(file);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, takePhotouri);
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else {
            Toast.makeText(getApplicationContext(), "内存卡不存在",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);
        //将URI指向相应的file:///…
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        // 不返回图片文件
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }
    public void initButton(){
        publish = findViewById(R.id.publish);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> l = new ArrayList<>();
                        l.add(location.country);
                        l.add(location.province);
                        l.add(location.city);
                        l.add(location.district);
                        JsonObject object = client.createActivity(editText.getText().toString(), location.latitude, location.longitude, l.toString());
                        if(!object.isJsonNull()){
                            String id = object.get("id").getAsString();
                            if(flat){
                                /*iv.setDrawingCacheEnabled(true);
                                Bitmap b = iv.getDrawingCache();
                                iv.setDrawingCacheEnabled(false);*/

                                JsonObject object1 = client.createImage(BitmapTool.getFile(photo), id, "1");
                                if(!object1.isJsonNull()){
                                    Message msg = new Message();
                                    msg.what = 1;
                                    infoHandler.sendMessage(msg);
                                }else{
                                    Message msg = new Message();
                                    msg.what = 2;
                                    infoHandler.sendMessage(msg);
                                }
                            }else{
                                Message msg = new Message();
                                msg.what = 3;
                                infoHandler.sendMessage(msg);
                            }
                        }else{
                            Message msg = new Message();
                            msg.what = 4;
                            infoHandler.sendMessage(msg);
                        }
                    }
                }).start();
            }
        });
    }
    Handler infoHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(PublishDynamicActivity2.this, "发送成功！", Toast.LENGTH_SHORT);
                    finish();
                    break;
                case 2:
                    Toast.makeText(PublishDynamicActivity2.this, "图片发送失败！", Toast.LENGTH_SHORT);
                    break;
                case 3:
                    Toast.makeText(PublishDynamicActivity2.this, "发送成功！", Toast.LENGTH_SHORT);
                    finish();
                    break;
                case 4:
                    Toast.makeText(PublishDynamicActivity2.this, "发送失败！", Toast.LENGTH_SHORT);
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
