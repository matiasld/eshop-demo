package com.example.eshop.model;

import android.content.ContentValues;

import com.example.eshop.database.MySQLiteDb;

public class Brand {

    private String brand;       //Marca
    private String url;         //Url

    public Brand(String brand, String url) {
        this.brand = brand;
        this.url = url;
    }

    public String getBrand() {
        return brand;
    }

    public String getUrl() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDb.DbEntry.BRAND, brand);
        values.put(MySQLiteDb.DbEntry.URL, url);

        return values;
    }
}
