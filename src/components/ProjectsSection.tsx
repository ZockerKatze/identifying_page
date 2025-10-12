'use client';
import React, { useEffect, useState } from 'react';
import ProjectCard from './ProjectCard';

interface RssItem {
  title: string;
  link: string;
  description: string;
  pubDate: string;
}

const PROJECTS_PER_PAGE = 3;

const ProjectsSection: React.FC = () => {
  const [rssItems, setRssItems] = useState<RssItem[]>([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchRss = async () => {
      try {
        const proxyUrl = 'https://api.allorigins.win/raw?url=';
        const feedUrl = 'https://rattatwinko.servecounterstrike.com/gitea/rattatwinko.rss';
        const res = await fetch(proxyUrl + encodeURIComponent(feedUrl));
        const text = await res.text();

        const parser = new DOMParser();
        const xml = parser.parseFromString(text, 'application/xml');
        const items = Array.from(xml.querySelectorAll('item')).map((item) => ({
          title: item.querySelector('title')?.textContent || 'No title',
          link: item.querySelector('link')?.textContent || '#',
          description: item.querySelector('description')?.textContent || '',
          pubDate: item.querySelector('pubDate')?.textContent || '',
        }));

        setRssItems(items);
      } catch (err) {
        console.error('Failed to fetch RSS feed', err);
      } finally {
        setIsLoading(false);
      }
    };

    fetchRss();
  }, []);

  const startIndex = (currentPage - 1) * PROJECTS_PER_PAGE;
  const paginatedItems = rssItems.slice(startIndex, startIndex + PROJECTS_PER_PAGE);
  const totalPages = Math.ceil(rssItems.length / PROJECTS_PER_PAGE);

  // Component to render HTML description with clickable links
  const DescriptionWithLinks: React.FC<{ html: string }> = ({ html }) => {
    const parser = new DOMParser();
    const doc = parser.parseFromString(html, 'text/html');
    const elements: React.ReactNode[] = [];

    doc.body.childNodes.forEach((node, idx) => {
      if (node.nodeType === Node.TEXT_NODE) {
        const text = node.textContent?.trim();
        if (text) elements.push(text + ' ');
      } else if (node.nodeName === 'A') {
        const anchor = node as HTMLAnchorElement;
        elements.push(
          <a
            key={idx}
            href={anchor.href}
            target="_blank"
            rel="noopener noreferrer"
            className="project-inline-link"
          >
            {anchor.textContent}
          </a>
        );
        elements.push(' ');
      } else {
        const text = node.textContent?.trim();
        if (text) elements.push(text + ' ');
      }
    });

    return <div className="mt-2">{elements}</div>;
  };

  // Extract plain text for the description prop
  const extractPlainText = (html: string): string => {
    const parser = new DOMParser();
    const doc = parser.parseFromString(html, 'text/html');
    const textParts: string[] = [];
    doc.body.childNodes.forEach((node) => {
      if (node.nodeType === Node.TEXT_NODE) {
        const text = node.textContent?.trim();
        if (text) textParts.push(text);
      }
    });
    return textParts.join(' ') || 'No description';
  };

  // Extract plain text from title (remove HTML tags)
  const extractTitleText = (html: string): string => {
    const parser = new DOMParser();
    const doc = parser.parseFromString(html, 'text/html');
    let text = doc.body.textContent || html;
    
    // Remove git hashes (40 hex chars) from title
    text = text.replace(/\b[0-9a-f]{40}\b/gi, '');
    
    return text.trim();
  };

  // Extract git hash from title
  const extractGitHash = (html: string): string | null => {
    const parser = new DOMParser();
    const doc = parser.parseFromString(html, 'text/html');
    const text = doc.body.textContent || html;
    
    // Find git hash (40 hex chars)
    const match = text.match(/\b[0-9a-f]{40}\b/i);
    return match ? match[0].substring(0, 7) : null;
  };

  return (
    <section>
      <h2 className="section-title">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="16"
          height="16"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth={1.5}
          strokeLinecap="round"
          strokeLinejoin="round"
        >
          <path d="M5 12.55a11 11 0 0 1 14.08 0"></path>
          <path d="M1.42 9a16 16 0 0 1 21.16 0"></path>
          <path d="M8.53 16.11a6 6 0 0 1 6.95 0"></path>
          <line x1="12" y1="20" x2="12.01" y2="20"></line>
        </svg>
        ./projects.md
      </h2>

      <div className="section-content">
        {isLoading && (
          <p className="text-gray-500">Loading projects...</p>
        )}

        {!isLoading && rssItems.length === 0 && (
          <p className="text-gray-500">Failed to fetch RSS feed.</p>
        )}

        {!isLoading && paginatedItems.map((item, idx) => {
          const gitHash = extractGitHash(item.title);
          
          return (
            <ProjectCard
              key={idx}
              title={extractTitleText(item.title)}
              description={extractPlainText(item.description)}
              link={item.link}
              gitHash={gitHash || undefined}
              recentFocus={[`Published: ${new Date(item.pubDate).toLocaleDateString()}`]}
            >
              <DescriptionWithLinks html={item.description} />
            </ProjectCard>
          );
        })}
      </div>

      {rssItems.length > 0 && (
        <div className="pagination-controls">
          <button
            disabled={currentPage === 1}
            onClick={() => setCurrentPage((p) => p - 1)}
            className="pagination-button"
          >
            ← Prev
          </button>
          <span className="pagination-info">
            Page {currentPage} of {totalPages}
          </span>
          <button
            disabled={currentPage === totalPages}
            onClick={() => setCurrentPage((p) => p + 1)}
            className="pagination-button"
          >
            Next →
          </button>
        </div>
      )}
    </section>
  );
};

export default ProjectsSection;