'use client'
import React, { useEffect, useRef, useState } from 'react';

interface Mark {
  x: number;
  y: number;
  id: number;
}

const SCRATCH_LIFETIME = 8000; // ms
const SCRATCH_IMG = '/cursors/cat/catscratch.png';
const HOLD_THRESHOLD = 50; // ms before drawing starts
const MIN_DIST = 5; // px minimum distance between marks

const CatScratchOverlay: React.FC = () => {
  const [marks, setMarks] = useState<Mark[]>([]);
  const markId = useRef(0);
  const drawing = useRef(false);
  const holdTimeout = useRef<NodeJS.Timeout | null>(null);
  const lastPos = useRef<{x: number, y: number} | null>(null);

  useEffect(() => {
    let startX = 0, startY = 0;

    const handleDown = (e: MouseEvent) => {
      if (e.button !== 0) return;
      startX = e.clientX;
      startY = e.clientY;
      holdTimeout.current = setTimeout(() => {
        drawing.current = true;
        lastPos.current = { x: startX, y: startY };
        addMark(startX, startY);
      }, HOLD_THRESHOLD);
    };
    const handleMove = (e: MouseEvent) => {
      if (!drawing.current) return;
      const last = lastPos.current;
      if (!last || Math.hypot(e.clientX - last.x, e.clientY - last.y) >= MIN_DIST) {
        addMark(e.clientX, e.clientY);
        lastPos.current = { x: e.clientX, y: e.clientY };
      }
    };
    const handleUp = () => {
      if (holdTimeout.current) clearTimeout(holdTimeout.current);
      drawing.current = false;
      lastPos.current = null;
    };
    window.addEventListener('mousedown', handleDown);
    window.addEventListener('mousemove', handleMove);
    window.addEventListener('mouseup', handleUp);
    return () => {
      window.removeEventListener('mousedown', handleDown);
      window.removeEventListener('mousemove', handleMove);
      window.removeEventListener('mouseup', handleUp);
      if (holdTimeout.current) clearTimeout(holdTimeout.current);
    };
    // eslint-disable-next-line
    function addMark(x: number, y: number) {
      setMarks(marks => [
        ...marks,
        { x, y, id: markId.current++ }
      ]);
    }
  }, []);

  // Remove old marks
  useEffect(() => {
    if (!marks.length) return;
    const interval = setInterval(() => {
      const now = Date.now();
      setMarks(marks => marks.filter(mark => now - mark.id < SCRATCH_LIFETIME));
    }, 200);
    return () => clearInterval(interval);
  }, [marks.length]);

  return (
    <div style={{
      position: 'fixed',
      left: 0,
      top: 0,
      width: '100vw',
      height: '100vh',
      pointerEvents: 'none',
      zIndex: 9999,
    }}>
      {marks.map(mark => (
        <img
          key={mark.id}
          src={SCRATCH_IMG}
          alt="cat scratch"
          style={{
            position: 'absolute',
            left: mark.x - 16,
            top: mark.y - 8,
            width: 32,
            height: 16,
            pointerEvents: 'none',
            userSelect: 'none',
          }}
        />
      ))}
    </div>
  );
};

export default CatScratchOverlay; 