'use client';

import React from 'react';
import { TypeAnimation } from 'react-type-animation';

const ContactSection: React.FC = () => (
  <section>
    <h2 className="section-title">
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
        <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path>
        <polyline points="22,6 12,13 2,6"></polyline>
      </svg>
      ./contact.yml
    </h2>
    <div className="section-content">
      <div className="contact-list">
        <a href="mailto:seppmutterman@gmail.com" className="contact-item">
          <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/gmail.svg" alt="Email" className="contact-icon" />
          <span>seppmutterman@gmail.com</span>
        </a>
        <a href="https://instagram.com/rattatwinko" target="_blank" rel="noopener noreferrer" className="contact-item">
          <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/instagram.svg" alt="Instagram" className="contact-icon" />
          <span>@rattatwinko</span>
          <TypeAnimation
          sequence={[
            ' // ',
            2000,
            ' // Check out some of Recent Projects!',
            1000
          ]}
          wrapper='span'
          speed={50}
          style={{ fontSize: '0.8em', color: 'var(--text-secondary)', marginLeft: '0.5rem' }}
          repeat={Infinity}
          />
        </a>
        <a target="_blank" rel="noopener noreferrer" className="contact-item">
          <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/discord.svg" alt="Discord" className="contact-icon" />
          <span>
            @rattatwingo
            <TypeAnimation
              sequence={[
                ' // ',
                2000,
                ' // Add Me :3',
                1000
              ]}
              wrapper="span"
              speed={50}
              style={{ fontSize: '0.8em', color: 'var(--text-secondary)', marginLeft: '0.5rem' }}
              repeat={Infinity}
            />
          </span>
        </a>
        <a href="https://tiktok.com/@rattatwingo" target="_blank" rel="noopener noreferrer" className="contact-item">
          <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/tiktok.svg" alt="TikTok" className="contact-icon" />
          <span>@rattatwingo</span>
        </a>
        <a href="https://github.com/ZockerKatze" target="_blank" rel="noopener noreferrer" className="contact-item">
          <img src="https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/github.svg" alt="GitHub" className="contact-icon" />
          <span>@rattatwinko</span>
        </a>
        <p className="contact-note">
          Feel free to reach out! I&apos;m most active on Discord and Instagram.<br/>
          <strong>Disclaimer:</strong> I do not develop on GitHub. I use Gitea for version control. That&apos;s why my GitHub is empty.
        </p>
      </div>
    </div>
  </section>
);

export default ContactSection; 