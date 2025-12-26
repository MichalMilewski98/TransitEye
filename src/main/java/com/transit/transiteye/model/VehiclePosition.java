package com.transit.transiteye.model;

public class VehiclePosition {

    private String vehicleId;
    private String routeId;
    private double latitude;
    private double longitude;

    public VehiclePosition(
            String vehicleId,
            String routeId,
            double latitude,
            double longitude) {
        this.vehicleId = vehicleId;
        this.routeId = routeId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getRouteId() {
        return routeId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
