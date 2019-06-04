package com.yang.iwalker.NetWork;

import android.net.http.HttpResponseCache;
import android.provider.MediaStore;
import android.util.Log;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoOkHttp {
    private OkHttpClient okHttpClient;
    private JsonParser parse;
    private static HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
    private final String login_url = "http://10.120.173.204:8088/user/login.do";
    private final String reg_url = "http://10.120.173.204:8088/user/register.do";
    private final String logout_url = "http://10.120.173.204:8088/user/logout.do";
    private final String modify_url = "http://10.120.173.204:8088/user/modify.do";
    private final String userInfo_url = "http://10.120.173.204:8088/user/get_user_info.do";
    private final String findFriend_url = "http://10.120.173.204:8088/user/findfriend.do";
    private final String friends_url = "http://10.120.173.204:8088/relation/showfriend.do";
    private final String addFriend_url = "http://10.120.173.204:8088/relation/addfriend.do";
    private final String comfirm_url = "http://10.120.173.204:8088/relation/confirmfriend.do";
    private final String showRequest_url = "http://10.120.173.204:8088/relation/showrequest.do";
    private final String createImage_url = "http://10.120.173.204:8088/image/create.do";
    private final String modifyActivity_url = "http://10.120.173.204:8088/activity/modify.do";
    private final String createActivity_url = "http://10.120.173.204:8088/activity/create.do";
    private final String deleteActivity_url = "http://10.120.173.204:8088/activity/delete.do";
    private final String deleteImage_url = "http://10.120.173.204:8088/image/delete.do";
    private final String getActivityInfo_url="http://10.120.173.204:8088/activity/get_activity_info.do";
    private final String getActivityByUser_url = "http://10.120.173.204:8088/activity/get_activities_by_user.do";
    private final String getActivityByLoc_url = "http://10.120.173.204:8088/activity/get_activities_by_location_name.do";
    private final String getImageByActivity_url = "http://10.120.173.204:8088/image/get_by_activity.do";
    private final String getCommit_url = "http://10.120.173.204:8088/comment/showcomment.do";
    private final String addCommit_url = "http://10.120.173.204:8088/comment/addcomment.do";
    private final String unlike_url = "http://10.120.173.204:8088/like/unlike.do";
    private final String like_url = "http://10.120.173.204:8088/like/like.do";
    private final String getAllAct_url = "http://10.120.173.204:8088/activity/get_all_activities.do";

    static class Gist{
        Map<String, GistFile> files;
    }
    static class GistFile{
        String context;
    }

    static class Friends{
        Map<String, Friend> friendMap;
    }
    static class Friend{
        Gist gist;
    }

    public DoOkHttp() {
        okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
                        cookieStore.put(httpUrl.host(), list);
                    }

                    @NotNull
                    @Override
                    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                        List<Cookie> cookies = cookieStore.get(httpUrl.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();
        parse = new JsonParser();
    }

    public String login(String userName, String password){
        String status = "1";
        try{
            FormBody body = new FormBody.Builder()
                    .add("username", userName)
                    .add("password", password)
                    .build();
            Request request = new Request.Builder()
                    .url(login_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                Log.i("status", jsonObject.get("status").getAsString());
                status = jsonObject.get("status").getAsString();
            }
        }catch(IOException e){
            Log.i("okhttp: ","网络连接失败");
        }
        return status;
    }

    public JsonArray getUserInfo(){
        JsonArray users = null;
        try{
            FormBody body = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(userInfo_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                Log.i("user",result);
                users = jsonObject.get("status").equals("0") ? jsonObject.getAsJsonArray("data") : null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return users;
    }

    public String regiest(String username, String password){
        String status = "1";
        try{
            FormBody body = new FormBody.Builder()
                    .add("userName", username)
                    .add("password", password)
                    .build();
            Request request = new Request.Builder()
                    .url(reg_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                Log.i("response:",result);
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                status = jsonObject.get("status").getAsString();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public String logout(){
        String status = "1";
        try{
            FormBody body = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(logout_url)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                status = jsonObject.get("status").getAsString();
            }
            }catch (Exception e){
                e.printStackTrace();
            }
            return status;
    }

    public JsonObject modify(String userName, String nickName, File file){
        JsonObject user = null;
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("userName", userName)
                    .addFormDataPart("nickname", nickName)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(MediaType.parse("multipart/form-data"), file))
                    .build();
            Request request = new Request.Builder()
                    .url(modify_url)
                    .post(requestBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                user = jsonObject.get("status").equals("0") ? jsonObject.getAsJsonObject("data") : null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public JsonObject findFriend(String friendName){
        JsonObject friend = null;
        try{
            FormBody body = new FormBody.Builder()
                    .add("findname", friendName)
                    .build();
            Request request = new Request.Builder()
                    .url(findFriend_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                friend = jsonObject.get("status").equals("0")? jsonObject.getAsJsonObject("data"): null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return friend;
    }

    public JsonArray showFriends(){
        JsonArray friends = null;
        try{
            FormBody body = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(friends_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                friends = jsonObject.get("status").equals("0")?
                        jsonObject.getAsJsonArray("data") : null;
                Log.i("friends", friends.getAsString());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return friends;
    }

    public String addFriend(String reciver){
        String status = "1";
        try{
            FormBody body = new FormBody.Builder()
                    .add("reciver", reciver)
                    .build();
            Request request = new Request.Builder()
                    .url(addFriend_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                status = jsonObject.get("status").getAsString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return status;
    }

    public String comfirmFriends(String applicant, String status){
        String res_status = "1";
        try{
            FormBody body = new FormBody.Builder()
                    .add("applicant", applicant)
                    .add("status", status)
                    .build();
            Request request = new Request.Builder()
                    .url(comfirm_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                res_status = jsonObject.get("status").getAsString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return res_status;
    }

    public JsonArray showRequest(){
        JsonArray requests = null;
        try{
            FormBody body = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(showRequest_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                requests = jsonObject.get("status").equals("0")?
                        jsonObject.getAsJsonArray("data") : null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return requests;
    }

    public JsonArray getActivityByUser(String username, String limit, String offset){
        JsonArray activities = null;
        try{
            FormBody body = new FormBody.Builder()
                    .add("username", username)
                    .add("limit", limit)
                    .add("offset", offset)
                    .build();
            Request request = new Request.Builder()
                    .url(getActivityByUser_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                activities = jsonObject.get("status").equals("0") ?
                        jsonObject.getAsJsonArray("data"):null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return activities;
    }

    public JsonObject createImage(File file, String activity_id, String order){
        JsonObject image = null;
        try{
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(MediaType.parse("multipart/form-data"), file))
                    .addFormDataPart("activityId", activity_id)
                    .addFormDataPart("order", order)
                    .build();
            Request request = new Request.Builder()
                    .url(createImage_url)
                    .post(requestBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                image = jsonObject.get("status").equals("0")?
                        jsonObject.getAsJsonObject("data") : null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return image;
    }

    public JsonObject modifyActivity(String id, String content){
        JsonObject activity = null;
        try{
            FormBody body = new FormBody.Builder()
                    .add("id", id)
                    .add("content", content)
                    .build();
            Request request = new Request.Builder()
                    .url(modifyActivity_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                activity = jsonObject.get("status").equals("0")?
                        jsonObject.getAsJsonObject("data"):null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return activity;
    }

    public JsonObject createActivity(String content, double x, double y, String locationName){
        JsonObject activity = null;
        try{
            FormBody body = new FormBody.Builder()
                    .add("content", content)
                    .add("location", String.valueOf(x)+","+String.valueOf(y))
                    .add("locationName", locationName)
                    .build();
            Request request = new Request.Builder()
                    .url(createActivity_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                activity = jsonObject.get("status").equals("0") ?
                        jsonObject.getAsJsonObject("data") : null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return activity;
    }

    public String deleteActivity(String id){
        String status = "1";
        try{
            FormBody body = new FormBody.Builder()
                    .add("activityId", id)
                    .build();
            Request request = new Request.Builder()
                    .url(deleteActivity_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                status = jsonObject.get("status").getAsString();

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return status;
    }

    public String deleteImage(String imageId, String activityId){
        String status = "1";
        try{
            FormBody body = new FormBody.Builder()
                    .add("imageId", imageId)
                    .add("activityId", activityId)
                    .build();
            Request request = new Request.Builder()
                    .url(deleteImage_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                status = jsonObject.get("status").getAsString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return status;
    }

    public JsonObject getActivityInfo(String id){
        JsonObject activity = null;
        try {
            FormBody body = new FormBody.Builder()
                    .add("activityId", id)
                    .build();
            Request request = new Request.Builder()
                    .url(getActivityInfo_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                activity = jsonObject.get("status").getAsInt() == 0 ? jsonObject.getAsJsonObject("data") : null ;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return activity;
    }

    public JsonArray getActivityByLocation(String location){
        JsonArray activities = null;
        try{
            FormBody body = new FormBody.Builder()
                    .add("locationName",location)
                    .build();
            Request request = new Request.Builder()
                    .url(getActivityByLoc_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                activities = jsonObject.get("status").getAsInt() == 0 ? jsonObject.getAsJsonArray("data") : null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return activities;
    }

    public JsonArray getImageByActivity(String activityId){
        JsonArray images = null;
        try{
            FormBody body = new FormBody.Builder()
                    .add("activutyId", activityId)
                    .build();
            Request request = new Request.Builder()
                    .url(getImageByActivity_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                images = jsonObject.get("status").getAsInt() == 0 ? jsonObject.getAsJsonArray("data") : null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return images;
    }

    public JsonArray getCommit(String activityId, String offset, String limit){
        JsonArray commitList = null;
        try{
            FormBody body = new FormBody.Builder()
                    .add("activity", activityId)
                    .add("offset", offset)
                    .add("limit", limit)
                    .build();
            Request request = new Request.Builder()
                    .url(getCommit_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                commitList = jsonObject.get("status").getAsInt() == 0 ? jsonObject.getAsJsonArray("data") : null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return commitList;
    }

    public String addCommit(String text, String activityId){
        String status = "1";
        try{
            FormBody body = new FormBody.Builder()
                    .add("text", text)
                    .add("activity", activityId)
                    .build();
            Request request = new Request.Builder()
                    .url(addCommit_url)
                    .post(body)
                    .build();
            Response response =okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                status = jsonObject.get("status").getAsString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return status;
    }

    public String like(String activityId){
        String status = "1";
        try{
            FormBody body = new FormBody.Builder()
                    .add("actity", activityId)
                    .build();
            Request request = new Request.Builder()
                    .url(like_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                status = jsonObject.get("status").getAsString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return status;
    }

    public String unlike(String activityId){
        String status = "1";
        try{
            FormBody body = new FormBody.Builder()
                    .add("actity", activityId)
                    .build();
            Request request = new Request.Builder()
                    .url(unlike_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                status = jsonObject.get("status").getAsString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return status;
    }

    public JsonArray getAllActivity(String limit, String offset){
        JsonArray activities = null;
        try{
            FormBody body = new FormBody.Builder()
                    .add("limit", limit)
                    .add("offset", offset)
                    .build();
            Request request = new Request.Builder()
                    .url(getAllAct_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                activities = jsonObject.getAsJsonArray("data");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return activities;
    }




}
