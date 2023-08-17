import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';

function Signup() {
  const history = useNavigate();
  const [userName, setUsername] = useState(''); // Second variable is a function used to set the value
  const [password, setPassword] = useState('');
  const [initialDeposit, setInitialDeposit] = useState('');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [accountType, setAccountType] = useState('ADULT');
  const [email, setEmail] = useState('');
  const [dateOfBirth, setDateOfBirth] = useState('');

  const handleSignUp = async (event) => { // When form is submitted with onsubmit

    event.preventDefault();

    console.log("Sending request with userName:", userName, "and password:", password,
      "and initialDeposit:", initialDeposit, "and firstName", firstName, "and lastName:", lastName,
      "and accountType:", accountType, "and email:", email, "and dateOfBirth:", dateOfBirth);

    const response = await fetch("http://localhost:7070/signup", { // Make a post request to Javalin login enpoint
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ // Containing sign up details
        userName: userName,
        password: password,
        initialDeposit: initialDeposit,
        firstName: firstName,
        lastName: lastName,
        accountType: accountType,
        email: email,
        dateOfBirth: dateOfBirth
      }),
      credentials: 'include'
    });

    const data = await response.json(); // Wait for a reponse from Javalin

    if (response.ok) { // Redirect to profile page using react-router
      history('/profile'); 
  } else {
      console.error("Sign up failed:", data.message);
  }
  };

  const handleInputChange = (event) => {
    const { name, value } = event.target;

    switch (name) {
        case 'userName':
            setUsername(value);
            break;
        case 'password':
            setPassword(value);
            break;
        case 'initialDeposit':
            setInitialDeposit(value);
            break;
        case 'firstName':
            setFirstName(value);
            break;
        case 'lastName':
            setLastName(value);
            break;
        case 'dateOfBirth':
            setDateOfBirth(value);
            break;
        case 'email':
            setEmail(value);
            break;
        case 'account-type':
            setAccountType(value);
            break;
        default:
            break;
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


      {/* Header */}
      <div className="header-img">
        <div className="balance">
          <h2>Sign Up</h2>
        </div>
      </div>

      {/* Account creation form */}
      <div>
        <form className="sign-up-form" autocomplete="on" onSubmit={(event) => handleSignUp(event)} method="post">
          <div className="btn-group">
            <input className="signup-btn" type="radio" id="adult-account" name="account-type" value="ADULT" onChange={handleInputChange} checked={accountType === 'ADULT'} />
            <label className="signup-btn" htmlFor="adult-account">Adult Account</label>
            <input className="signup-btn" type="radio" id="student-account" name="account-type" value="STUDENT" onChange={handleInputChange} checked={accountType === 'STUDENT'} />
            <label className="signup-btn" htmlFor="student-account">Student Account</label>
          </div>
          <input required className="form-item" type="text" name="userName" value={userName} placeholder="Username" onChange={handleInputChange} autocomplete='username'/>
          <input required className="form-item" type="password" name="password" value={password} placeholder="Password" onChange={handleInputChange} autoComplete='new-password'/>
          <input required className="form-item" type="text" name="initialDeposit" value={initialDeposit} placeholder="Initial Deposit" onChange={handleInputChange} autoComplete="off"/>
          <input required className="form-item" type="text" name="firstName" value={firstName} placeholder="First Name" onChange={handleInputChange} autoComplete='given-name' />
          <input required className="form-item" type="text" name="lastName" value={lastName} placeholder="Last Name" onChange={handleInputChange} autoComplete='family-name'/>
          <input required className="form-item" type="date" name="dateOfBirth" value={dateOfBirth} placeholder="Date of Birth" onChange={handleInputChange} autoComplete='bday'/>
          <input required className="form-item" type="email" name="email" value={email} placeholder="Email" onChange={handleInputChange} autoComplete='email'/>
          <input className="sign-up-btn" type="submit" value="Create Account" />
        </form>
      </div>
    </div>
  );
}

export default Signup;
