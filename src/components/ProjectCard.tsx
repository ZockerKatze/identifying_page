import React from 'react';

interface ProjectCardProps {
    title: string;
    description: string;
    link: string;
    recentFocus?: string[];
    children?: React.ReactNode;
    gitHash?: string;
}

const ProjectCard: React.FC<ProjectCardProps> = ({ title, description, link, children, recentFocus, gitHash }) => (
  <div className="project">
    <div className="project-header">
      <div className="project-info">
        <h3 className="project-title">{title}</h3>
        {gitHash && (
          <div className="git-hash-box">
            <span className="git-hash-label">Commit:</span>
            <code className="git-hash-value">{gitHash}</code>
          </div>
        )}
        <p className="project-description">{description}</p>
        {children}
      </div>
      {/* This is used for the Link */}
      <a href={link} className="project-link" target="_blank" rel="noopener noreferrer">
        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
          <path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"></path>
          <polyline points="15 3 21 3 21 9"></polyline>
          <line x1="10" y1="14" x2="21" y2="3"></line>
        </svg>
      </a>
    </div>
    {/* @description => We use the variable from the interface which allows us to paste the description.
        @component_usage => at './ProjectSection.tsx'
    */}
    {recentFocus && recentFocus.length > 0 && (
      <div className="subsection">
        <h3 className="subsection-title">Recent Focus</h3>
        <ul>
          {recentFocus.map((item, idx) => (
            <li key={idx}>{item}</li>
          ))}
        </ul>
      </div>
    )}
  </div>
);

export default ProjectCard;