import React from 'react';

const NoVehiclesOverlay: React.FC = () => {
  return (
    <div style={overlayStyle}>
      Brak aktywnych pojazdów dla wybranej linii
    </div>
  );
};

const overlayStyle: React.CSSProperties = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  background: 'rgba(255,255,255,0.9)',
  padding: '16px 20px',
  borderRadius: 8,
  zIndex: 1000,
  fontSize: 14
};

export default NoVehiclesOverlay;