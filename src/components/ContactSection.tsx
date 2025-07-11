'use client';

import React from 'react';
import { TypeAnimation } from 'react-type-animation';
import UniversalDataProvider from './UniversalDataProvider';
import Image from 'next/image';

interface ContactIcon {
  name: string;
  iconUrl: string;
}

const getContactIconUrl = (contactIcons: ContactIcon[], name: string) => {
  const icon = contactIcons.find((i) => i.name === name);
  return icon ? icon.iconUrl : '';
};

const FALLBACK_ICON = '/file.svg'; // fallback icon in public/

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
              <a href="mailto:seppmutterman@gmail.com" className="contact-item cursor-pointer">
                <Image src={getContactIconUrl(contactIcons, 'Gmail') || FALLBACK_ICON} alt="Email" className="contact-icon" width={ICON_SIZE} height={ICON_SIZE} />
                <span>seppmutterman@gmail.com</span>
              </a>
              <a href="https://instagram.com/rattatwinko" target="_blank" rel="noopener noreferrer" className="contact-item cursor-pointer">
                <Image src={getContactIconUrl(contactIcons, 'Instagram') || FALLBACK_ICON} alt="Instagram" className="contact-icon" width={ICON_SIZE} height={ICON_SIZE} />
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
              <a target="_blank" rel="noopener noreferrer" className="contact-item cursor-not-allowed">
                <Image src={getContactIconUrl(contactIcons, 'Discord') || FALLBACK_ICON} alt="Discord" className="contact-icon " width={ICON_SIZE} height={ICON_SIZE} />
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
              <a href="https://tiktok.com/@rattatwingo" target="_blank" rel="noopener noreferrer" className="contact-item cursor-pointer">
                <Image src={getContactIconUrl(contactIcons, 'TikTok') || FALLBACK_ICON} alt="TikTok" className="contact-icon" width={ICON_SIZE} height={ICON_SIZE} />
                <span>@rattatwingo</span>
              </a>
              <a href="https://github.com/ZockerKatze" target="_blank" rel="noopener noreferrer" className="contact-item cursor-pointer">
                <Image src={getContactIconUrl(contactIcons, 'GitHub') || FALLBACK_ICON} alt="GitHub" className="contact-icon" width={ICON_SIZE} height={ICON_SIZE} />
                <span>@rattatwinko</span>
              </a>
              <p className="contact-note cursor-text">
                Feel free to reach out! I&apos;m most active on Discord and Instagram.<br/>
                <strong>Disclaimer:</strong> I do not develop on GitHub. I use Gitea for version control. That&apos;s why my GitHub is empty.
              </p>
            </div>
          </div>
        </section>
      );
    }}
  </UniversalDataProvider>
);

export default ContactSection; 