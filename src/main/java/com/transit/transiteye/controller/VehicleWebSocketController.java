package com.transit.transiteye.controller;

import com.transit.transiteye.model.VehiclePosition;
import com.transit.transiteye.service.GtfsRealtimeService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@Controller
public class VehicleWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final GtfsRealtimeService gtfsRealtimeService;


    public VehicleWebSocketController(SimpMessagingTemplate messagingTemplate, GtfsRealtimeService gtfsRealtimeService) {
        this.messagingTemplate = messagingTemplate;
        this.gtfsRealtimeService = gtfsRealtimeService;
    }

    @Scheduled(fixedDelay = 10000)
    public void sendVehicles() {
        System.out.println("Sending vehicle positions via WebSocket");
        List<VehiclePosition> vehicles = gtfsRealtimeService.fetchRealtimePositions();
        messagingTemplate.convertAndSend("/topic/vehicles", vehicles);
    }
}
