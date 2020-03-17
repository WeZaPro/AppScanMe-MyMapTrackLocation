package com.example.mymaptracklocation;

public class ModelAddress {
    String UID;
    double lat,lon;
    String address,city,tambon,postalCode,knownName,country,amphoe;

    public ModelAddress() {
    }

    public ModelAddress(String UID, double lat, double lon, String address, String city, String tambon, String postalCode, String country, String amphoe, String knownName) {
        this.UID = UID;
        this.lat = lat;
        this.lon = lon;
        this.address = address;
        this.city = city;
        this.tambon = tambon;
        this.postalCode = postalCode;
        this.knownName = knownName;
        this.country = country;
        this.amphoe = amphoe;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTambon() {
        return tambon;
    }

    public void setTambon(String tambon) {
        this.tambon = tambon;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getKnownName() {
        return knownName;
    }

    public void setKnownName(String knownName) {
        this.knownName = knownName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAmphoe() {
        return amphoe;
    }

    public void setAmphoe(String amphoe) {
        this.amphoe = amphoe;
    }
}
