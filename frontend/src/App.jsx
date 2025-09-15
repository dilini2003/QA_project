import { useState } from "react";
import Login from "./Login";
import Signup from "./Signup";
import "./App.css";

function App() {
  const [showLogin, setShowLogin] = useState(true);

  return (
    <div className="app">
      <h1>User Authentication</h1>
      <div>
        <button onClick={() => setShowLogin(true)}>Login</button>
        <button onClick={() => setShowLogin(false)}>Signup</button>
      </div>

      {showLogin ? <Login /> : <Signup />}
    </div>
  );
}

export default App;
