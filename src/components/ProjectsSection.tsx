import React from 'react';

const ProjectsSection: React.FC = () => (
  <section>
    <h2 className="section-title">
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
        <path d="M5 12.55a11 11 0 0 1 14.08 0"></path>
        <path d="M1.42 9a16 16 0 0 1 21.16 0"></path>
        <path d="M8.53 16.11a6 6 0 0 1 6.95 0"></path>
        <line x1="12" y1="20" x2="12.01" y2="20"></line>
      </svg>
      ./projects.md
    </h2>
    <div className="section-content">
      <div className="project">
        <div className="project-header">
          <h3 className="project-title">Markdownblog</h3>
          <a href="https://google.com/" className="project-link">
            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
              <path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"></path>
              <polyline points="15 3 21 3 21 9"></polyline>
              <line x1="10" y1="14" x2="21" y2="3"></line>
            </svg>
          </a>
        </div>
        <p className="project-description"></p>
      </div>
      <div className="subsection">
        <h3 className="subsection-title">Recent Focus</h3>
        <ul>
          <li>Finishing touches within MarkdownBlogs Rust-Parser and Frontend</li>
        </ul>
      </div>
    </div>
  </section>
);

export default ProjectsSection; 