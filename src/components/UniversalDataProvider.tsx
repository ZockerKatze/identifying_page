import React, { useEffect, useState } from 'react';

interface UniversalDataProviderProps {
  configPath: string; // e.g., '/config/iconconfig.json'
  children: (data: unknown | null) => React.ReactNode;
}

const UniversalDataProvider: React.FC<UniversalDataProviderProps> = ({ configPath, children }) => {
  // Use unknown for generic JSON data
  const [data, setData] = useState<unknown | null>(null);

  useEffect(() => {
    const basePath = process.env.NEXT_PUBLIC_BASE_PATH || '';
    fetch(`${basePath}${configPath}`)
      .then(res => res.json())
      .then(setData)
      .catch(() => setData(null));
  }, [configPath]);

  return <>{children(data)}</>;
};

export default UniversalDataProvider; 