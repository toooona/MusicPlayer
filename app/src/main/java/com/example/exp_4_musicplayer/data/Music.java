package com.example.exp_4_musicplayer.data;

import org.litepal.crud.LitePalSupport;

public class Music extends LitePalSupport {

    private long id;            // 音乐id

    private String title;       // 歌名

    private String aritist;     // 歌手

    private String path;        // 音乐文件路径

    private String fileName;    // 音乐文件名

    private long fileSize;      // 音乐文件大小

    public Music(String title){
        this.title = title;
    }

    public Music() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAritist() {
        return aritist;
    }

    public void setAritist(String aritist) {
        this.aritist = aritist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

}
