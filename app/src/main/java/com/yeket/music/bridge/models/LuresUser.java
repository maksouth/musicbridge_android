package com.yeket.music.bridge.models;

import android.location.Location;

public class LuresUser {
    private String id;
    private String displayName;
    private String imageUri;
    private String gender;
    private long birthdate;
    private String email;
    private String aboutYou;
    private Location location;
    private String spotifyId;
    private String facebookId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(long birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAboutYou() {
        return aboutYou;
    }

    public void setAboutYou(String aboutYou) {
        this.aboutYou = aboutYou;
    }

    public void copyFrom(LuresUser user){
        this.id = user.id;
        this.displayName = user.displayName;
        this.imageUri = user.imageUri;
        this.gender = user.gender;
        this.birthdate = user.birthdate;
        this.email = user.email;
        this.aboutYou = user.aboutYou;
        this.location = user.location;
        this.spotifyId = user.spotifyId;
        this.facebookId = user.facebookId;
    }
}
