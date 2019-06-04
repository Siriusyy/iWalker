package com.yang.iwalker.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yang.iwalker.CommentActivity;
import com.yang.iwalker.R;

import java.util.List;
import java.util.Map;

public class HomeAdapter extends RecyclerView.Adapter <HomeAdapter.ViewHolder> {

    List<Map<String, Object>> datas;
    Context context;

    public HomeAdapter(List<Map<String, Object>> datas, Context context) {
        this.datas = datas;
        this.context = context;
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
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder viewHolder, int i) {
        //viewHolder.friendID.setText(datas.get(i).toString());
        viewHolder.dynamicID = datas.get(i).get("dynamicID").toString();
        viewHolder.friendID.setText(datas.get(i).get("friendID").toString());
        viewHolder.content.setText(datas.get(i).get("content").toString());
        viewHolder.text_date.setText(datas.get(i).get("textdate").toString());
        viewHolder.text_like.setText(datas.get(i).get("textlike").toString());
        viewHolder.text_location.setText(datas.get(i).get("textlocation").toString());
        if(datas.get(i).get("radiolike").equals("1")){
            viewHolder.radio_like.setChecked(true);
        }
        //viewHolder.image.setImageBitmap();

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


}
