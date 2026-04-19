package com.erc.canopysentinalg.model;

public class DeforestationAlert {
    private String region;
    private double area;
    private String timestamp;
    private double latitude;
    private double longitude;
    private double ndviValue;

    public DeforestationAlert(String region, double area, String timestamp, double latitude, double longitude, double ndviValue) {
        this.region = region;
        this.area = area;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ndviValue = ndviValue;
    }

    // Getters
    public String getRegion() { return region; }
    public double getArea() { return area; }
    public String getTimestamp() { return timestamp; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public double getNdviValue() { return ndviValue; }

    // Notification message format
    public String getNotificationMessage() {
        return String.format("🚨 Deforestation Detected in %s! Estimated loss: %.1f ha on %s", 
            region, area, timestamp);
    }
} 