'use client';

import React from 'react';
import { TypeAnimation } from 'react-type-animation';
import Image from 'next/image';
import UniversalDataProvider from './UniversalDataProvider';

interface ContactIcon {
  name: string;
  icon: string;
  url: string;
  display: string;
}

const ICON_SIZE = 32;

const ContactSection: React.FC = () => (
  <UniversalDataProvider configPath="/config/iconconfig.json">
    {(data) => {
      if (!data || typeof data !== 'object' || !('ContactIcons' in data)) {
        return <div>Loading...</div>;
      }
      const contactIcons = (data as { ContactIcons: ContactIcon[] }).ContactIcons || [];
      return (
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
              {contactIcons.map((contact: ContactIcon) => {
                const isDiscord = contact.name === 'Discord';
                const isInstagram = contact.name === 'Instagram';
                // const isGmail = contact.name === 'Gmail';
                return (
                  <a 
                    key={contact.name}
                    href={contact.url} 
                    target={contact.name === 'Gmail' ? undefined : "_blank"} 
                    rel={contact.name === 'Gmail' ? undefined : "noopener noreferrer"} 
                    className={`contact-item ${isDiscord ? 'cursor-not-allowed' : 'cursor-pointer'}`}
                  >
                    <Image 
                      src={`https://cdn.jsdelivr.net/npm/simple-icons@v11/icons/${contact.icon}.svg`} 
                      alt={contact.name} 
                      className="contact-icon" 
                      width={ICON_SIZE} 
                      height={ICON_SIZE} 
                    />
                    <span>
                      {contact.display}
                      {isInstagram && (
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
                      )}
                      {isDiscord && (
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
                      )}
                    </span>
                  </a>
                );
              })}
              <p className="contact-note cursor-text">
                Feel free to reach out! I&apos;m most active on Discord.<br/>
                <strong>Disclaimer:</strong> I do not develop on GitHub. I use <a href='http://rattatwinko.servecounterstrike.com/'>Gitea</a> for version control. That&apos;s why my GitHub is empty.<br />
                <strong>Second Disclaimer:</strong> <i>I used to have a Email / Insta on here.</i>
              </p>
            </div>
          </div>
        </section>
      );
    }}
  </UniversalDataProvider>
);

export default ContactSection; 