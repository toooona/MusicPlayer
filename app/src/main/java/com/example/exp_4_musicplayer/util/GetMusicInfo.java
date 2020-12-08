package com.example.exp_4_musicplayer.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import static com.example.exp_4_musicplayer.MainActivity.currentMusicList;
import com.example.exp_4_musicplayer.data.Music;


import org.litepal.LitePal;

import java.util.List;
import static com.example.exp_4_musicplayer.MainActivity.musicList;

public class GetMusicInfo {

    // 扫描歌曲
    public static void scanMusic(Context context, List<Music> musicList){
        musicList.clear();
            Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        BaseColumns._ID,
                        MediaStore.Audio.AudioColumns.IS_MUSIC,
                        MediaStore.Audio.AudioColumns.TITLE,
                        MediaStore.Audio.AudioColumns.ARTIST,
                        MediaStore.Audio.AudioColumns.DATA,
                        MediaStore.Audio.AudioColumns.DISPLAY_NAME,
                        MediaStore.Audio.AudioColumns.SIZE,
                        MediaStore.Audio.AudioColumns.DURATION
                }, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor == null){
            return;
        }
        while (cursor.moveToNext()){
            //判断是否为音乐
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            if (isMusic == 1){
                Music music = new Music();
//                long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
                String title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
                String fileName = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME)));
                long fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                music.setTitle(title);
                music.setAritist(artist);
                music.setPath(path);
                music.setFileName(fileName);
                music.setFileSize(fileSize);
                musicList.add(music);
            }
        }
        cursor.close();
    }

    // 获取本地音乐
    public static void getLocalMusic(Context context){
        musicList.clear();
        musicList = LitePal.findAll(Music.class);
        //先查询并获取数据库中的歌曲
        if (musicList.isEmpty()) {
            //若无则重新扫描歌曲
            GetMusicInfo.scanMusic(context, musicList);
            for (Music music : musicList) {
                music.save();
            }
        }
        currentMusicList = musicList;

    }
}
