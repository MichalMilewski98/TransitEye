package com.transit.transiteye.model;

public class CachedVehicle {
    private VehiclePosition vehicle;
    private long lastSeen;

    public CachedVehicle(VehiclePosition vehicle) {
        this.vehicle = vehicle;
        this.lastSeen = System.currentTimeMillis();
    }

    public VehiclePosition getVehicle() {
        return vehicle;
    }

    public long getLastSeen() {
        return lastSeen;
    }
}