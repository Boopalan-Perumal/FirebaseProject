package com.boopalan.FirebaseProject.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Post {

    public String uid;
    public String author;
    public String event;
    public String date;
    public String venue;
    public String time;
//    String imageUrl;

    private String pushId;

    public Map<String, Boolean> stars = new HashMap<>();

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String author, String event1, String date1,String venue1,String time1) {
        this.uid = uid;
        this.author = author;
        this.event = event1;
        this.date = date1;
        this.venue = venue1;
        this.time = time1;
//        this.imageUrl =imageUrl;

    }

    public String getLargeImageUrl(String imageUrl) {
        String largeImageUrl = imageUrl.substring(0, imageUrl.length() - 6).concat("o.jpg");
        return largeImageUrl;
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

    public String getEvent1() {
        return event;
    }

    public void setEvent1(String event1) {
        this.event = event1;
    }

    public String getDate1() {
        return date;
    }

    public void setDate1(String date1) {
        this.date = date1;
    }

    public String getVenue1() {
        return venue;
    }

    public void setVenue1(String venue1) {
        this.venue = venue1;
    }

    public String getTime1() {
        return time;
    }

    public void setTime1(String time1) {
        this.time = time1;
    }

   /* public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
*/
    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("event", event);
        result.put("date", date);
        result.put("venue", venue);
        result.put("time", time);
//        result.put("imageUrl",imageUrl);


        return result;
    }
    // [END post_to_map]

}
// [END post_class]
