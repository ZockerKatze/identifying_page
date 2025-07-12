'use client'
import React, { useState, useEffect } from 'react';

{/*
  @description   => AgeCalculator component that calculates and displays current age based on birth date.
  @used_components => React hooks (useState, useEffect)
*/}

interface AgeCalculatorProps {
  birthDate: Date;
  className?: string;
}

const AgeCalculator: React.FC<AgeCalculatorProps> = ({ birthDate, className = '' }) => {
  const [age, setAge] = useState<number>(0);

  useEffect(() => {
    const calculateAge = () => {
      const today = new Date();
      let age = today.getFullYear() - birthDate.getFullYear();
      const monthDiff = today.getMonth() - birthDate.getMonth();
      
      // If birthday hasnt occurred yet this year subtract 1 from age
      if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--;
      }
      
      setAge(age);
    };

    calculateAge();
    
    // Update age once per day at midnight
    const now = new Date();
    const tomorrow = new Date(now.getFullYear(), now.getMonth(), now.getDate() + 1);
    const timeUntilMidnight = tomorrow.getTime() - now.getTime();
    
    const timer = setTimeout(calculateAge, timeUntilMidnight);
    
    return () => clearTimeout(timer);
  }, [birthDate]);

  return <span className={className}>{age}</span>;
};

export default AgeCalculator; 