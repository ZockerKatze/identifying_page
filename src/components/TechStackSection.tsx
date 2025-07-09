import React from 'react';

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
    <div className="section-content">
      <div className="subsection">
        <h3 className="subsection-title">Languages</h3>
        <div className="tech-grid">
          <div className="tech-item">Python</div>
          <div className="tech-item">Lua</div>
          <div className="tech-item">Rust</div>
          <div className="tech-item">Java</div>
          <div className="tech-item">Bash</div>
          <div className="tech-item">TypeScript</div>
        </div>
      </div>
      <div className="subsection">
        <h3 className="subsection-title">Software</h3>
        <div className="tech-grid">
          <div className="tech-item">Git</div>
          <div className="tech-item">Docker</div>
          <div className="tech-item">FFmpeg</div>
          <div className="tech-item">Visual Studio Code</div>
          <div className="tech-item">NeoVim</div>
          <div className="tech-item">Portainer</div>
          <div className="tech-item">Maven/Gradle</div>
          <div className="tech-item">CMake</div>
        </div>
      </div>
      <div className="subsection">
        <h3 className="subsection-title">Operating Systems</h3>
        <div className="tech-grid">
          <div className="tech-item">Fedora</div>
          <div className="tech-item">Alpine Linux</div>
          <div className="tech-item">Debian</div>
          <div className="tech-item">Openmediavault</div>
        </div>
      </div>
      <div className="subsection">
        <h3 className="subsection-title">Enterprise Technology</h3>
        <div className="tech-grid">
          <div className="tech-item">Gradle</div>
          <div className="tech-item">Maven</div>
          <div className="tech-item">Java Swing</div>
          <div className="tech-item">Nginx</div>
          <div className="tech-item">CI/CD</div>
        </div>
      </div>
      <p className="domain-note">Last Update - 2025@Q3</p>
    </div>
  </section>
);

export default TechStackSection; 