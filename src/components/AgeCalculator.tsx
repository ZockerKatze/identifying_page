'use client'
import React, { useState, useEffect } from 'react';

interface AgeCalculatorProps {
  birthDate: Date;
  className?: string;
}

const AgeCalculator: React.FC<AgeCalculatorProps> = ({ birthDate, className = '' }) => {
  const [age, setAge] = useState<number>(0);

  useEffect(() => {
    // Calculate Vienna date components
    const getViennaDate = () => {
      const now = new Date();
      const parts = new Intl.DateTimeFormat('en-US', {
        timeZone: 'Europe/Vienna',
        year: 'numeric',
        month: 'numeric',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric',
        second: 'numeric',
      }).formatToParts(now);

      let year = 0, month = 0, day = 0, hour = 0, minute = 0, second = 0;
      parts.forEach(p => {
        const val = parseInt(p.value, 10);
        if (p.type === 'year') year = val;
        if (p.type === 'month') month = val;
        if (p.type === 'day') day = val;
        if (p.type === 'hour') hour = val;
        if (p.type === 'minute') minute = val;
        if (p.type === 'second') second = val;
      });

      return { year, month, day, hour, minute, second };
    };

    const calculateAge = () => {
      const { year, month, day } = getViennaDate();
      let newAge = year - birthDate.getFullYear();
      const monthDiff = month - 1 - birthDate.getMonth(); // JS month is 0-based
      if (monthDiff < 0 || (monthDiff === 0 && day < birthDate.getDate())) newAge--;
      return newAge;
    };

    const updateAge = () => setAge(calculateAge());

    updateAge(); // initial set

    // Calculate milliseconds until next Vienna midnight
    const { hour, minute, second } = getViennaDate();
    const msUntilMidnight = ((23 - hour) * 3600 + (59 - minute) * 60 + (59 - second) + 1) * 1000;

    const timer = setTimeout(function tick() {
      updateAge();
      // Schedule next update exactly 24h later
      setTimeout(tick, 24 * 60 * 60 * 1000);
    }, msUntilMidnight);

    return () => clearTimeout(timer);
  }, [birthDate]);

  return <span className={className}>{age}</span>;
};

export default AgeCalculator;

