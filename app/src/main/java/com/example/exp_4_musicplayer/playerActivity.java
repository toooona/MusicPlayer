package com.example.exp_4_musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.exp_4_musicplayer.data.Music;
import com.example.exp_4_musicplayer.util.GetMusicInfo;

import static com.example.exp_4_musicplayer.MainActivity.currentMusicList;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class playerActivity extends AppCompatActivity implements View.OnClickListener{

    String musicPosition, musicTitle;
//    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private TextView currentTime, totalTime, current_MusicName;
    private ImageButton play_Song;
    private SeekBar seekBar;        //进度条
    private boolean isSeekBarChanging;
    private SimpleDateFormat format;
    private int currentMusicPostion;     // 当前音乐位置
    private Timer timer;
    private Music music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        play_Song = findViewById(R.id.play);
        Button stop = findViewById(R.id.stop);
        ImageButton nextSong = findViewById(R.id.next_song);
        ImageButton preSong = findViewById(R.id.previous_song);
        ImageButton back = findViewById(R.id.back);
        seekBar = findViewById(R.id.song_progress);
        current_MusicName = findViewById(R.id.current_MusicName);
        currentTime = findViewById(R.id.current_time);
        totalTime = findViewById(R.id.total_time);
        format = new SimpleDateFormat("mm:ss"); //进度条时间格式

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        currentMusicPostion = bundle.getInt("musicPosition", 0);
        musicTitle = bundle.getString("musicTitle");


        play_Song.setOnClickListener(this);
        stop.setOnClickListener(this);
        nextSong.setOnClickListener(this);
        preSong.setOnClickListener(this);
        back.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new MySeekBar());

        initMediaPlayer(currentMusicPostion);

    }

    private void initMediaPlayer(int position){
        try{
            seekBar.setProgress(0);
            mediaPlayer.reset();
            music = currentMusicList.get(position);
            current_MusicName.setText(music.getTitle());
            mediaPlayer.setDataSource(music.getPath()); //指定音频文件路径
            mediaPlayer.prepare();  // 进入准备状态
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax(mediaPlayer.getDuration());
                    totalTime.setText(format.format(mediaPlayer.getDuration()) + "");
                    currentTime.setText("00:00");
                }
            });
            Thread.sleep(1000);
            startMusic();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.back:
                mediaPlayer.stop();
                Intent intent = new Intent(playerActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.play:
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    play_Song.setBackgroundResource(R.drawable.start);
                }
                else if(!mediaPlayer.isPlaying()){
                    startMusic();
                    play_Song.setBackgroundResource(R.drawable.pause);
                }
                break;

            case R.id.next_song:
                setPosition();
                initMediaPlayer(currentMusicPostion);
                break;

            case R.id.previous_song:
                if (currentMusicPostion != 0) {
                    currentMusicPostion--;
                }else {
                    currentMusicPostion = currentMusicList.size()-1;
                }
                initMediaPlayer(currentMusicPostion);
                break;

            case R.id.stop:
                if(mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                break;

            default:
                break;
        }
    }

    // 进度条处理
    public class MySeekBar implements SeekBar.OnSeekBarChangeListener{
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // 播放结束时，自动播放下一首
            if (progress >= mediaPlayer.getDuration()){
                setPosition();
                initMediaPlayer(currentMusicPostion);
            }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // 滚动时停止定时器
            isSeekBarChanging = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // 滚动结束后重新设置进度条的值
            isSeekBarChanging = false;
            mediaPlayer.seekTo(seekBar.getProgress());
        }
    }

    private void startMusic(){
        mediaPlayer.start();    // 开始播放
        mediaPlayer.seekTo(seekBar.getProgress());
        // 监听播放时回调函数
        timer = new Timer();
        timer.schedule(new TimerTask() {
            Runnable updateUI = new Runnable() {
                @Override
                public void run() {
                    currentTime.setText(format.format(mediaPlayer.getCurrentPosition()) + "");
                }
            };
            @Override
            public void run() {
                if(!isSeekBarChanging && mediaPlayer.isPlaying()){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    runOnUiThread(updateUI);
                }
            }
        }, 0, 50);

    }

    // 设置下一首音乐在歌单中的位置
    private void setPosition(){
        if (currentMusicPostion < currentMusicList.size()-1) {
            currentMusicPostion++;
        }else {
            currentMusicPostion = 0;
        }
    }

    protected void onDestroy(){
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

}