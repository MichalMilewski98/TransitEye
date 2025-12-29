package com.transit.transiteye.model;

public record GpsHealthStatus(
        boolean feedAvailable,
        long lastFetchEpochMs,
        int activeVehicles
) {}