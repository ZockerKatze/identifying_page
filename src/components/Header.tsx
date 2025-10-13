'use client';

import React from 'react';
import { TypeAnimation } from 'react-type-animation';
import "../app/globals.css"

const Header: React.FC = () => (
  <header>
    <div
      onClick={() => {
        const basePath =  '/identifying_page'; // fuck this . local shit isnt needed anyways.
        window.location.href = `${basePath}/java`;
      }}
      style={{ display: 'inline-block', cursor: 'pointer' }}
    >
      <TypeAnimation
        sequence={[
          "Im a Developer",
          1000,
          "Or a lazy Person if you will",
          50000,
          "I live at Albuquerque New Mexico 308 Negra Arroyo Lane"
        ]}
        wrapper="h1"
        speed={10}
        style={{ fontSize: '2em', display: 'inline-block' }}
        repeat={Infinity}
      />
    </div>
  </header>
);

export default Header; 