'use client'
import React from 'react';
import UniversalDataProvider from './UniversalDataProvider';

{/*
  @description   => ExperienceSection displays experience items using data from iconconfig.json.
    @description_iconconfig.json => If you want to modify experience items then do it there. no actual code has to be written.
  @used_components => UniversalDataProvider
  @icon_source   => SimpleIcons (check the link below)
*/}

const getIconUrl = (name: string, icon?: string) => {
  const slug = icon ? icon :
    name
      .toLowerCase()
      .replace(/\s+/g, '')
      .replace(/\+/g, 'plus')
      .replace(/\./g, 'dot')
      .replace(/\#/g, 'sharp');
  return `https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/${slug}.svg`;
};

interface ExperienceItemProps {
  name: string;
  duration: string;
  icon?: string;
}

const ExperienceItem: React.FC<ExperienceItemProps> = ({ name, duration, icon }) => (
  <div className="experience-item">
    <img src={getIconUrl(name, icon)} alt={name} className="tech-icon" />
    <span className="experience-item-name">{name}</span>
    <span className="experience-item-duration">{duration}</span>
  </div>
);

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
    {/*
      @description      => Loads experience data from iconconfig.json and renders each experience item with icons.
      @used_components  => UniversalDataProvider, ExperienceItem
      @data_source      => /public/iconconfig.json
    */}
    <UniversalDataProvider configPath="/config/iconconfig.json">
      {(data) => {
        if (!data || typeof data !== 'object' || !('Experience' in data)) {
          return <div className="section-content">Loading experience data...</div>;
        }
        const experienceItems = (data as { Experience: Array<{ name: string; duration: string; icon?: string }> }).Experience;
        return (
          <div className="section-content">
            <div className="experience-list">
              {experienceItems.map((item) => (
                <ExperienceItem key={item.name} name={item.name} duration={item.duration} icon={item.icon} />
              ))}
            </div>
          </div>
        );
      }}
    </UniversalDataProvider>
  </section>
);

export default ExperienceSection; 