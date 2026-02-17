import Header from '../components/Header'
import AboutSection from '../components/AboutSection'
import ExperienceSection from '../components/ExperienceSection'
import TechStackSection from '../components/TechStackSection'
import InfrastructureSection from '../components/InfrastructureSection'
import ContactSection from '../components/ContactSection'
import Footer from '../components/Footer'
import JavaApplet from '@/components/JavaApplets'

export default function Home() {
  return (
    <div className="container">
      <JavaApplet />
      <Header />
      <AboutSection />
      <ExperienceSection />
      <TechStackSection />
      <InfrastructureSection />
      <ContactSection />
      <Footer />
    </div>
  )
}
