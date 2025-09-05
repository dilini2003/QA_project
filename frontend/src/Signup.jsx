import { useState } from "react";
import axios from "axios";

function Signup() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleSignup = async () => {
    if (!username || !email || !password) {
    setMessage("All fields are required!");
    return;
  }
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(email)) {
    setMessage("Invalid email format!");
    return;
  }
    try {
      const res = await axios.post("http://localhost:8080/api/auth/signup", {
        username,
        email,
        password,
      });
      // res.data contains JSON from backend
      setMessage(res.data.message);
    } catch (err) {
      // Show backend error message
      setMessage(err.response?.data?.message || "Signup failed!");
    }
  };

  return (
    <div style={{ marginTop: "20px" }}>
      <h2>Signup</h2>
      <input 
        type="text"
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      /><br /><br />
      <input
        type="email"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      /><br /><br />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      /><br /><br />
      <button onClick={handleSignup}>Signup</button>
      <p>{message}</p>
    </div>
  );
}

export default Signup;
