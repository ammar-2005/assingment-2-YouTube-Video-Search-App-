package com.example.youtubevideosearchapp;

public class VideoModel {
    private String title;
    private String description;
    private String channelTitle;
    private String publishTime;
    private String thumbnailUrl;

    public VideoModel(String title, String description, String channelTitle, String publishTime, String thumbnailUrl) {
        this.title = title;
        this.description = description;
        this.channelTitle = channelTitle;
        this.publishTime = publishTime;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getChannelTitle() { return channelTitle; }
    public String getPublishTime() { return publishTime; }
    public String getThumbnailUrl() { return thumbnailUrl; }
}