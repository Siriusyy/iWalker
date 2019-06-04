package com.yang.iwalker.adapter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yang.iwalker.R;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter <FriendsAdapter.ViewHolder> {

    public interface Act1 {
        void click(Bundle bundle);
    }

    public void setAct(Act1 act) {
        this.act = act;
    }

    Act1 act;
    JsonArray datas;
    List<Bitmap> l;

    public FriendsAdapter(JsonArray datas, List<Bitmap> l) {
        this.datas = datas;
        this.l = l;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        String ID;
        TextView friendname;
        TextView info;
        ImageView image;

        View view;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.view = itemView;
            friendname = itemView.findViewById(R.id.fri_id);
            info = itemView.findViewById(R.id.comment_info_name);
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
    public FriendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_friend, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();

                if (act!=null){
                    Bundle bundle = new Bundle();
                    bundle.putString("ID", holder.ID);
                    act.click(bundle);
                }

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
    public void onBindViewHolder(@NonNull FriendsAdapter.ViewHolder viewHolder, int i) {
        JsonObject object = datas.get(i).getAsJsonObject();
        viewHolder.ID = object.get("userName").getAsString();
        if(!object.get("nickname").isJsonNull()){
            viewHolder.friendname.setText(object.get("nickname").getAsString()+"("+viewHolder.ID+")");
        }else{
            viewHolder.friendname.setText("未命名用戶"+"("+viewHolder.ID+")");
        }

        if(!object.get("desc").isJsonNull()){
            viewHolder.info.setText(object.get("desc").getAsString());
        } else{
            viewHolder.info.setText("");
        }

        viewHolder.image.setImageBitmap(l.get(i));

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


}
