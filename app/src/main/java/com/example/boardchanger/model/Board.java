package com.example.boardchanger.model;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Board {
    final public static String COLLECTION_NAME = "boards";
    @PrimaryKey
    @NonNull
    String name="";
    String price="";
    String description="";
//   ImageView image;
    String year="";
    String address="";

   public Board() {}

    public Board(String name, String year, String price, String description,String address) {
        this.name = name;
        this.year = year;
        this.price = price;
       this.description = description;
       this.address = address;
        //this.image = image;
    }

    public static Board create(Map<String, Object> json) {
       String name = (String) json.get("name");
       String year = (String) json.get("year");
       String price = (String) json.get("price");
       String description = (String) json.get("description");
       String address = (String) json.get("address");
       Board board = new Board(name, year, price, description, address);
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


   public void setDescription(String description) {
        this.description = description;
  }

  /* public void setImage(ImageView image) {
      this.image = image;
    }*/

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
        json.put("name", name);
        json.put("year", year);
        json.put("price", price);
        json.put("description", description);
        json.put("address", address);
        return json;
    }

 /*   public ImageView getImage() {
        return image;
    }*/

}
