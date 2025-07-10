import React from 'react';
import ProjectCard from './ProjectCard';

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

    {/* @description => We use the custom Component here as its so much simpler to use than writing HTML here (shit anyways)
                        You need to wrap this into a div tho.
        @used_components => './ProjectCard.tsx'
    */}
    <div className="section-content cursor-help">
      <ProjectCard
        title="Markdownblog"
        description="A markdown-based blog platform."
        link="https://google.com/"
        recentFocus={["Finishing touches within MarkdownBlogs Rust-Parser and Frontend"]}
      />
    </div>
    <div className="section-content cursor-help">
      <ProjectCard
        title="This Website"
        description="It's just this Website. You don't need to know more"
        link="https://zockerkatze.github.io/identifying_page/" /* We use the Link to the Current Site here. */
        recentFocus={["Finishing touches"]}
      />
    </div>
    
    <div className='section-content cursor-help'>
      <ProjectCard
      title='Some Rust Learning Projects'
      description='Some learning for the great Rust Programming Language'
      link='https://rust-lang.org/'
      recentFocus={[
        'Learning Async'
      ]}
      />
    </div>
  </section>
);

export default ProjectsSection; 