package com.yang.iwalker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yang.iwalker.NetWork.DoOkHttp;
import com.yang.iwalker.R;

import java.util.List;

public class AddFriendAdapter extends RecyclerView.Adapter <AddFriendAdapter.ViewHolder> {
    JsonArray datas;
    List<Bitmap> bitmapList;
    DoOkHttp client;
    Context context;

    public AddFriendAdapter(JsonArray datas, List<Bitmap> bitmapList, DoOkHttp client, Context context) {
        this.datas = datas;
        this.bitmapList = bitmapList;
        this.client = client;
        this.context = context;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        String ID;
        TextView friendname;
        TextView info;
        ImageView image;
        Button submit;

        View view;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.view = itemView;
            friendname = itemView.findViewById(R.id.fri_id);
            info = itemView.findViewById(R.id.fri_info);
            image = itemView.findViewById(R.id.img_fri);
            submit = itemView.findViewById(R.id.submit);
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
    public AddFriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_friend_add, viewGroup, false);
        final AddFriendAdapter.ViewHolder holder = new AddFriendAdapter.ViewHolder(view);

        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String s = client.addFriend(holder.ID);
                        if(s.equals("0")){
                            Message m = new Message();
                            m.what = 1;
                            mHandler.sendMessage(m);
                        }else{
                            Message m = new Message();
                            m.what = 2;
                            mHandler.sendMessage(m);
                        }
                    }
                }).start();
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
    public void onBindViewHolder(@NonNull AddFriendAdapter.ViewHolder viewHolder, int i) {
        JsonObject object = datas.get(i).getAsJsonObject();
        viewHolder.ID = object.get("userName").getAsString();
        if(!object.get("nickname").isJsonNull()){
            viewHolder.friendname.setText(object.get("nickname").getAsString()+"("+object.get("userName").getAsString()+")");
        }else{
            viewHolder.friendname.setText("("+object.get("userName").getAsString()+")");
        }

        if(!object.get("desc").isJsonNull())
            viewHolder.info.setText(object.get("desc").getAsString());
        else
            viewHolder.info.setText("");
        if(bitmapList.get(i)!=null){
            viewHolder.image.setImageBitmap(bitmapList.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(context, "请求成功", Toast.LENGTH_LONG);
                    break;
                case 2:
                    Toast.makeText(context, "请求失败", Toast.LENGTH_LONG);
                    break;
            }
            super.handleMessage(msg);
        }
    };

}
