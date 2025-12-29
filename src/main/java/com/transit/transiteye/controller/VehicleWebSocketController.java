package com.transit.transiteye.controller;

import com.transit.transiteye.model.VehiclePosition;
import com.transit.transiteye.service.GpsRealtimeService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;


@Controller
public class VehicleWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final GpsRealtimeService gpsRealtimeService;


    public VehicleWebSocketController(SimpMessagingTemplate messagingTemplate, GpsRealtimeService gpsRealtimeService) {
        this.messagingTemplate = messagingTemplate;
        this.gpsRealtimeService = gpsRealtimeService;
    }

    @Scheduled(fixedDelay = 3000)
    public void sendVehiclesData() {
        System.out.println("Sending vehicle positions via WebSocket");
        List<VehiclePosition> vehicles = gpsRealtimeService.fetchRealtimePositions();
        messagingTemplate.convertAndSend("/topic/vehicles", vehicles);
    }
}
