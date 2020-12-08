package com.example.exp_4_musicplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import com.example.exp_4_musicplayer.data.Music;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    public static List<Music> musicList = new ArrayList<>();

    public static Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View musicView;     //保存子项最外层布局实例
        TextView musicName;
        TextView musicAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            musicView = itemView;
            this.musicName = itemView.findViewById(R.id.music_name);
            this.musicAuthor = itemView.findViewById(R.id.music_author);
        }
    }

    //构造函数，传入数据
    public MusicAdapter(List<Music> musicList) {
        this.musicList = musicList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //创建ViewHolder实例
        if (mContext == null){
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_item, viewGroup, false);
        //传入music_item布局
        final ViewHolder holder = new ViewHolder(view);
        // item的点击监听器
        holder.musicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Music music = musicList.get(position);
                Intent intent = new Intent(mContext, playerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("musicPosition", position);
                bundle.putString("musicTitle", music.getTitle());
                intent.putExtras(bundle);
                mContext.startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //为RecyclerView子项数据进行赋值
        Music music = musicList.get(position);
        holder.musicName.setText(music.getTitle());
        holder.musicAuthor.setText(music.getAritist());
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

}

