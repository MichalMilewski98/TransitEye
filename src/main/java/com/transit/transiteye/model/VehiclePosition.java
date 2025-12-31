package com.transit.transiteye.model;

public record VehiclePosition(
        String vehicleId,
        String routeId,
        double latitude,
        double longitude,
        int delay
) {}
