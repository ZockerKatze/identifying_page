'use client'
import React, { useEffect, useCallback, useState } from 'react';
import confetti from 'canvas-confetti';
import AgeCalculator from './AgeCalculator';

/*
* @description => BirthdayConfetti component that detects birthdays using Vienna timezone and triggers confetti animation.
* @used_components => canvas-confetti, AgeCalculator component
* @debug_function => simulate_birthday() - Call this in browser console to test
*/

interface BirthdayConfettiProps {
  birthDate: Date;
}

const BirthdayConfetti: React.FC<BirthdayConfettiProps> = ({ birthDate }) => {
  const [isBirthday, setIsBirthday] = useState(false);

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

  const checkBirthdayWithViennaTime = useCallback(async () => {
    try {
      // Use Vienna time API to check birthday - same logic as AgeCalculator
      const response = await fetch('https://timeapi.io/api/Time/current/zone?timeZone=Europe/Vienna');
      const data = await response.json();
      
      console.log("Vienna Time API Response for Birthday Check -> ", data);
      
      // Check if today (in Vienna) is the birthday
      const isToday = 
        data.month === (birthDate.getMonth() + 1) && // API month is 1-based, JS Date month is 0-based
        data.day === birthDate.getDate();
      
      console.log("Birthday check:", {
        viennaMonth: data.month,
        viennaDay: data.day,
        birthMonth: birthDate.getMonth() + 1,
        birthDay: birthDate.getDate(),
        isBirthday: isToday
      });
      
      setIsBirthday(isToday);
      
      if (isToday) {
        triggerConfetti();
      }
      
      // Calculate milliseconds until next day in Vienna - same logic as AgeCalculator
      const currentViennaMs = (data.hour * 60 * 60 * 1000) + 
                             (data.minute * 60 * 1000) + 
                             (data.seconds * 1000) + 
                             data.milliSeconds;
      
      const msInDay = 24 * 60 * 60 * 1000;
      const msUntilMidnight = msInDay - currentViennaMs;
      
      // Schedule next check at Vienna midnight (with 1 minute buffer)
      setTimeout(checkBirthdayWithViennaTime, msUntilMidnight + 60000);
      
    } catch (error) {
      console.error("Error checking birthday with Vienna time:", error);
    }
  }, [birthDate, triggerConfetti]);

  useEffect(() => {
    checkBirthdayWithViennaTime();
  }, [checkBirthdayWithViennaTime]);

  // Expose debug function globally for testing birthday effects
  useEffect(() => {
    (window as Window & { simulate_birthday?: () => void }).simulate_birthday = () => {
      setIsBirthday(true);
      triggerConfetti();
      setTimeout(() => setIsBirthday(false), 10000);
    };

    return () => {
      delete (window as Window & { simulate_birthday?: () => void }).simulate_birthday;
    };
  }, [triggerConfetti]);

  return (
    <>
      {/* debug statement ; works ; removed */}
      
      {/*<div className="fixed top-4 left-4 bg-blue-500 text-white px-3 py-1 rounded-full font-bold text-sm z-10000">
        Age: <AgeCalculator birthDate={birthDate} />
      </div>*/}
      
      {/* Birthday message and confetti trigger */}
      {isBirthday && (
        <>
          <style jsx>{`
            @keyframes bounce {
              0%, 20%, 50%, 80%, 100% {
                transform: translateY(0);
              }
              40% {
                transform: translateY(-10px);
              }
              60% {
                transform: translateY(-5px);
              }
            }
          `}</style>
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
            Today&apos;s my birthday! 🎉
          </div>
        </>
      )}
    </>
  );
};

export default BirthdayConfetti;