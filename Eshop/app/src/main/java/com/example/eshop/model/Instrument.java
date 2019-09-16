package com.example.eshop.model;

public class Instrument {
    private String available;
    private String brand;
    private String category;
    private String description;
    private String details;
    private String model;
    private String price;
    private String urlimg1;
    private String urlimg2;
    private String urlimg3;
    private String urlyoutube;
    private String views;
    private String instrumentid;

    public Instrument() {

    }

    public Instrument(String available, String brand, String category, String description, String details, String model, String price, String urlimg1, String urlimg2, String urlimg3, String urlyoutube, String views, String instrumentid) {
        this.available = available;
        this.brand = brand;
        this.category = category;
        this.description = description;
        this.details = details;
        this.model = model;
        this.price = price;
        this.urlimg1 = urlimg1;
        this.urlimg2 = urlimg2;
        this.urlimg3 = urlimg3;
        this.urlyoutube = urlyoutube;
        this.views = views;
        this.instrumentid = instrumentid;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrlimg1() {
        return urlimg1;
    }

    public void setUrlimg1(String urlimg1) {
        this.urlimg1 = urlimg1;
    }

    public String getUrlimg2() {
        return urlimg2;
    }

    public void setUrlimg2(String urlimg2) {
        this.urlimg2 = urlimg2;
    }

    public String getUrlimg3() {
        return urlimg3;
    }

    public void setUrlimg3(String urlimg3) {
        this.urlimg3 = urlimg3;
    }

    public String getUrlyoutube() {
        return urlyoutube;
    }

    public void setUrlyoutube(String urlyoutube) {
        this.urlyoutube = urlyoutube;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getInstrumentid() {
        return instrumentid;
    }

    public void setInstrumentid(String instrumentid) {
        this.instrumentid = instrumentid;
    }
}
