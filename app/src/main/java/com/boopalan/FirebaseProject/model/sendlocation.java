package com.boopalan.FirebaseProject.model;

/**
 * Created by Boopalan on 06-Mar-2017.
 */

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class sendlocation {

    public String uid;
    public String author;
    public String lat;
    public String lon;

    private String pushId;

    public Map<String, Boolean> stars = new HashMap<>();

    public sendlocation() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }
    public sendlocation(String uid, String author,String lat1,String lon1){
        this.uid = uid;
        this.author = author;
        this.lat = lat1;
        this.lon = lon1;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("Latitude", lat);
        result.put("Longitude",lon);

        return result;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }





    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public Map<String, Boolean> getStars() {
        return stars;
    }

    public void setStars(Map<String, Boolean> stars) {
        this.stars = stars;
    }
}

