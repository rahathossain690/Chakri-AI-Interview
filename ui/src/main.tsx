import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import Chakri from './Chakri'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <Chakri/>
  </StrictMode>
)
