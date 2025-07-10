import type { Metadata } from 'next'
import './globals.css'

export const metadata: Metadata = {
  title: 'rattatwinko.sh',
  description: 'Developer portfolio',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body>
        <div id="modal-root"></div>
        {children}
      </body>
    </html>
  )
}
