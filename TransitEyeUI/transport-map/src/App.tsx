import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import { useEffect, useState } from 'react';

function App() {
  const [vehicles, setVehicles] = useState([]);

  useEffect(() => {
    const fetchVehicles = async () => {
      const res = await fetch('http://localhost:8080/api/vehicles');
      const data = await res.json();
      console.log('VEHICLES', data);
      setVehicles(data);
    }

    fetchVehicles();
    const interval = setInterval(fetchVehicles, 5000);
    return () => clearInterval(interval);
  }, []);

  return (
    <MapContainer
      center={[54.3520, 18.6466]}
      zoom={13}
      style={{ height: '100vh', width: '100%' }}
    >
      <TileLayer
        attribution="© OpenStreetMap"
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />

      {vehicles.map(v => (
        <Marker key={v.vehicleId} position={[v.latitude, v.longitude]}>
          <Popup>
            Linia: {v.routeId}<br />
            ID: {v.vehicleId}
          </Popup>
        </Marker>
      ))}
    </MapContainer>
  );
}

export default App;
