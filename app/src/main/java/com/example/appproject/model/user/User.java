package com.example.appproject.model.user;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class User {

    @PrimaryKey
    @NonNull
    String uid;
    @NonNull
    String email;
    String fullName;
    String location;
    String profilePicUrl;
    Long lastUpdateDate;

    public User() { }

    @Ignore
    public User(@NonNull String uid,@NonNull String email) {
        this.uid = uid;
        this.email = email;
        lastUpdateDate = 0L;
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
        String email = (String) data.get("email");
        String fullName = (String) data.get("full_name");
        String location = (String) data.get("location");
        Timestamp ts = (Timestamp) data.get("update_date");
        assert ts != null;
        Long lastUpdateDate = ts.getSeconds();

        User user = new User(uid,email);
        user.setFullName(fullName);
        user.setLocation(location);
        user.setLastUpdateDate(lastUpdateDate);
        return user;
    }

    public Map<String,Object> toJson(){
        Map<String,Object> json = new HashMap<>();
        json.put("uid",uid);
        json.put("email",email);
        json.put("full_name",fullName);
        json.put("location",location);
        json.put("update_date", FieldValue.serverTimestamp());
        return json;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
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

    public Long getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Long lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
