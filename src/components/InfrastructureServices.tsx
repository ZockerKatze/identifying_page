import React from 'react';
import UniversalDataProvider from './UniversalDataProvider';

const InfrastructureServices: React.FC = () => (
  <UniversalDataProvider configPath="/config/iconconfig.json">
    {(data) => {
      if (!data || typeof data !== 'object' || !('InfrastructureServices' in data)) {
        return <div>Loading infrastructure services...</div>;
      }
      const services = (data as { InfrastructureServices: Array<{ name: string; url: string; icon: string }> }).InfrastructureServices;
      return (
        <div className="tech-grid">
          {services.map((service) => (
            <div className="tech-item" key={service.name}>
              <img src={`https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/${service.icon}.svg`} alt={service.name} className="tech-icon" />
              <a href={service.url} target="_blank" style={{ color:'inherit', textDecoration:"none" }} rel="noopener noreferrer">{service.name}</a>
            </div>
          ))}
        </div>
      );
    }}
  </UniversalDataProvider>
);

export default InfrastructureServices; 