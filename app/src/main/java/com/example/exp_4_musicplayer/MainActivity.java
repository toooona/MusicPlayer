package com.example.exp_4_musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.exp_4_musicplayer.data.Music;
import com.example.exp_4_musicplayer.util.GetMusicInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

//    private MediaPlayer mediaPlayer = new MediaPlayer();
    public static List<Music> musicList = new ArrayList<>();
    public static List<Music> currentMusicList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMusics();       //初始化音乐列表
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        MusicAdapter adapter = new MusicAdapter(musicList);
        recyclerView.setAdapter(adapter);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            //若未授予权限，请求权限
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

    }

    private void initMusics(){
        GetMusicInfo.getLocalMusic(MainActivity.this);

    }


}