'use client'
import React, { useEffect, useCallback } from 'react';
import confetti from 'canvas-confetti';

{/*
  @description   => BirthdayConfetti component that detects birthdays and triggers confetti animation.
  @used_components => canvas-confetti
  @debug_function => simulate_birthday() - Call this in browser console to test

*/}

interface BirthdayConfettiProps {
  birthDate: Date;
}

const BirthdayConfetti: React.FC<BirthdayConfettiProps> = ({ birthDate }) => {
  const [isBirthday, setIsBirthday] = React.useState(false);

  const triggerConfetti = useCallback(() => {
    // Create the Confetti. Yeeeeeeeee boi
    const count = 200;
    const defaults = {
      origin: { y: 0.7 }
    };

    function fire(particleRatio: number, opts: confetti.Options) {
      confetti({
        ...defaults,
        ...opts,
        particleCount: Math.floor(count * particleRatio)
      });
    }

    fire(0.25, {
      spread: 26,
      startVelocity: 55,
    });

    fire(0.2, {
      spread: 60,
    });

    fire(0.35, {
      spread: 100,
      decay: 0.91,
      scalar: 0.8
    });

    fire(0.1, {
      spread: 120,
      startVelocity: 25,
      decay: 0.92,
      scalar: 1.2
    });

    fire(0.1, {
      spread: 120,
      startVelocity: 45,
    });
  }, []);

  const checkBirthday = useCallback(() => {
    const today = new Date();
    const isToday = 
      today.getMonth() === birthDate.getMonth() && 
      today.getDate() === birthDate.getDate();
    
    setIsBirthday(isToday);
    
    if (isToday) {
      triggerConfetti();
    }
  }, [birthDate, triggerConfetti]);

  useEffect(() => {
    checkBirthday();
    
    // Check once per day at midnight
    const now = new Date();
    const tomorrow = new Date(now.getFullYear(), now.getMonth(), now.getDate() + 1);
    const timeUntilMidnight = tomorrow.getTime() - now.getTime();
    
    const timer = setTimeout(checkBirthday, timeUntilMidnight);
    
    return () => clearTimeout(timer);
  }, [birthDate, checkBirthday]);

  // Expose debug function globally for testing birthday effects
  useEffect(() => {
    (window as Window & { simulate_birthday?: () => void }).simulate_birthday = () => {
      setIsBirthday(true);
      triggerConfetti();
    };
    
    return () => {
      delete (window as Window & { simulate_birthday?: () => void }).simulate_birthday;
    };
  }, [triggerConfetti]);

  // Only render birthday message if it actually the birthday
  if (!isBirthday) return null;

  return (
    <div
      style={{
        position: 'fixed',
        top: '20px',
        right: '20px',
        background: 'linear-gradient(135deg, #ff6b6b, #4ecdc4, #45b7d1, #96ceb4, #feca57)',
        color: 'white',
        padding: '12px 20px',
        borderRadius: '25px',
        fontSize: '16px',
        fontWeight: 'bold',
        boxShadow: '0 4px 15px rgba(0,0,0,0.3)',
        zIndex: 10001,
        animation: 'bounce 2s infinite',
        textAlign: 'center',
        minWidth: '200px'
      }}
    >
      Today&apos;s my birthday!
    </div>
  );
};

export default BirthdayConfetti; 