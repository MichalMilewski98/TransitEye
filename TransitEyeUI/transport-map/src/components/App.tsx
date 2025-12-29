import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';
import L from 'leaflet';
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

function App() {
  const [vehicles, setVehicles] = useState<Vehicle[]>([]);
  const [selectedRoute, setSelectedRoute] = useState<string>('ALL');

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
      return; // Explicitly return void
    };
  }, []);


  const availableRoutes = Array.from(
    new Set(vehicles.map(v => v.routeId))
  ).sort();

  const filteredVehicles =
  selectedRoute === 'ALL'
    ? vehicles
    : vehicles.filter(v => v.routeId === selectedRoute);

  return (

    <div style={{ height: '100vh', width: '100vw' }}>
  <div style={{ position: 'absolute', top: 10, left: 50, zIndex: 1000 }}>
    <select
      value={selectedRoute}
      onChange={e => setSelectedRoute(e.target.value)}
    >
      <option value="ALL">Wszystkie</option>
      {availableRoutes.map(route => (
        <option key={route} value={route}>
          {route}
        </option>
      ))}
    </select>
  </div>
    <MapContainer
      center={[54.3520, 18.6466]}
      zoom={13}
      minZoom={11}
      maxZoom={18}
      maxBounds={TROJMIASTO_BOUNDS}
      maxBoundsViscosity={1.0}
      style={{ height: '100vh', width: '100vw' }}
    >
      <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />

      {filteredVehicles.map(v => {
  const pos = animatedPositions[v.vehicleId];
  if (!pos) return null;

  return (
    <Marker
      key={v.vehicleId}
      position={pos}
    >
      <Popup>
        Linia: {v.routeId}<br />
        ID: {v.vehicleId}
      </Popup>
    </Marker>
  );
})}
    </MapContainer>
    </div>
  );
}

export default App;
