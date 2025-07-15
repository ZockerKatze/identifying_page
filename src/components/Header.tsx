'use client';

import React from 'react';
import { TypeAnimation } from 'react-type-animation';

const Header: React.FC = () => (
  <header>
    <div
      onClick={() => {
        // fallback to '/identifying_page' if env is not set
        const basePath = process.env.NEXT_PUBLIC_BASE_PATH || '/identifying_page';
        window.location.href = `${basePath}/java`;
      }}
      style={{ display: 'inline-block', cursor: 'pointer' }}
    >
      <TypeAnimation
        sequence={[
          'Rattatwinko',
          5000,
          'ZockerKatze',
          5000
        ]}
        wrapper="h1"
        speed={10}
        style={{ fontSize: '2em', display: 'inline-block' }}
        repeat={Infinity}
      />
    </div>
    <br />
    <TypeAnimation
      sequence={[
        'Developer',
        5000,
        'Server-Admin',
        5000
      ]}
      wrapper='p'
      speed={10}
      style={{ fontSize: 'em', display: 'inline-block' }}
      repeat={Infinity}
    />
  </header>
);

export default Header; 