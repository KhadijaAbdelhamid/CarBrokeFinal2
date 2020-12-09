package com.kad.carbrokefinal;

public class Provider {
    String id,name,phone,distance;
    Double lat,lon;

    public Provider() { }

    public Provider(String id, String name, String phone, String distance, Double lat, Double lon) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.distance = distance;
        this.lat = lat;
        this.lon = lon;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }


    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
