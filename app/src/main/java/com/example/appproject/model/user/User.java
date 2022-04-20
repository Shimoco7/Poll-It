package com.example.appproject.model.user;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

@Entity
public class User {

    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    String uid;
    @NonNull
    String email;
    String name="";
    String address="";
    String gender="";
    String profilePicUrl="";
    Long lastUpdateDate; //TODO - update last updated by new field on mongo (parse to date and then to Long)

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
    //TODO - remove
    public static User create(Map<String, Object> data) {
        String uid = (String) data.get("uid");
        String email = (String) data.get("email");
        String name = (String) data.get("name");
        String gender = (String) data.get("gender");
        String address = (String) data.get("address");
        Timestamp ts = (Timestamp) data.get("update_date");
        String profilePicUrl = (String) data.get("profile_pic_url");

        assert ts != null;
        assert uid != null;
        assert email != null;
        Long lastUpdateDate = ts.getSeconds();

        User user = new User(uid,email);
        user.setLastUpdateDate(lastUpdateDate);
        user.setName(name);
        user.setAddress(address);
        if(profilePicUrl != null){
            user.setProfilePicUrl(profilePicUrl);
        }
        if(gender != null){
            user.setGender(gender);
        }
        return user;
    }

    //TODO-remove
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
