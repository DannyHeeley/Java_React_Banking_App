import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';

function Login() {
  const history = useNavigate();
  const [userName, setUsername] = useState(''); // Second variable is a function used to set the value
  const [password, setPassword] = useState(''); // Second variable is a function used to set the value
  const [message, setMessage] = useState('');

  const handleLogin = async (event) => { // When form is submitted with onsubmit

    event.preventDefault();

    const response = await fetch("http://localhost:7070/login", { // Make a post request to Javalin login enpoint
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        userName: userName,
        password: password,
      })
    });

    const data = await response.json(); // Wait for a reponse from Javalin
    console.log("Backend response:", data);

    if (response.ok) {
      history('/profile');
    } else {
      setMessage("Incorrect password!");
      console.error("Login failed:", data.message);
    }
  };

  return (
    <div className="container">
      {/* Selector menu */}
      <div className="selector-menu">
        <Link to="/"><i className="fas fa-university fa-2x" title="Dashboard"></i></Link>
        <Link to="/login"><i className="fas fa-search-dollar fa-2x" title="Transactions"></i></Link>
        <Link to="/login"><i className="fas fa-paper-plane fa-2x" title="Transfer Money"></i></Link>
        <Link to="/login"><i className="fas fa-user fa-2x" title="Profile"></i></Link>
      </div>

      <div className="header-img">
        <div className="balance">
          <h2>Login</h2>
        </div>
      </div>

      {/* Login Form */}
      <form className="login-form" autocomplete="on" onSubmit={(event) => handleLogin(event)}>
        <input className='login-input' required type="text" id="userName" name="userName" value={userName} onChange={e => setUsername(e.target.value)} placeholder="Username" autocomplete="username"/>
        <input className='login-input' required type="password" id="password" name="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="Password" autocomplete="current-password"/>
        <input className="login-btn" type="submit" value="Login" />
      </form>

      <div className="message">
        <p>{message}</p>
      </div>

    </div>
  );
}

export default Login;