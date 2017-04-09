package com.boopalan.FirebaseProject.model;

/**
 * Created by Boopalan on 28-Mar-2017.
 */

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class photo {

    public String uid;
    public String author;

    String imageUrl;

    private String pushId;

    public Map<String, Boolean> stars = new HashMap<>();

    public photo() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public photo(String uid, String author, String imageUrl) {
        this.uid = uid;
        this.author = author;
        this.imageUrl =imageUrl;

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



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);

        result.put("imageUrl",imageUrl);


        return result;
    }
    // [END post_to_map]

}
// [END post_class]
