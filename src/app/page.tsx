import Header from '../components/Header'
import AboutSection from '../components/AboutSection'
import ExperienceSection from '../components/ExperienceSection'
import TechStackSection from '../components/TechStackSection'
import InfrastructureSection from '../components/InfrastructureSection'
import ProjectsSection from '../components/ProjectsSection'
import ContactSection from '../components/ContactSection'
import Footer from '../components/Footer'

export default function Home() {
  return (
    <div className="container">
      <Header />
      <AboutSection />
      <ExperienceSection />
      <TechStackSection />
      <InfrastructureSection />
      <ProjectsSection />
      <ContactSection />
      <Footer />
    </div>
  )
}
