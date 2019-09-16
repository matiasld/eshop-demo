package com.example.eshop.model;

public class Category {
    private String imageId;
    private String title;


    public Category (String imageId, String title) {
        this.imageId = imageId;     //URL de la img
        this.title = title;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
