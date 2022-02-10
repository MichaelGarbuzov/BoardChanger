package com.example.boardchanger.model.users;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class User {
    final public static String COLLECTION_NAME = "users";
    @PrimaryKey
    @NonNull
    String email;
    String name;
    String imageUrl;
    String password;
    Long updateDate = new Long(0);
    //Board[] posts;
    public User(String email, String name, String password){
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public User(){};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("email", email);
        json.put("name", name);
        json.put("password", password);
        json.put("updateDate", FieldValue.serverTimestamp());
        json.put("imageUrl", imageUrl);
        return json;
    }

    public static User create(Map<String, Object> json) {
        String email = (String) json.get("email");
        String name = (String) json.get("name");
        String password = (String) json.get("password");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();
        String imageUrl = (String)json.get("imageUrl");
        User user = new User(email, name, password);
        user.setUpdateDate(updateDate);
        user.setImageUrl(imageUrl);
        return user;
    }

    private void setUpdateDate(Long updateDate) { this.updateDate = updateDate; }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setImageUrl(String url) {
        imageUrl = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
