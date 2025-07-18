'use client'
import React, { useState } from "react";
import type {} from 'react/jsx-runtime';
import Image from 'next/image';

export const APPLETS = [
  {
    label: "3D Cube",
    jar: "MyApplet.jar",
    code: "MyApplet",
    width: 640,
    height: 480,
  },
  {
    label: "Tetris Java Applet",
    jar: "TetrisApplet.jar",
    code: "TetrisApplet",
    width: 640,
    height: 480,
  },
  {
    label: "SineWave",
    jar: "sinewave.jar",
    code: "sinewave",
    width: 640,
    height: 480,
  },
  {
    label: "Snake",
    jar: "SnakeApplet.jar",
    code: "SnakeApplet",
    width: 640,
    height: 480,
  }
  /* For Future we add more applets here. doing these is a pain tho.
     If you want to do one then look at the JavaDocs. They help a lot
     Compiling these is done with max Java 8. JDK9 disabled the applets.
  */
];

// const EXTENSION_URL = "https://chrome.google.com/webstore/detail/cheerpj-applet-runner/bbmolahhldcbngedljfadjlognfaaein";

export default function LegacyApplet() {
  const [selected, setSelected] = useState(0);
  const [carouselIndex, setCarouselIndex] = useState(0);
  const [zoomed, setZoomed] = useState(false);
  const applet = APPLETS[selected];
  const basePath = process.env.NEXT_PUBLIC_BASE_PATH || '';
  const src = `${basePath}/legacy-applet/index.html?jar=${encodeURIComponent(applet.jar)}&code=${encodeURIComponent(applet.code)}&width=${applet.width}&height=${applet.height}`;

  // Demo images for the carousel
  const demoImages = [
    { src: `${basePath}/demoimages/cube.png`, alt: '3D Cube' },
    { src: `${basePath}/demoimages/tetris.png`, alt: 'Tetris' },
    { src: `${basePath}/demoimages/snake.png`, alt: 'Snake' },
    { src: `${basePath}/demoimages/waves.png`, alt: 'Waves' },
    { src: `${basePath}/demoimages/profiler.png`, alt:'JProfiler'}
  ];

  const handlePrev = () => setCarouselIndex((prev) => (prev - 1 + demoImages.length) % demoImages.length);
  const handleNext = () => setCarouselIndex((prev) => (prev + 1) % demoImages.length);
  const handleZoom = () => setZoomed(true);
  const handleCloseZoom = () => setZoomed(false);

  return (
    <div style={{ display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", minHeight: "60vh" }}>
      <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', background: '#111', border: '2px solid rgba(128,128,128,0.5)', borderRadius: 8, padding: 24, marginBottom: 24 }}>
        <label style={{ color: "#fff", marginBottom: 8 }}>
          Select Applet:{" "}
          <select value={selected} onChange={e => setSelected(Number(e.target.value))}>
            {APPLETS.map((a, i) => (
              <option value={i} key={a.label}>{a.label}</option>
            ))}
          </select>
        </label>
        <iframe
          src={src}
          style={{ width: applet.width + 20, height: applet.height + 40, border: "none", background: "#fff" }}
          title="Legacy Java Applet"
        />
      </div>
      <div style={{
        margin: '0 auto 10px auto',
        maxWidth: 600,
        width: '100%',
        textAlign: 'center',
        color: '#fff',
        background: '#181818',
        border: '2px solid rgba(128,128,128,0.5)',
        borderRadius: 10,
        padding: 28,
        lineHeight: 1.7,
        letterSpacing: 0.01,
        boxShadow: '0 2px 12px rgba(0,0,0,0.12)'
      }}>
        <a
          href="https://github.com/ZockerKatze/identifying_page/tree/main/public/legacy-applet"
          target="_blank"
          rel="noopener noreferrer"
          style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', textDecoration: 'none', color: 'inherit', gap: 16 }}
        >
          <span style={{ fontWeight: 'bold', fontSize: '1.05em' }}>
            View the Source Code on GitHub:
          </span>
          <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24">
            <path fill="white" d="M12 0.296C5.37 0.296 0 5.665 0 12.296c0 5.289 3.438 9.772 8.205 11.367.6.111.82-.26.82-.577v-2.234c-3.338.725-4.033-1.61-4.033-1.61-.546-1.387-1.333-1.756-1.333-1.756-1.09-.746.083-.73.083-.73 1.205.084 1.84 1.237 1.84 1.237 1.07 1.834 2.809 1.304 3.495.997.108-.776.42-1.305.763-1.605-2.665-.304-5.466-1.334-5.466-5.932 0-1.31.468-2.381 1.235-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.3 1.23a11.5 11.5 0 0 1 3.003-.404c1.02.005 2.045.138 3.003.404 2.29-1.552 3.296-1.23 3.296-1.23.653 1.652.242 2.873.118 3.176.77.84 1.233 1.911 1.233 3.221 0 4.61-2.805 5.625-5.475 5.921.43.372.823 1.103.823 2.222v3.293c0 .319.218.694.825.576C20.565 22.065 24 17.584 24 12.296 24 5.665 18.627 0.296 12 0.296z"/>
          </svg>
        </a>
      </div>

      <div style={{
        marginTop: 32,
        maxWidth: 600,
        textAlign: "center",
        color: "#fff",
        background: "#111",
        border: "2px solid rgba(128,128,128,0.5)",
        borderRadius: 10,
        padding: 36,
        marginBottom: 32,
        lineHeight: 1.7,
        letterSpacing: 0.01,
      }}>
        <p style={{ fontWeight: 'bold', fontSize: '1.2em', marginBottom: 18 }}>Back to the future:</p>
        <p style={{ margin: '0 0 22px 0', fontWeight: 400 }}>
          Why did I do this? Easy answer! I watched a video about Java, and they mentioned how the deprecation of Applets is really sad. I agree with that.
        </p>
        <p style={{ margin: '0 0 22px 0', fontWeight: 400 }}>
          But then I discovered <strong>CheerpJ</strong>, a project that brings Java Applets back to life in modern browsers. I thought that was really cool and wanted to try it out myself!
        </p>
        <p style={{ margin: 0, fontWeight: 400 }}>
          Give these applets a try! The controls are simple, and I hope you have fun playing Tetris. It&apos;s basic, but still a lot of fun!
        </p>
      </div>
      <div style={{
        marginBottom: 32,
        maxWidth: 600,
        textAlign: 'left',
        color: '#fff',
        background: '#181818',
        border: '2px solid rgba(128,128,128,0.5)',
        borderRadius: 10,
        padding: 28,
        lineHeight: 1.7,
        letterSpacing: 0.01,
        boxShadow: '0 2px 12px rgba(0,0,0,0.12)'
      }}>
        <h3 style={{ fontWeight: 'bold', fontSize: '1.15em', marginBottom: 16 }}>Game Contents:</h3>
        <ul style={{ paddingLeft: 18, margin: 0 }}>
          <li style={{ marginBottom: 12 }}><strong>3D Cube</strong> – Why? To test the Applet&apos;s initial functionality. If this runs, everything else will.</li>
          <li style={{ marginBottom: 12 }}><strong>Tetris</strong> – Why? I got this idea from a friend while talking about <i>some redacted topic</i>.</li>
          <li style={{ marginBottom: 12 }}><strong>Sine Wave Simulator</strong> – I did this to prove my Mathematics Teacher wrong. <i>I hate him</i>.</li>
          <li style={{ marginBottom: 12 }}><strong>Snake</strong> – Why? I already wrote a Java Snake and I just had to rewrite it to some extent to make it work with Java applets.</li>
          <li><strong>Java Applet Profiler</strong> - If you are looking to develop Java Applets yourself then you might want to checkout the Profiler I wrote. I uses a drag and drop system for .jar or .class Files that come from compiled Java. You can Profile the Runtime of your Java Applets there!</li>
        </ul>
        <hr style={{ opacity: 0.5, margin: '28px 0 18px 0' }} />
        <div style={{ display: 'flex', justifyContent: 'center', width: '100%' }}>
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', width: 320 }}>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 16 }}>
              <button onClick={handlePrev} style={{ background: '#222', color: '#fff', border: 'none', borderRadius: 4, padding: '8px 12px', cursor: 'pointer', fontSize: 18 }} aria-label="Previous image">⟨</button>
              <Image
                src={demoImages[carouselIndex].src}
                alt={demoImages[carouselIndex].alt}
                width={240}
                height={180}
                style={{ borderRadius: 8, border: '1px solid #333', background: '#fff', cursor: 'zoom-in' }}
                priority
                onClick={handleZoom}
              />
              <button onClick={handleNext} style={{ background: '#222', color: '#fff', border: 'none', borderRadius: 4, padding: '8px 12px', cursor: 'pointer', fontSize: 18 }} aria-label="Next image">⟩</button>
            </div>
            <div style={{ color: '#fff', marginTop: 8, fontSize: 14 }}>{demoImages[carouselIndex].alt}</div>
          </div>
        </div>
      </div>
      {/* Zoom Modal */}
      {zoomed && (
        <div
          onClick={handleCloseZoom}
          style={{
            position: 'fixed',
            top: 0,
            left: 0,
            width: '100vw',
            height: '100vh',
            background: 'rgba(0,0,0,0.85)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            zIndex: 1000,
            cursor: 'zoom-out',
          }}
        >
          <div style={{ position: 'relative' }}>
            <Image
              src={demoImages[carouselIndex].src}
              alt={demoImages[carouselIndex].alt}
              width={720}
              height={540}
              style={{ borderRadius: 12, border: '2px solid #fff', background: '#222', maxWidth: '90vw', maxHeight: '80vh', objectFit: 'contain' }}
              priority
            />
            <button
              onClick={handleCloseZoom}
              style={{
                position: 'absolute',
                top: 8,
                right: 8,
                background: 'rgba(0,0,0,0.7)',
                color: '#fff',
                border: 'none',
                borderRadius: '50%',
                width: 32,
                height: 32,
                fontSize: 20,
                cursor: 'pointer',
                zIndex: 1001,
              }}
              aria-label="Close zoomed image"
            >
              ×
            </button>
          </div>
        </div>
      )}
    </div>
  );
} 