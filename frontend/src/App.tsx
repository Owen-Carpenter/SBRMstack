import "./styles/General.css";
import { HashRouter as Router, Route, Routes } from "react-router-dom";
import { Login } from "./pages/Login";
import { Home } from "./pages/Home";
import { Register } from "./pages/Register";
import { Paywall } from "./pages/Paywall"

function App() {
  return (
    <>
      <Router>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/Home" element={<Home />} />
          <Route path="/Login" element={<Login />} />
          <Route path="/Register" element={<Register />} />
          <Route path="/Subscribe" element={<Paywall/> } />
        </Routes>
      </Router>
    </>
  )
}

export default App;
