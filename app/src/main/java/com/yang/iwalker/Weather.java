package com.yang.iwalker;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.loopj.android.http.HttpGet;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.utils.URLEncodedUtils;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class Weather {
    private String city;

    public Weather(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Message showWhether(){
        String url = "https://free-api.heweather.net/s6/weather/now?";
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("location", city));
        params.add(new BasicNameValuePair("key", "fd0498c75ffa486f9b0f756314f8669e"));
        String param = URLEncodedUtils.format(params, "utf-8");
        HttpGet httpGet = new HttpGet(url+"&"+param);
        HttpClient httpClient = new DefaultHttpClient();
        Message m = new Message();
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            String result = getJsonStringFromGZIP(httpResponse);// 获取到解压缩之后的字符串

            JSONObject obj = new JSONObject(result);
            if (obj != null) {
                String tmp = obj.getJSONArray("HeWeather6").getJSONObject(0).getJSONObject("now").getString("tmp");
                String weather = obj.getJSONArray("HeWeather6").getJSONObject(0).getJSONObject("now").getString("cond_txt");
                Bundle b = new Bundle();
                b.putString("tmp", tmp);
                b.putString("weather", weather);
                m.setData(b);
                m.what = 1;
                //whandler.sendMessage(m);
                return m;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        m.what = 2;
        return m;
    }

    private String getJsonStringFromGZIP(HttpResponse response) {
        String jsonString = null;
        try {
            InputStream is = response.getEntity().getContent();
            BufferedInputStream bis = new BufferedInputStream(is);
            bis.mark(2);
            // 取前两个字节
            byte[] header = new byte[2];
            int result = bis.read(header);
            // reset输入流到开始位置
            bis.reset();
            // 判断是否是GZIP格式
            int headerData = getShort(header);
            if (result != -1 && headerData == 0x1f8b) {
                is = new GZIPInputStream(bis);
            } else {
                is = bis;
            }
            InputStreamReader reader = new InputStreamReader(is, "utf-8");
            char[] data = new char[100];
            int readSize;
            StringBuffer sb = new StringBuffer();
            while ((readSize = reader.read(data)) > 0) {
                sb.append(data, 0, readSize);
            }
            jsonString = sb.toString();
            bis.close();
            reader.close();
        } catch (Exception e) {
            Log.e("HttpTask", e.toString(), e);
        }
        return jsonString;
    }
    private int getShort(byte[] data) {
        return (int) ((data[0] << 8) | data[1] & 0xFF);
    }
}
