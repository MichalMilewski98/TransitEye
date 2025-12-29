package com.transit.transiteye.controller;

import com.transit.transiteye.model.GpsHealthStatus;
import com.transit.transiteye.service.GpsRealtimeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    private final GpsRealtimeService gpsService;

    public HealthController(GpsRealtimeService gpsService) {
        this.gpsService = gpsService;
    }

    @GetMapping("/gps")
    public GpsHealthStatus gpsHealth() {
        return new GpsHealthStatus(
                gpsService.isFeedAvailable(),
                gpsService.getLastFetchTime(),
                gpsService.getActiveVehicleCount()
        );
    }
}