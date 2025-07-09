import React from 'react';

const InfrastructureSection: React.FC = () => (
  <section>
    <h2 className="section-title">
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
        <rect x="2" y="2" width="20" height="8" rx="2" ry="2"></rect>
        <rect x="2" y="14" width="20" height="8" rx="2" ry="2"></rect>
        <line x1="6" y1="6" x2="6.01" y2="6"></line>
        <line x1="6" y1="18" x2="6.01" y2="18"></line>
      </svg>
      ./infrastructure.cfg
    </h2>
    <div className="section-content">
      <div className="subsection">
        <h3 className="subsection-title">HomeLab Nodes:</h3>
        <ul>
          <li>Intel Core I7-4770k</li>
          <li>9TB HDD && SSD (7TB HDD / 2TB SSD)</li>
        </ul>
      </div>
      <div className="subsection">
        <h3 className="subsection-title">Services</h3>
        <div className="tech-grid">
          <div className="tech-item">API</div>
        </div>
      </div>
    </div>
  </section>
);

export default InfrastructureSection; 