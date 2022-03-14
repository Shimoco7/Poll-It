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
    String name="";
    String address="";
    String profilePicUrl = "";
    Long lastUpdateDate;

    public User() { }

    @Ignore
    public User(@NonNull String uid,@NonNull String email) {
        this.uid = uid;
        this.email = email;
        lastUpdateDate = 0L;
    }

    /**
     * Factory
     *
     */
    public static User create(Map<String, Object> data) {
        String uid = (String) data.get("uid");
        String email = (String) data.get("email");
        String name = (String) data.get("name");
        String address = (String) data.get("address");
        Timestamp ts = (Timestamp) data.get("update_date");
        assert ts != null;
        Long lastUpdateDate = ts.getSeconds();
        String profilePicUrl = (String) data.get("profile_pic_url");

        User user = new User(uid,email);
        user.setLastUpdateDate(lastUpdateDate);
        user.setProfilePicUrl(profilePicUrl);
        user.setName(name);
        user.setAddress(address);
        return user;
    }

    public Map<String,Object> toJson(){
        Map<String,Object> json = new HashMap<>();
        json.put("uid",uid);
        json.put("email",email);
        json.put("name",name);
        json.put("address",address);
        json.put("update_date", FieldValue.serverTimestamp());
        json.put("profile_pic_url", profilePicUrl);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
