import type { Metadata } from 'next'
import './globals.css'
import BirthdayConfetti from '../components/BirthdayConfetti';

export const metadata: Metadata = {
  title: 'devprofile.sh',
  description: 'Developer portfolio',
}


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
        <BirthdayConfetti birthDate={new Date(2010, 2, 9)} />
        {children}
      </body>
    </html>
  )
}
