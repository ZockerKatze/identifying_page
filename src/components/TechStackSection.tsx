'use client'
import React from 'react';
import TechStackDataProvider, { TechConfig } from './TechStackDataProvider';

{/*
  @description   => TechStackSection displays categorized technology items using data from iconconfig.json.
    @description_iconconfig.json => If you want to modify shit then do it there. no actual code has to be written.
  @used_components => TechStackDataProvider, TechItem
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

interface TechItemProps {
  name: string;
  url: string;
  icon?: string;
}

const TechItem: React.FC<TechItemProps> = ({ name, url, icon }) => (
  <div
    className="tech-item cursor-pointer"
    onClick={() => window.open(url, "_blank")}
  >
    <img src={getIconUrl(name, icon)} alt={name} className="tech-icon" />
    {name}
  </div>
);

const TechStackSection: React.FC = () => (
  <section>
    <h2 className="section-title">
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
        <rect x="2" y="2" width="20" height="8" rx="2" ry="2"></rect>
        <rect x="2" y="14" width="20" height="8" rx="2" ry="2"></rect>
        <line x1="6" y1="6" x2="6.01" y2="6"></line>
        <line x1="6" y1="18" x2="6.01" y2="18"></line>
      </svg>
      ./tech_stack.json
    </h2>
    {/*
      @description      => Loads tech stack data from iconconfig.json and renders each category and its items.
      @used_components  => TechStackDataProvider, TechItem
      @data_source      => /public/iconconfig.json
    */}
    <TechStackDataProvider>
      {(data: TechConfig | null) =>
        data ? (
          <div className="section-content">
            {Object.entries(data)
              /* We filter out any others here. If anything goes wrong check this */
              .filter(([category]) => category !== 'ContactIcons' && category !== 'InfrastructureServices' && category !== 'Experience')
              .map(([category, items]) => (
                <div className="subsection" key={category}>
                  <h3 className="subsection-title">{category}</h3>
                  <div className="tech-grid">
                    {items.map(item => (
                      <TechItem key={item.name} name={item.name} url={item.url} icon={item.icon} />
                    ))}
                  </div>
                </div>
              ))}
            <p className="domain-note">Last Update - 2026@Q1</p>
          </div>
        ) : (
          <div className="section-content">Loading...</div>
        )
      }
    </TechStackDataProvider>
  </section>
);

export default TechStackSection; 