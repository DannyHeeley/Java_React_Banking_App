import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';


// Components
import Home from './components/Home';
import Login from './components/Login';
import Signup from './components/Signup';
import Dashboard from './components/Dashboard.js';
import Transactions from './components/Transactions';
import Transfers from './components/Transfers';
import Profile from './components/Profile';

function App() {
  const [currentBalance, setCurrentBalance] = useState(0);
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/dashboard" element={<Dashboard currentBalance={currentBalance} setCurrentBalance={setCurrentBalance} />} />
        <Route path="/transactions" element={<Transactions currentBalance={currentBalance} setCurrentBalance={setCurrentBalance} />} />
        <Route path="/transfers" element={<Transfers currentBalance={currentBalance} setCurrentBalance={setCurrentBalance}/>} />
        <Route path="/profile" element={<Profile />} />
      </Routes>
    </Router>
  );
}

export default App;