import React from 'react';
import AgeCalculator from './AgeCalculator';

const AboutSection: React.FC = () => (
  <section>
  <h2 className="section-title">
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="16"
      height="16"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="1.5"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <polyline points="16 18 22 12 16 6"></polyline>
      <polyline points="8 6 2 12 8 18"></polyline>
    </svg>
    ./about.sh
  </h2>

  <div className="section-content">
    <div className="about-text">
      <p>I make Computers do Stuff.</p>
      <p style={{fontStyle:'italic'}}>Check out my{" "}
        <a
          href="http://rattatwinko.servecounterstrike.com/"
          className="cursor-pointer"
          style={{ color: "inherit", textDecoration: "underline" }}
        >
          Gitea
        </a>
        , that's where I do Stuff.
      </p>

      <br style={{ opacity: 0.5, margin: "10, 0" }} />
      
      {/* Javascript uses 2 for March and 3 for April, dont get confused. Ik javscripts fucking weird */}
      <p>I am <i><AgeCalculator birthDate={new Date(2010, 2, 9)} /></i> Years old.</p>
      <hr style={{ opacity: 0.5, margin: "1em 0" }} />
      <p>
        Sometimes I do <strong style={{ fontWeight: 700, fontStyle: "italic" }}>Frontend</strong> but most of the time I reside in the{" "}
        <strong style={{ fontWeight: 700, fontStyle: "italic" }}>Backend</strong>
      </p>

      <div
        style={{
          fontSize: "0.85em",
          marginTop: "0.3em",
          color: "var(--text-secondary)",
          paddingLeft: "1.5em",
          fontStyle: "italic",
        }}> 
        FunFact: I did the <strong style={{ fontWeight: 700, fontStyle: "italic" }}>Frontend</strong> to this Website!
      </div>
    </div>
  </div>
</section>

);

export default AboutSection; 