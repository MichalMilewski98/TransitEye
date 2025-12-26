package com.transit.transiteye.service;

import com.transit.transiteye.model.VehiclePosition;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class VehicleService {

    private final List<VehiclePosition> vehicles =
            new CopyOnWriteArrayList<>();

    @Scheduled(fixedDelay = 5000)
    public void refreshVehicles() {
        // MOCK – na start
        vehicles.clear();
        vehicles.add(new VehiclePosition(
                "BUS_123",
                "169",
                54.3520,
                18.6466
        ));
        vehicles.add(new VehiclePosition(
                "BUS_456",
                "106",
                54.3600,
                18.6300
        ));
        System.out.println("REFRESH VEHICLES");
    }

    public List<VehiclePosition> getVehicles() {
        return vehicles;
    }
}