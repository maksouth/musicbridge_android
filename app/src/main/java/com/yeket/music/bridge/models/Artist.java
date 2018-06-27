package com.yeket.music.bridge.models;

public class Artist {

    private String spotifyId;
    private String name;
    private String imageUrl;

    public Artist(String spotifyId, String name, String image_url) {
        this.spotifyId = spotifyId;
        this.name = name;
        this.imageUrl = image_url;
    }

    public Artist(){}

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String image_url) {
        this.imageUrl = image_url;
    }
}
