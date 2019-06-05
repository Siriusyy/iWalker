package com.yang.iwalker.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yang.iwalker.CommentActivity;
import com.yang.iwalker.NetWork.DoOkHttp;
import com.yang.iwalker.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter <HomeAdapter.ViewHolder> {

    JsonArray datas;
    Context context;
    List<Bitmap> l;
    DoOkHttp client;

    public HomeAdapter(JsonArray datas, Context context, List<Bitmap> l, DoOkHttp client) {
        this.datas = datas;
        this.context = context;
        this.l = l;
        this.client = client;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        String dynamicID;
        TextView friendID;
        TextView content;
        TextView text_date;
        TextView text_like;
        RadioButton radio_like;
        ImageView radio_comments;
        ImageView radio_share;
        TextView text_location;
        ImageView image;

        View view;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.view = itemView;
            friendID = itemView.findViewById(R.id.fri_id);
            content = itemView.findViewById(R.id.text_content);
            text_date = itemView.findViewById(R.id.text_date);
            text_like = itemView.findViewById(R.id.text_like);
            text_location = itemView.findViewById(R.id.text_location);
            radio_like = itemView.findViewById(R.id.radio_like);
            radio_comments = itemView.findViewById(R.id.ic_comments);
            radio_share = itemView.findViewById(R.id.ic_forward);
            image = itemView.findViewById(R.id.image);
        }
    }

    /**
     * 创建内部类viewholder时调用
     * @param viewGroup
     * @param i
     * @return
     */
    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_home, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        //JsonObject object = datas.get(i).getAsJsonObject();
        holder.radio_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, holder.content.getText());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(intent, "iWalker"));
            }
        });
        holder.radio_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("dynamicID", holder.dynamicID);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        holder.radio_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(holder.radio_like.isChecked()){

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String num;
                            num = client.like(holder.dynamicID);
                            Log.i("status", num);
                        }
                    }).start();
                    String s = holder.text_like.getText().toString();
                    int num = Integer.valueOf(s)+1;
                    holder.text_like.setText(String.valueOf(num));

                }
//            }
        });
        return holder;
    }
    /**
     * 列表中的子项滚动到屏幕中的时候调用
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder viewHolder, int i) {
        //viewHolder.friendID.setText(datas.get(i).toString());
        JsonObject object = datas.get(i).getAsJsonObject();
        viewHolder.dynamicID = object.get("id").getAsString();
        viewHolder.friendID.setText(object.get("userName").getAsString());
        viewHolder.content.setText(object.get("content").getAsString());
        String time = stampToDate(object.get("createTime").getAsString());
        viewHolder.text_date.setText(time);
        viewHolder.text_like.setText(object.get("likeNum").getAsString());
        String li = object.get("locationName").getAsString();
        String[] list = li.split(",");
        String s = list[3].substring(1, list[3].length()-1);
        viewHolder.text_location.setText(s);

        if(object.get("like").getAsString().equals("true")){
            viewHolder.radio_like.setChecked(true);
        }else{
            viewHolder.radio_like.setChecked(false);
        }
        if(l.get(i) != null){
            viewHolder.image.setImageBitmap(l.get(i));
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
