package com.yang.iwalker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yang.iwalker.NetWork.DoOkHttp;

public class LoginActivity extends Activity {
    private EditText et_username, et_password;
    private Button btn_login, btn_register;

    private DoOkHttp client;
    private String username, password;
    private User user;
    //private static final int SUCCESS = 0;
    //private static final int FALL=1;


    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();

        if(username != null && password != null){
            new Thread(connetRunnable).start();
        }

        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new Thread(connetRunnable).start();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this, RegActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    Runnable connetRunnable = new Runnable() {
        @Override
        public void run() {
            EnterPwd();
            LoginServer();
        }
    };
    /*
     * 登陆服务函数
     */
    //
    public void EnterPwd(){
        username = et_username.getText().toString();
        password = et_password.getText().toString();
    }

    public void LoginServer(){
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty((password))){
            Toast.makeText(this,"账号或密码为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //处理返回的信息
        String status = client.login(username, password);
        client.showFriends();
        if(status.equals("0")){
            Intent intent = new Intent(LoginActivity.this, TestActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", user);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }else{
            et_username.setText("");
            et_password.setText("");
        }
    }

    /*public void checkAndWrite(String username, String password){
        File file = new File(this.getCacheDir(),"user.dat");

        try{
            FileOutputStream fos = new FileOutputStream(file);
            fos.write((username + "##" + password).getBytes());
            Log.i("数据保存","数据保存成功");
            fos.close();
        }catch(Exception e){
            Toast.makeText(this,"数据保存失败",Toast.LENGTH_SHORT).show();
            Log.i("Main", e.toString());
        }
    }

    public void read(File file){
        if(file.exists() && file.length() > 0){
            try{
                FileInputStream fis = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                String userLogin = br.readLine();
                username = userLogin.split("##")[0];
                password = userLogin.split("##")[1];

                fis.close();
                br.close();
            }catch(Exception e){
                Log.i("缓存读取","文件不存在，或者读取错误");
            }
        }
    }

    public void delete(){
        File file = new File(this.getCacheDir(), "user.dat");
        if(file.exists() && file.length() > 0){
            file.delete();
        }
    }

    public void initData(){
        File file = new File(this.getCacheDir(), "user.dat");

        read(file);
    }*/

    public void init(){
        et_username = (EditText) findViewById(R.id.editText2);
        et_password = (EditText) findViewById(R.id.editText);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_reg);
        username = null;
        password = null;
        client = new DoOkHttp();
        user = new User();
    }
}
