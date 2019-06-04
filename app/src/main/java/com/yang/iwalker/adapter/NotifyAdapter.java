package com.yang.iwalker.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yang.iwalker.CommentActivity;
import com.yang.iwalker.R;

import java.util.List;
import java.util.Map;

public class NotifyAdapter extends RecyclerView.Adapter <NotifyAdapter.ViewHolder>  {
    List<Map<String, Object>> datas;

    public NotifyAdapter(List<Map<String, Object>> datas) {
        this.datas = datas;
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

            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        //viewHolder.friendID.setText(datas.get(i).toString());
        viewHolder.friend = datas.get(i).get("friendName").toString();
        viewHolder.friendName.setText(datas.get(i).get("friendName").toString());
        viewHolder.info.setText(datas.get(i).get("info").toString());
        //viewHolder.image.setImageBitmap();

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

}
