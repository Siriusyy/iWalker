package com.yang.iwalker.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yang.iwalker.R;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter <FriendsAdapter.ViewHolder> {

    public interface Act1 {
        void click();
    }

    public void setAct(Act1 act) {
        this.act = act;
    }

    Act1 act;

    List datas;



    public FriendsAdapter(List  datas) {
        this.datas = datas;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView friendID;
        TextView info;

        View view;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.view = itemView;
            friendID = itemView.findViewById(R.id.fri_id);
            info = itemView.findViewById(R.id.fri_info);

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
                    act.click();
                }

            }
        });
        holder.friendID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();


                    /*点击list_home中friendID动作*/


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



        viewHolder.friendID.setText(datas.get(i).toString());

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


}
