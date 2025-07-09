'use client';

import React from 'react';
import { TypeAnimation } from 'react-type-animation';

const Header: React.FC = () => (
  <header>
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
    <p className="header-subtitle">Developer</p>
  </header>
);

export default Header; 