import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

function Profile() {
  const [user, setUser] = useState({});

  useEffect(() => {
    fetch("http://localhost:7070/profile", {
        credentials: 'include'
    })
    .then(response => response.json())
    .then(data => {
        setUser(data);
    });
}, []);

  return (
    <div className="container">
      {/* Selector menu */}
      <div className="selector-menu">
        <Link to="/dashboard"><i className="fas fa-university fa-2x" title="Dashboard"></i></Link>
        <Link to="/transactions"><i className="fas fa-search-dollar fa-2x" title="Transactions"></i></Link>
        <Link to="/transfers"><i className="fas fa-paper-plane fa-2x" title="Transfer Money"></i></Link>
        <Link to="/profile"><i className="fas fa-user fa-2x" title="Profile"></i></Link>
      </div>

      <hr />
      <div>
        <h2>Profile</h2>
      </div>
      <hr />

      {/* User greeting */}
      <div className="header-img">
        <div className="balance">
          <h2>Hello {user.userName}</h2>
        </div>
      </div>

      {/* Account details */}
      <div className="list">
        <div className="item">
          <h4>AccountID:</h4>
          <p>{ user.accountId }</p>
        </div>
        <div className="item">
          <h4>CustomerID:</h4>
          <p>{ user.customerId }</p>
        </div>
        <div className="item">
          <h4>Account Number:</h4>
          <p>{ user.accountNumber }</p>
        </div>
        <div className="item">
          <h4>Account Type:</h4>
          <p>{ user.accountType }</p>
        </div>
        <div className="item">
          <h4>Created:</h4>
          <p>{ user.dateCreated }</p>
        </div>
        <div className="item">
          <h4>Last Updated:</h4>
          <p>{ user.dateUpdated } | { user.timeUpdated }</p>
        </div>
      </div>


      <div style={{textAlign: "center", marginTop: "20px"}}>
        <a href="/" className="logout-btn"><i className="fas fa-sign-out-alt"></i> Logout</a>
      </div>

    </div>
  );
}

export default Profile;