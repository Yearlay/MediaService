package com.amt.bean;

/**
 * Created by archermind on 2018/8/9.
 */

public class AudioBean extends MediaBean {
    private String title;
    private String titlePY;
    private String artist;
    private String artistPY;
    private String album;
    private String albumPY;
    private String composer;
    private String genre;
    private int duration;
    private String thumbnailPath;
    private int playTime;

    public AudioBean() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitlePY() {
        return titlePY;
    }

    public void setTitlePY(String titlePY) {
        this.titlePY = titlePY;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtistPY() {
        return artistPY;
    }

    public void setArtistPY(String artistPY) {
        this.artistPY = artistPY;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumPY() {
        return albumPY;
    }

    public void setAlbumPY(String albumPY) {
        this.albumPY = albumPY;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }
}
