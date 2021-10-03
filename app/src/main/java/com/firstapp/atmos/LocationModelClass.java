package com.firstapp.atmos;

public class LocationModelClass {

    private String latitude;
    private String longitude;

    public LocationModelClass(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationModelClass() {
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
