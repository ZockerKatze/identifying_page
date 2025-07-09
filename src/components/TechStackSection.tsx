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
          <div className="tech-item">
            {
            /* We just use a CDN here since local Storage would be efficient, but overkill for this situation 
              This is not as readable but, it does the Job.
              The only thing you need to read anyways is the Styling, you can skip the links!
            */
            }
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/python.svg" alt="Python" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#3776AB', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            Python
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/lua.svg" alt="Lua" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#2C2D72', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            Lua
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/rust.svg" alt="Rust" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#DEA584', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            Rust
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/openjdk.svg" alt="Java" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#007396', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            Java
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/gnubash.svg" alt="Bash" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#4EAA25', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            Bash
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/typescript.svg" alt="TypeScript" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#3178C6', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            TypeScript
          </div>
        </div>
      </div>
      <div className="subsection">
        <h3 className="subsection-title">Software</h3>
        {/* We just use a CDN here since local Storage would be efficient, but overkill for this situation */}
        <div className="tech-grid">
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/git.svg" alt="Git" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#F05032', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            Git
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/docker.svg" alt="Docker" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#2496ED', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            Docker
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/ffmpeg.svg" alt="FFmpeg" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#007808', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            FFmpeg
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/visualstudiocode.svg" alt="Visual Studio Code" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#007ACC', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            Visual Studio Code
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/neovim.svg" alt="NeoVim" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#57A143', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            NeoVim
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/portainer.svg" alt="Portainer" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#13BEF9', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            Portainer
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/apachemaven.svg" alt="Maven/Gradle" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#C71A36', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            Maven/Gradle
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/cmake.svg" alt="CMake" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#064F8C', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            CMake
          </div>
        </div>
      </div>
      <div className="subsection">
        <h3 className="subsection-title">Operating Systems</h3>
        <div className="tech-grid">
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/archlinux.svg" alt="Arch Linux" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#1793D1', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            Arch Linux
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/alpinelinux.svg" alt="Alpine Linux" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#0D597F', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            Alpine Linux
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/debian.svg" alt="Debian" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#A81D33', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            Debian
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/redhat.svg" alt="RHEL (Red-Hat)" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#EE0000', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            RHEL (Red-Hat)
          </div>
        </div>
      </div>
      <div className="subsection">
        <h3 className="subsection-title">Enterprise Technology</h3>
        {/* We just use a CDN here since local Storage would be efficient, but overkill for this situation */}
        <div className="tech-grid">
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/gradle.svg" alt="Gradle" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#02303A', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            Gradle
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/apachemaven.svg" alt="Maven" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#C71A36', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            Maven
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/openjdk.svg" alt="Java Swing" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#007396', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            Java Swing
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/nginx.svg" alt="Nginx" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#009639', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            Nginx
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/githubactions.svg" alt="CI/CD" style={{height: '1em', verticalAlign: 'middle', marginRight: '0.5em', color: '#2088FF', background: '#fff', borderRadius: '3px', padding: '2px'}} />
            CI/CD
          </div>
        </div>
      </div>
      <p className="domain-note">Last Update - 2025@Q3</p>
    </div>
  </section>
);

export default TechStackSection; 