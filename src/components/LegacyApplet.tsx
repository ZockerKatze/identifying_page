'use client'
import React, { useState } from "react";
import type {} from 'react/jsx-runtime';

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
  /* For Future we add more applets here. doing these is a pain tho.
     If you want to do one then look at the JavaDocs. They help a lot
     Compiling these is done with max Java 8. JDK9 disabled the applets.
  */
];

// const EXTENSION_URL = "https://chrome.google.com/webstore/detail/cheerpj-applet-runner/bbmolahhldcbngedljfadjlognfaaein";

export default function LegacyApplet() {
  const [selected, setSelected] = useState(0);
  const applet = APPLETS[selected];
  const basePath = process.env.NEXT_PUBLIC_BASE_PATH || '';
  const src = `${basePath}/legacy-applet/index.html?jar=${encodeURIComponent(applet.jar)}&code=${encodeURIComponent(applet.code)}&width=${applet.width}&height=${applet.height}`;

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
    </div>
  );
} 