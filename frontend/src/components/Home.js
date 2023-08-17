import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

function Home() {
  return (
    <div className="container">
      {/* Selector menu */}
      <div className="selector-menu">
        <Link to="/"><i className="fas fa-university fa-2x" title="Dashboard"></i></Link>
        <Link to="/login"><i className="fas fa-search-dollar fa-2x" title="Transactions"></i></Link>
        <Link to="/login"><i className="fas fa-paper-plane fa-2x" title="Transfer Money"></i></Link>
        <Link to="/login"><i className="fas fa-user fa-2x" title="Profile"></i></Link>
      </div>

      {/* Account information */}
      <div className="header-img">
        <div className="balance">
          <h2>CA$H MONEY BANKING</h2>
        </div>
      </div>

      {/* Login & Signup */}
      <div className="login-section">
        <div className="btn-group">
          <Link to="/login"><button id="login-btn" className="home-btn">Login</button></Link>
          <Link to="/signup"><button id="sign-up-btn" className="home-btn">Sign Up</button></Link>
        </div>
      </div>

    </div>
  );
}

export default Home;
