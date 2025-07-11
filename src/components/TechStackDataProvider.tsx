'use client'
import React, { useEffect, useState } from 'react';

{/*
  @description      => TechStackDataProvider fetches and provides tech stack data from iconconfig.json to its children.
  @data_source      => /public/iconconfig.json
  @used_in          => TechStackSection
  @types            => TechCategory, TechConfig
*/}

export interface TechCategory {
  name: string;
  url: string;
  icon?: string;
}

export interface TechConfig {
  [category: string]: TechCategory[];
}

interface TechStackDataProviderProps {
  children: (data: TechConfig | null) => React.ReactNode;
}

const TechStackDataProvider: React.FC<TechStackDataProviderProps> = ({ children }) => {
  const [data, setData] = useState<TechConfig | null>(null);

  useEffect(() => {
    fetch('/config/iconconfig.json')
      .then(res => res.json())
      .then(setData)
      .catch(() => setData(null));
  }, []);

  return <>{children(data)}</>;
};

export default TechStackDataProvider; 