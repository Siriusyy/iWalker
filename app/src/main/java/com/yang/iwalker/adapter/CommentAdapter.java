package com.yang.iwalker.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yang.iwalker.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    JsonArray datas;
    public CommentAdapter(JsonArray datas) {
        this.datas = datas;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView comments;
        View view;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.view = itemView;
            comments = itemView.findViewById(R.id.comment_info_context);
            name = itemView.findViewById(R.id.comment_info_name);
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
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_comment, viewGroup, false);
        final CommentAdapter.ViewHolder holder = new CommentAdapter.ViewHolder(view);

        return holder;
    }

    /**
     * 列表中的子项滚动到屏幕中的时候调用
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder viewHolder, int i) {
        JsonObject object = datas.get(i).getAsJsonObject();

        if(!object.get("userName").isJsonNull()){
            viewHolder.name.setText(object.get("userName").getAsString()+":");
        }
        viewHolder.comments.setText(object.get("content").getAsString());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

}
