package com.transit.transiteye.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transit.transiteye.model.CachedVehicle;
import com.transit.transiteye.model.VehiclePosition;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GpsRealtimeService {

    private static final String VEHICLE_POSITIONS_URL =
            "https://ckan2.multimediagdansk.pl/gpsPositions?v=2";

    private static final long FETCH_INTERVAL_MS = 10_000;
    private static final long VEHICLE_TTL_MS = 30_000;
    private int consecutiveFailures = 0;
    private long backoffUntil = 0;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, CachedVehicle> vehicleCache = new ConcurrentHashMap<>();
    private volatile long lastFetchTime = 0;
    private volatile boolean feedAvailable = false;

    public List<VehiclePosition> fetchRealtimePositions() {
        long now = System.currentTimeMillis();

        if (now < backoffUntil) {
            return currentVehiclesSnapshot();
        }

        if (now - lastFetchTime >= FETCH_INTERVAL_MS) {
            fetchAndUpdateCacheWithRetry(now);
            lastFetchTime = now;
        }

        evictStaleVehicles(now);
        return currentVehiclesSnapshot();
    }

    private void fetchAndUpdateCache() throws IOException {
        try (InputStream stream =
                     new URL(VEHICLE_POSITIONS_URL).openStream()) {

            JsonNode root = objectMapper.readTree(stream);
            JsonNode vehicles = root.get("vehicles");

            if (vehicles == null || !vehicles.isArray()) return;

            for (JsonNode node : vehicles) {
                if (node.get("lat").isNull() || node.get("lon").isNull()) continue;

                double lat = node.get("lat").asDouble();
                double lon = node.get("lon").asDouble();

                if (lat == 0 || lon == 0) continue;
                String vehicleId = String.format(
                        "ZG-%s-%s",
                        node.get("routeShortName").asText(),
                        node.get("vehicleCode").asText()
                );

                String routeId = normalizeRouteId(
                        node.get("routeShortName").asText()
                );

                VehiclePosition vp = new VehiclePosition(
                        vehicleId,
                        routeId,
                        lat,
                        lon,
                        node.get("delay").asInt(0)
                );

                vehicleCache.put(
                        vp.vehicleId(),
                        new CachedVehicle(vp)
                );
            }
            feedAvailable = true;

        } catch (Exception e) {
            feedAvailable = false;
            // log ERROR
            throw e;
        }
    }

    public boolean isFeedAvailable() {
        return feedAvailable;
    }

    public long getLastFetchTime() {
        return lastFetchTime;
    }

    public int getActiveVehicleCount() {
        return vehicleCache.size();
    }

    private void fetchAndUpdateCacheWithRetry(long now) {
        try {
            fetchAndUpdateCache();
            consecutiveFailures = 0;
            feedAvailable = true;

        } catch (Exception e) {
            consecutiveFailures++;
            feedAvailable = false;

            long delay = Math.min(
                    60_000,
                    (long) Math.pow(2, consecutiveFailures) * 1000
            );

            backoffUntil = now + delay;

            // log WARN
        }
    }

    private String normalizeRouteId(String raw) {
        if (raw == null) return "UNKNOWN";

        String normalized = raw.trim().toUpperCase();

        if (normalized.isEmpty()) return "UNKNOWN";

        return normalized;
    }

    private void evictStaleVehicles(long now) {
        vehicleCache.entrySet().removeIf(entry ->
                now - entry.getValue().getLastSeen() > VEHICLE_TTL_MS
        );
    }

    private List<VehiclePosition> currentVehiclesSnapshot() {
        return vehicleCache.values().stream()
                .map(CachedVehicle::getVehicle)
                .toList();
    }
}