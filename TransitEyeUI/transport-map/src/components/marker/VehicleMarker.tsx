import { Marker, Popup } from 'react-leaflet';
import { memo } from 'react';

interface Props {
  vehicleId: string;
  routeId: string;
  lat: number;
  lon: number;
}

const VehicleMarker = memo(
  ({ vehicleId, routeId, lat, lon }: Props) => {
    return (
      <Marker position={[lat, lon]}>
        <Popup>
          Linia: {routeId}<br/>
          ID: {vehicleId}
        </Popup>
      </Marker>
    );
  }
);

export default VehicleMarker;