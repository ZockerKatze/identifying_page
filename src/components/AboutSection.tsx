import React from 'react';

const AboutSection: React.FC = () => (
  <section>
    <h2 className="section-title">
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
        <polyline points="16 18 22 12 16 6"></polyline>
        <polyline points="8 6 2 12 8 18"></polyline>
      </svg>
      ./about.sh
    </h2>
    <div className="section-content">
      <p className="about-text">
        Self-taught in IT with emphasis on virtualization and local environment hosting. Experienced across multiple platforms.
      </p>
    </div>
  </section>
);

export default AboutSection; 