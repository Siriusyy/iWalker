package com.yang.iwalker;

import com.baidu.mapapi.model.LatLng;

public class LocationInfo {
    private String name;
    private String city;
    private String Address;
    private LatLng position;

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public void setName(String name) {
        this.name = name;
    }
}
