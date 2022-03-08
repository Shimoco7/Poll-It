package com.example.appproject.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

@Entity
public class User {

    @PrimaryKey
    @NonNull
    String uid;
    String fullName;
    String location;
    String profilePicUrl;

    public User() { }

    @Ignore
    public User(@NonNull String uid) {
        this.uid = uid;
        //TODO - delete default values
        setFullName("Israel Israeli");
        setLocation("Akko");
    }

    /**
     * Factory
     *
     */
    public static User create(Map<String, Object> data) {
        String uid = (String) data.get("uid");
        String fullName = (String) data.get("full_name");
        String location = (String) data.get("location");

        User user = new User(uid);
        user.setFullName(fullName);
        user.setLocation(location);
        return user;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public Map<String,Object> toJson(){
        Map<String,Object> json = new HashMap<>();
        json.put("uid",uid);
        json.put("full_name",fullName);
        json.put("location",location);
        return json;
    }

}
