package com.example.boardchanger.model;

import android.widget.ImageView;

public class Board {

    String price;
     String name;
    String description;
   ImageView image;
    String year;

   public Board() {}

    public Board(String price, String name, String year) {
        this.price = price;
        this.name = name;
       this.description = description;
        this.image = image;
        this.year = year;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

   public void setDescription(String description) {
        this.description = description;
  }

   public void setImage(ImageView image) {
      this.image = image;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ImageView getImage() {
        return image;
    }

    public String getYear() {
        return year;
    }
}
