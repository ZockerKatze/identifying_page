import React from 'react';

const ExperienceSection: React.FC = () => (
  <section>
    <h2 className="section-title">
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
        <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
        <line x1="16" y1="2" x2="16" y2="6"></line>
        <line x1="8" y1="2" x2="8" y2="6"></line>
        <line x1="3" y1="10" x2="21" y2="10"></line>
      </svg>
      ./experience.log
    </h2>
    <div className="section-content">
      <div className="experience-list">
        <div className="experience-item">
          <span className="experience-item-name">Linux</span>
          <span className="experience-item-duration">4+ years</span>
        </div>
        <div className="experience-item">
          <span className="experience-item-name">Docker</span>
          <span className="experience-item-duration">2+ years</span>
        </div>
        <div className="experience-item">
          <span className="experience-item-name">Networking</span>
          <span className="experience-item-duration">3+ years</span>
        </div>
        <div className="experience-item">
          <span className="experience-item-name">Openmediavault</span>
          <span className="experience-item-duration">3 year</span>
        </div>
        <div className="experience-item">
          <span className="experience-item-name">Git</span>
          <span className="experience-item-duration">2+ years</span>
        </div>
      </div>
    </div>
  </section>
);

export default ExperienceSection; 