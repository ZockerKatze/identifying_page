'use client'
import React, { useState, useEffect } from 'react';

/*
 * @description => AgeCalculator component that calculates and displays current age based on birth date using Vienna timezone from API
 * @used_components => React hooks (useState, useEffect)
 */
interface AgeCalculatorProps {
  birthDate: Date;
  className?: string;
}

const AgeCalculator: React.FC<AgeCalculatorProps> = ({ birthDate, className = '' }) => {
  const [age, setAge] = useState<number>(0);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    let timer: NodeJS.Timeout;
    let cancelled = false;

    const fetchViennaTimeAndCalculateAge = async () => {
      try {
        setLoading(true);
        
        // Use timeapi.io for Vienna time (supports CORS)
        const response = await fetch('https://timeapi.io/api/Time/current/zone?timeZone=Europe/Vienna');
        console.log("Fetched Time from API -> ", response);
        
        const data = await response.json();
        console.log("API Response Data -> ", data);
        
        console.log("Vienna Time Components from API -> ", {
          year: data.year,
          month: data.month,
          day: data.day,
          hour: data.hour,
          minute: data.minute,
          seconds: data.seconds
        });
        
        // Calculate age using Vienna time components directly from API
        let age = data.year - birthDate.getFullYear();
        const monthDiff = data.month - 1 - birthDate.getMonth(); // API month is 1-based, JS Date month is 0-based
        
        if (monthDiff < 0 || (monthDiff === 0 && data.day < birthDate.getDate())) {
          age--;
        }
        
        console.log("Calculated Age using Vienna API data -> ", age);
        
        if (!cancelled) {
          setAge(age);
          setLoading(false);
        }
        
        // Calculate milliseconds until next day in Vienna
        const currentViennaMs = (data.hour * 60 * 60 * 1000) + 
                               (data.minute * 60 * 1000) + 
                               (data.seconds * 1000) + 
                               data.milliSeconds;
        
        const msInDay = 24 * 60 * 60 * 1000;
        const msUntilMidnight = msInDay - currentViennaMs;
        
        console.log("MS until Vienna midnight (calculated from API) -> ", msUntilMidnight);
        
        timer = setTimeout(fetchViennaTimeAndCalculateAge, msUntilMidnight + 60000);
        
      } catch (e) {
        console.error("Error fetching Vienna time:", e);
        if (!cancelled) setLoading(false);
      }
    };

    fetchViennaTimeAndCalculateAge();

    return () => {
      cancelled = true;
      if (timer) clearTimeout(timer);
    };
  }, [birthDate]);

  if (loading) return <span className={className}>...</span>;
  
  return <span className={className}>{age}</span>;
};

export default AgeCalculator;