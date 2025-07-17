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
  ];

  const handlePrev = () => setCarouselIndex((prev) => (prev - 1 + demoImages.length) % demoImages.length);
  const handleNext = () => setCarouselIndex((prev) => (prev + 1) % demoImages.length);
  const handleZoom = () => setZoomed(true);
  const handleCloseZoom = () => setZoomed(false);

  return (
    <div style={{ display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", minHeight: "60vh" }}>
      <div style={{ background: '#fffbe6', color: '#a67c00', border: '1px solid #ffe58f', borderRadius: 8, padding: 16, marginBottom: 20, maxWidth: 600, textAlign: 'center', fontWeight: 'bold', fontSize: '1.05em' }}>
        ⚠️ CheerpJ is being loaded through the Web, it is now not depending on a Browser Extension. But has become slower.
      </div>
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
      {/* Game Contents Section */}
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
          <li><strong>Snake</strong> – Why? I already wrote a Java Snake and I just had to rewrite it to some extent to make it work with Java applets.</li>
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