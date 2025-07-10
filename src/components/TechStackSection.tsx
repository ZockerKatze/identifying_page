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
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/python.svg" alt="Python" className="tech-icon" />
            Python
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/lua.svg" alt="Lua" className="tech-icon" />
            Lua
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/rust.svg" alt="Rust" className="tech-icon" />
            Rust
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/openjdk.svg" alt="Java" className="tech-icon" />
            Java
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/gnubash.svg" alt="Bash" className="tech-icon" />
            Bash
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/typescript.svg" alt="TypeScript" className="tech-icon" />
            TypeScript
          </div>
        </div>
      </div>
      <div className="subsection">
        <h3 className="subsection-title">Software</h3>
        <div className="tech-grid">
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/git.svg" alt="Git" className="tech-icon" />
            Git
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/docker.svg" alt="Docker" className="tech-icon" />
            Docker
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/ffmpeg.svg" alt="FFmpeg" className="tech-icon" />
            FFmpeg
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/visualstudiocode.svg" alt="Visual Studio Code" className="tech-icon" />
            Visual Studio Code
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/neovim.svg" alt="NeoVim" className="tech-icon" />
            NeoVim
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/portainer.svg" alt="Portainer" className="tech-icon" />
            Portainer
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/apachemaven.svg" alt="Maven/Gradle" className="tech-icon" />
            Maven/Gradle
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/cmake.svg" alt="CMake" className="tech-icon" />
            CMake
          </div>
        </div>
      </div>
      <div className="subsection">
        <h3 className="subsection-title">Operating Systems</h3>
        <div className="tech-grid">
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/archlinux.svg" alt="Arch Linux" className="tech-icon" />
            Arch Linux
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/alpinelinux.svg" alt="Alpine Linux" className="tech-icon" />
            Alpine Linux
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/debian.svg" alt="Debian" className="tech-icon" />
            Debian
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/redhat.svg" alt="RHEL (Red-Hat)" className="tech-icon" />
            RHEL (Red-Hat)
          </div>
        </div>
      </div>
      <div className="subsection">
        <h3 className="subsection-title">Enterprise Technology</h3>
        {/* We just use a CDN here since local Storage would be efficient, but overkill for this situation */}
        <div className="tech-grid">
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/gradle.svg" alt="Gradle" className="tech-icon" />
            Gradle
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/apachemaven.svg" alt="Maven" className="tech-icon" />
            Maven
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/openjdk.svg" alt="Java Swing" className="tech-icon" />
            Java Swing
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/nginx.svg" alt="Nginx" className="tech-icon" />
            Nginx
          </div>
          <div className="tech-item">
            <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/githubactions.svg" alt="CI/CD" className="tech-icon" />
            CI/CD
          </div>
        </div>
      </div>
      <p className="domain-note">Last Update - 2025@Q3</p>
    </div>
  </section>
);

export default TechStackSection; 