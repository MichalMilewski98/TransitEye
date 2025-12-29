package com.transit.transiteye.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transit.transiteye.model.VehiclePosition;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class GtfsRealtimeService {

    private static final String VEHICLE_POSITIONS_URL = "https://ckan2.multimediagdansk.pl/gpsPositions?v=2";

    public List<VehiclePosition> fetchRealtimePositions() {
        try {
            URL url = new URL(VEHICLE_POSITIONS_URL);
            InputStream stream = url.openStream();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(stream);

            List<VehiclePosition> vehicles = new ArrayList<>();
            JsonNode vehiclesNode = rootNode.get("vehicles");

            if (vehiclesNode != null && vehiclesNode.isArray()) {
                for (JsonNode vehicleNode : vehiclesNode) {
                    VehiclePosition vehicle = new VehiclePosition(
                            vehicleNode.get("vehicleCode").asText(),
                            vehicleNode.get("routeShortName").asText(),
                            vehicleNode.get("lat").asDouble(),
                            vehicleNode.get("lon").asDouble(),
                            vehicleNode.get("delay").asInt()
                    );
                    vehicles.add(vehicle);
                }
            }
            return vehicles;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch vehicle positions", e);
        }
    }
}