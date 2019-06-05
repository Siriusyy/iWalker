package com.yang.iwalker.NetWork;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class DoOkHttp {
    private OkHttpClient okHttpClient;
    private JsonParser parse;
    private static HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
    private final String apiIp = "http://192.168.43.173:8088/";

    private final String login_url = apiIp+"user/login.do";
    private final String reg_url = apiIp+"user/register.do";
    private final String logout_url = apiIp+"user/logout.do";
    private final String modify_url = apiIp+"user/modify.do";
    private final String userInfo_url = apiIp+"user/get_user_info.do";
    private final String findFriend_url = apiIp+"user/findfriend.do";
    private final String friends_url = apiIp+"relation/showfriend.do";
    private final String addFriend_url = apiIp+"relation/addfriend.do";
    private final String comfirm_url = apiIp+"relation/confirmfriend.do";
    private final String showRequest_url = apiIp+"relation/showrequest.do";
    private final String createImage_url = apiIp+"image/create.do";
    private final String modifyActivity_url = apiIp+"activity/modify.do";
    private final String createActivity_url = apiIp+"activity/create.do";
    private final String deleteActivity_url = apiIp+"activity/delete.do";
    private final String deleteImage_url = apiIp+"image/delete.do";
    private final String getActivityInfo_url=apiIp+"activity/get_activity_info.do";
    private final String getActivityByUser_url = apiIp+"activity/get_activities_by_user.do";
    private final String getActivityByLoc_url = apiIp+"activity/get_activities_by_location_name.do";
    private final String getImageByActivity_url = apiIp+"image/get_by_activity.do";
    private final String getCommit_url = apiIp+"comment/showcomment.do";
    private final String addCommit_url = apiIp+"comment/addcomment.do";
    private final String unlike_url = apiIp+"like/unlike.do";
    private final String like_url = apiIp+"like/like.do";
    private final String getAllAct_url = apiIp+"activity/get_all_activities.do";
    private final String getUserFriend_url = apiIp+"user/get_user_friend.do";
    private final String getSelfActivity_url = apiIp+"activity/get_activities_by_self.do";


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

    public JsonArray getSelfActivity(String limit, String offset){
        JsonArray activities = null;
        try{
            FormBody body = new FormBody.Builder()
                    .add("limit", limit)
                    .add("offset", offset)
                    .build();
            Request request = new Request.Builder()
                    .url(getSelfActivity_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                activities = jsonObject.get("status").toString().equals("0")?
                        jsonObject.getAsJsonArray("data") : null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return activities;
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

    public JsonObject getUserInfo(){
        JsonObject users = null;
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
                users = jsonObject.get("status").toString().equals("0") ? jsonObject.getAsJsonObject("data") : null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return users;
    }

    public String deleteFriends(String applicant){
        String res_status = "3";
        try{
            FormBody body = new FormBody.Builder()
                    .add("applicant", applicant)
                    .add("status", "3")
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

    public String regiest(String username, String password, String nickname, boolean gender){
        String status = "1";
        try{
            FormBody body = new FormBody.Builder()
                    .add("userName", username)
                    .add("password", password)
                    .add("nickname", nickname)
                    .add("gender", String.valueOf(gender))
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

    public JsonObject modify(String nickName, String desc){
        JsonObject user = null;
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("nickname", nickName)
                    .addFormDataPart("desc", desc)
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
    public JsonObject modifyNickname(String nickName){
        JsonObject user = null;
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("nickname", nickName)
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
    public JsonObject modifyDesc(String desc){
        JsonObject user = null;
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("desc", desc)
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

    public JsonObject modifyGender(String gender){
        JsonObject user = null;
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("gender", gender)
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


    public JsonObject modify(File file){
        JsonObject user = null;
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
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

    public JsonArray findFriend(String friendName){
        JsonArray friends = null;
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
                friends = jsonObject.get("status").toString().equals("0")? jsonObject.getAsJsonArray("data"): null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return friends;
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
                friends = jsonObject.get("status").toString().equals("0")?
                        jsonObject.getAsJsonArray("data") : null;
                //Log.i("friends", friends.getAsString());
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
                    .add("receiver", reciver)
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

    public String comfirmFriends(String applicant){
        String res_status = "1";
        try{
            FormBody body = new FormBody.Builder()
                    .add("applicant", applicant)
                    .add("status", "1")
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
                requests = jsonObject.get("status").toString().equals("0")?
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
                activities = jsonObject.get("status").toString().equals("0") ?
                        jsonObject.getAsJsonArray("data"):null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return activities;
    }
    public JsonObject getUserFriends(String userName){
        JsonObject friend = null;
        try{
            FormBody body = new FormBody.Builder()
                    .add("userName", userName)
                    .build();
            Request request = new Request.Builder()
                    .url(getUserFriend_url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JsonObject jsonObject = (JsonObject) parse.parse(result);
                friend = jsonObject.get("status").toString().equals("0")?
                        jsonObject.getAsJsonObject("data") : null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return friend;
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
                image = jsonObject.get("status").toString().equals("0")?
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
                activity = jsonObject.get("status").toString().equals("0")?
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
                activity = jsonObject.get("status").toString().equals("0") ?
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

    public String rejectFriends(String applicant){
        String res_status = "1";
        try{
            FormBody body = new FormBody.Builder()
                    .add("applicant", applicant)
                    .add("status", "2")
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
                    .add("activity", activityId)
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
                    .add("activity", activityId)
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
