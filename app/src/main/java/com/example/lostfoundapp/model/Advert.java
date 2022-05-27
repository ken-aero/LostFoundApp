package com.example.lostfoundapp.model;

public class Advert {

    private int advert_id;
    private int post_type;
    private String name;
    private String phone;
    private String description;
    private String date;
    private String location;
    private String location_lat;
    private String location_lng;

    public Advert(int post_type, String name, String phone, String description,
                  String date, String location, String location_lat, String location_lng) {
        this.post_type = post_type;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.location_lat = location_lat;
        this.location_lng = location_lng;
    }

    public Advert() {
    }

    public String getLocation_lat() {
        return location_lat;
    }

    public void setLocation_lat(String location_lat) {
        this.location_lat = location_lat;
    }

    public String getLocation_lng() {
        return location_lng;
    }

    public void setLocation_lng(String location_lng) {
        this.location_lng = location_lng;
    }

    public int getAdvert_id() {
        return advert_id;
    }

    public int getPost_Type() {
        return post_type;
    }

    public void setPost_Type(int post_type) {
        this.post_type = post_type;
    }

    public void setAdvert_id(int advert_id) {
        this.advert_id = advert_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
