package com.yang.iwalker.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yang.iwalker.ChangeInfo;
import com.yang.iwalker.R;


public class SelfFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SHOW_TEXT = "text";

    private static int output_X = 200;
    private static int output_Y = 200;
    private String mContentText;

    private static final int CODE_GALLERY_REQUEST = 0;
    private static final int CODE_RESULT_REQUEST = 2;
    ImageView user_image;
    TextView user_name;
    TextView user_gender;
    TextView user_info;
    Button btn_change_data;
    Button btn_user_footprint;
    Button btn_user_notice;
    Button btn_feedback;

    public SelfFragment() {
        // Required empty public constructor
    }

    public static SelfFragment newInstance(String param1) {
        SelfFragment fragment = new SelfFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SHOW_TEXT, param1);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContentText = getArguments().getString(ARG_SHOW_TEXT);
        }

    }

    private View rootView;

    View getRootView() {
        return rootView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_self, container, false);
        TextView contentTv = rootView.findViewById(R.id.content_tv);
        contentTv.setText(mContentText);
        user_image = rootView.findViewById(R.id.user_image);
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseHeadImageFromGallery();
            }
        });
        initText();
        initButton();
        initButtonClick();
        new Thread(getInfoRunnable).start();
        return rootView;
    }

    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        /*Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);*/
        Intent intent1 = new Intent(Intent.ACTION_PICK, null);
        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent1, CODE_GALLERY_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {

        // 用户没有进行有效的设置操作，返回
        //if (resultCode == RESULT_CANCELED) {
        //    Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
        //    return;
        // }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());
                break;


            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }
    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            user_image.setImageBitmap(photo);
        }
    }
    public void initText(){
        user_name = rootView.findViewById(R.id.user_name);
        user_gender = rootView.findViewById(R.id.user_gender);
        user_info = rootView.findViewById(R.id.user_signature);
    }
    public void initButton(){
        btn_change_data = rootView.findViewById(R.id.change_data);
        btn_user_footprint = rootView.findViewById(R.id.user_footprint);
        btn_user_notice = rootView.findViewById(R.id.user_notice);
        btn_feedback = rootView.findViewById(R.id.help_feedback);
    }

    public void initButtonClick(){
        btn_change_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                startActivity(new Intent(getContext(), ChangeInfo.class));
            }
        });
    }

    Runnable getInfoRunnable = new Runnable() {
        @Override
        public void run() {
            Message m = new Message();
            m.what = 1;
            getInfoHandler.sendMessage(m);
        }
    };
    Handler getInfoHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:

                    break;
            }
        }
    };
}
