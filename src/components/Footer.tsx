'use client'
import React from 'react';

{/*
    @description => This is the Component for the Footer, it mainly displays the badges and the Copyright notice.
    @if_error    => Check for any Issues with client side components such as 'onClick'
    @usage       => At the bottom of a Website. duh. 
*/}

const Footer: React.FC = () => {
  return (
    <footer>
      <p>&copy; Rattatwinko, This Site is OSS. You can check it out under <a className='cursor-pointer' style={{ textDecoration:"none",color:'inherit' }} href="https://github.com/ZockerKatze/identifying_page">GitHub</a></p>
      <img src="https://img.shields.io/badge/typescript-%23007ACC.svg?style=for-the-badge&logo=typescript&logoColor=white" className='footer-badge' alt="TypeScript Badge" />
      <img src="https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB" className='footer-badge' alt="React Badge" />
      <img src="https://img.shields.io/badge/next.js-000000?style=for-the-badge&logo=nextdotjs&logoColor=white" className='footer-badge' alt="Next.js Badge" />
      <img src="https://img.shields.io/badge/gitea-609926?style=for-the-badge&logo=gitea&logoColor=white" className='footer-badge' alt="Gitea Badge" title='Not Here! :3 -->'/>

      {/* @description  => This will open the GitHub Page for this WebSites Source Code
          @if_error     => Check for any issues with the 'onClick' property ; or check if your styling is correct.
          @usage        => In the bottom of the Website , you can click on the GitHub Badge
      */}
      <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white" className='footer-badge cursor-pointer' alt="GitHub Source Code" title="or here"
        onClick={() => window.open('https://github.com/zockerkatze/identifying_page', '_blank')} />
    </footer>
  );
};

export default Footer; 