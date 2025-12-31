import React from 'react';

interface Props {
  routes: string[];
  selected: string;
  onChange: (route: string) => void;
  count: number;
}

const RouteFilter: React.FC<Props> = ({
  routes,
  selected,
  onChange,
  count
}) => {
  return (
    <div style={filterStyle}>
      <div style={{ marginBottom: 6 }}>
        Linia:
      </div>

      <select
        value={selected}
        onChange={e => onChange(e.target.value)}
        style={{ width: '100%' }}
      >
        <option value="ALL">
          Wszystkie ({routes.length})
        </option>

        {routes.map(route => (
          <option key={route} value={route}>
            {route}
          </option>
        ))}
      </select>

      <div style={{ marginTop: 6, fontSize: 12 }}>
        Pojazdy: <b>{count}</b>
      </div>
    </div>
  );
};

const filterStyle: React.CSSProperties = {
  position: 'absolute',
  top: 10,
  left: 10,
  zIndex: 1000,
  background: 'white',
  padding: '10px 12px',
  borderRadius: 6,
  fontSize: 13,
  boxShadow: '0 1px 4px rgba(0,0,0,0.3)'
};

export default RouteFilter;