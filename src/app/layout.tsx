import type { Metadata } from 'next'
import './globals.css'
import ClientOverlayWrapper from '../components/ClientOverlayWrapper';

export const metadata: Metadata = {
  title: 'rattatwinko.sh',
  description: 'Developer portfolio',
}

const SCRATCH_ENABLED = false; {/* This is only used for scratching */}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <head>
        <meta name="viewport" content="width=device-width, initial-scale=1" />
      </head>
      <body>
        <div id="modal-root"></div>
        {SCRATCH_ENABLED && <ClientOverlayWrapper />}
        {children}
      </body>
    </html>
  )
}
