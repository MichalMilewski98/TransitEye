import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import { useEffect, useMemo, useState } from 'react';
import { Client } from '@stomp/stompjs';
import VehicleMarker from './marker/VehicleMarker';
import RouteFilter from './filter/RouteFilter';
import NoVehiclesOverlay from './overlay/VehiclesOverlay';
import 'leaflet/dist/leaflet.css';

interface Vehicle {
  vehicleId: string;
  routeId: string;
  latitude: number;
  longitude: number;
}

const TROJMIASTO_BOUNDS: [[number, number], [number, number]] = [
  [54.27, 18.45],
  [54.62, 18.85],
];
const ROUTE_STORAGE_KEY = 'selectedRoute';


function App() {
  const [vehicles, setVehicles] = useState<Vehicle[]>([]);
  const [selectedRoute, setSelectedRoute] = useState<string>(() => {
    return localStorage.getItem(ROUTE_STORAGE_KEY) ?? 'ALL';
  });

  const availableRoutes = useMemo(() => {
    return Array.from(
      new Set(vehicles.map(v => v.routeId))
    ).sort();
  }, [vehicles]);

  const filteredVehicles = useMemo(() => {
    if (selectedRoute === 'ALL') return vehicles;
    return vehicles.filter(v => v.routeId === selectedRoute);
  }, [vehicles, selectedRoute]);


  useEffect(() => {
    const client = new Client({
      brokerURL: 'ws://localhost:8080/ws',
      reconnectDelay: 10000,
      debug: str => console.log(str),
    });
  
    client.onConnect = () => {
      console.log('Connected to WebSocket');
      client.subscribe('/topic/vehicles', message => {
        const data: Vehicle[] = JSON.parse(message.body);
        setVehicles(data);
      });
    };
  
    client.onStompError = frame => {
      console.error('Broker error:', frame.headers['message']);
      console.error('Details:', frame.body);
    };
  
    client.activate();
  
    return () => {
      client.deactivate();
      return; 
    };
  }, []);

  useEffect(() => {
    if (
      selectedRoute !== 'ALL' &&
      !availableRoutes.includes(selectedRoute)
    ) {
      setSelectedRoute('ALL');
    }
  }, [availableRoutes, selectedRoute]);

  useEffect(() => {
    localStorage.setItem(ROUTE_STORAGE_KEY, selectedRoute);
  }, [selectedRoute]);

  return (
    <div style={{ height: '100vh', width: '100vw' }}>

      <RouteFilter
        routes={availableRoutes}
        selected={selectedRoute}
        onChange={setSelectedRoute}
        count={filteredVehicles.length}
      />

      {filteredVehicles.length === 0 && (
        <NoVehiclesOverlay />
      )}

      <MapContainer
        center={[54.3520, 18.6466]}
        zoom={13}
        style={{ height: '100vh', width: '100vw' }}
      >
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />

        {filteredVehicles.map(v => (
          <VehicleMarker
            key={v.vehicleId}
            vehicleId={v.vehicleId}
            routeId={v.routeId}
            lat={v.latitude}
            lon={v.longitude}
          />
        ))}
      </MapContainer>
    </div>
  );
}

export default App;