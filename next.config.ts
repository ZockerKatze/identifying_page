import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  // Enable static export for deployment
  output: "export",
  
  // Optional: Add if you're deploying to GitHub Pages or similar
  // basePath: process.env.NODE_ENV === 'production' ? '/your-repo-name' : '',
  
  // Configure images if you'll use Next.js Image optimization
  images: {
    unoptimized: true, // Disable if you don't need Image Optimization
  },
  
  // Webpack configuration for custom font handling
  webpack: (config) => {
    // Add rule for font files
    config.module.rules.push({
      test: /\.(woff2)$/,
      type: 'asset/resource',
      generator: {
        filename: 'static/fonts/[name][ext]',
      },
    });
    
    return config;
  },
  
  // Enable React Strict Mode for better development practices
  reactStrictMode: true,
  
  // Optional: Configure page extensions if you want to use different file types
  pageExtensions: ['tsx', 'ts', 'jsx', 'js'],
};

export default nextConfig;
