// components/JavaApplet.tsx
import Link from 'next/link';
import styles from './JavaApplet.module.css';

// gh pages is annoying :C
const basePath = process.env.NEXT_PUBLIC_BASE_PATH || '';

export default function JavaApplet() {
  return (
    <div className={styles.container}>
      <Link href="java" className={styles.link}>
        
        <svg 
          width="70" 
          height="60" 
          viewBox="0 0 100 100" 
          className={styles.arrowSvg}
        >
          <path 
            d="M 10 60 C 0 80, 20 90, 30 70 C 40 50, 60 30, 90 20 M 75 15 L 90 20 L 80 35" 
            fill="none" 
            strokeWidth="4" 
            strokeLinecap="round"
            className={styles.arrowPath}
          />
        </svg>

        {/* svg java logo */}
        <img
          src={`${basePath}/demoimages/java.svg`}
          alt="Java Logo"
          className={styles.javaLogo}
        />
        
      </Link>
    </div>
  );
};