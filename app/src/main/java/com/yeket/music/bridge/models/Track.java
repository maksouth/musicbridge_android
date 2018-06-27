package com.yeket.music.bridge.models;

public class Track {

    private String spotifyId;
    private String name;
    private long duration_ms;
    private String image_url;
    private String preview_url;

    public Track(String spotifyId, String name, long duration_ms, String image_url, String preview_url) {
        this.spotifyId = spotifyId;
        this.name = name;
        this.duration_ms = duration_ms;
        this.image_url = image_url;
        this.preview_url = preview_url;
    }

    public Track(){}

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDurationMs() {
        return duration_ms;
    }

    public void setDurationMs(long duration_ms) {
        this.duration_ms = duration_ms;
    }

    public String getImageUrl() {
        return image_url;
    }

    public void setImageUrl(String image_url) {
        this.image_url = image_url;
    }

    public String getPreviewUrl() {
        return preview_url;
    }

    public void setPreviewUrl(String preview_url) {
        this.preview_url = preview_url;
    }
}
