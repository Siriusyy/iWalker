package com.yang.iwalker.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yang.iwalker.NetWork.DoOkHttp;
import com.yang.iwalker.R;

import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter <NotifyAdapter.ViewHolder>  {
    JsonArray datas;
    List<Bitmap> list;
    DoOkHttp client;

    public NotifyAdapter(JsonArray datas, List<Bitmap> list, DoOkHttp client) {
        this.datas = datas;
        this.list = list;
        this.client = client;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        String friend;
        TextView friendName;
        TextView info;
        Button addFriends;
        Button reject;
        ImageView image;


        View view;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.view = itemView;
            friendName = itemView.findViewById(R.id.fri_id);
            info = itemView.findViewById(R.id.fri_info);
            addFriends = itemView.findViewById(R.id.agree);
            reject = itemView.findViewById(R.id.reject);
            image = itemView.findViewById(R.id.img_fri);
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
    public NotifyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_info, viewGroup, false);
        final NotifyAdapter.ViewHolder holder = new NotifyAdapter.ViewHolder(view);
        holder.addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String status = client.comfirmFriends(holder.friend);
                        if(status.equals("0")){

                        }
                    }
                }).start();
                holder.addFriends.setEnabled(false);
                holder.reject.setEnabled(false);
            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String status = client.rejectFriends(holder.friend);
                        if(status.equals("0")){

                        }
                    }
                }).start();
                holder.addFriends.setEnabled(false);
                holder.reject.setEnabled(false);
            }
        });
        return holder;
    }

    /**
     * 列表中的子项滚动到屏幕中的时候调用
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull NotifyAdapter.ViewHolder viewHolder, int i) {
        JsonObject object = datas.get(i).getAsJsonObject();
        viewHolder.friend = object.get("userName").getAsString();
        if(!object.get("nickname").isJsonNull()){
            viewHolder.friendName.setText(object.get("nickname").getAsString()+"("+viewHolder.friend+")");
        }else{
            viewHolder.friendName.setText("未命名用戶"+"("+viewHolder.friend+")");
        }

        if(!object.get("desc").isJsonNull()){
            viewHolder.info.setText(object.get("desc").getAsString());
        } else{
            viewHolder.info.setText("");
        }

        viewHolder.image.setImageBitmap(list.get(i));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

}
