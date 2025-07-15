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
  /* For Future we add more applets here. doing these is a pain tho.
     If you want to do one then look at the JavaDocs. They help a lot
     Compiling these is done with max Java 8. JDK9 disabled the applets.
  */
];

const EXTENSION_URL = "https://chrome.google.com/webstore/detail/cheerpj-applet-runner/bbmolahhldcbngedljfadjlognfaaein";

export default function LegacyApplet() {
  const [selected, setSelected] = useState(0);
  const applet = APPLETS[selected];
  const basePath = process.env.NEXT_PUBLIC_BASE_PATH || '';
  const src = `${basePath}/legacy-applet/index.html?jar=${encodeURIComponent(applet.jar)}&code=${encodeURIComponent(applet.code)}&width=${applet.width}&height=${applet.height}`;

  return (
    <div style={{ display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", minHeight: "60vh" }}>
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
      <div style={{ marginTop: 24, maxWidth: 500, textAlign: "center", color: "#fff", background: "#222", padding: 16, borderRadius: 8 }}>
        <strong>Note:</strong> This Java applet requires the <a href={EXTENSION_URL} target="_blank" rel="noopener noreferrer" style={{ color: "#4af" }}>CheerpJ Applet Runner</a> browser extension.<br />
        If you do not see the applet above, please install the extension and reload the page. <b>Be patient with this. Its slow as shit.</b>
      </div>
    </div>
  );
} 