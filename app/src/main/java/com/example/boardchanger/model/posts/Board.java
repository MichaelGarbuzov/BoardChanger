package com.example.boardchanger.model.posts;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.boardchanger.model.users.User;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
public class Board {
    final public static String COLLECTION_NAME = "boards";
    @PrimaryKey
    @NonNull
    String boardID = UUID.randomUUID().toString();
    String name = "";
    String usersEmail;
    String price = "";
    String description = "";
    String year = "";
    String address = "";
    String imageUrl;
    static String phoneNum;
    Long updateDate = new Long(0);
    Boolean isDeleted = false;

    public Boolean getDeleted() {
        return isDeleted;
    }
    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
    public static String getPhoneNum() {
        return phoneNum;
    }

    public String getId() {
        return boardID;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getUser() {
        return usersEmail;
    }

    public void setUser(String userEmail) {
        this.usersEmail = userEmail;
    }

    public Board() {
    }

    public Board(String name, String year, String price, String description, String address) {
        this.name = name;
        this.year = year;
        this.price = price;
        this.description = description;
        this.address = address;
    }

    public static Board create(Map<String, Object> json) {
        String boardID = (String) json.get("boardID");
        String name = (String) json.get("name");
        String year = (String) json.get("year");
        String price = (String) json.get("price");
        String description = (String) json.get("description");
        String address = (String) json.get("address");
        Timestamp ts = (Timestamp) json.get("updateDate");
        Long updateDate = ts.getSeconds();
        String imageUrl = (String) json.get("imageUrl");
        String phoneNum = (String) json.get("phoneNum");
        Board board = new Board(name, year, price, description, address);
        board.setPhoneNum(phoneNum);
        board.setUpdateDate(updateDate);
        board.setImageUrl(imageUrl);
        board.setUser(json.get("usersEmail").toString());
        board.setBoardID(boardID);
        return board;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setBoardID(String id) {
        this.boardID = id;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("boardID", boardID);
        json.put("name", name);
        json.put("year", year);
        json.put("price", price);
        json.put("description", description);
        json.put("address", address);
        json.put("updateDate", FieldValue.serverTimestamp());
        json.put("imageUrl", imageUrl);
        json.put("phoneNum", phoneNum);
        json.put("usersEmail", usersEmail);
        json.put("isDeleted", isDeleted);
        return json;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

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
