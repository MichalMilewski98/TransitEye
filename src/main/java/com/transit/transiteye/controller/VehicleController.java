package com.transit.transiteye.controller;

import com.transit.transiteye.model.VehiclePosition;
import com.transit.transiteye.service.VehicleService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
@RestController
public class VehicleController
{

private final VehicleService vehicleService;

public VehicleController(VehicleService vehicleService) {
    this.vehicleService = vehicleService;
}

@GetMapping("/vehicles")
public List<VehiclePosition> vehicles() {
    return vehicleService.getVehicles();
}
}
