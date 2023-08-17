import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

function Dashboard({ currentBalance, setCurrentBalance }) {
  const [transactionType, setTransactionType] = useState('DEPOSIT'); // default to deposit
  const [amount, setAmount] = useState(0);
  const [depositWithdrawalList, setTransactions] = useState([]);

  function handleAmountChange(event) {
    setAmount(event.target.value);
  }

  useEffect(() => {
    fetch("http://localhost:7070/dashboard", {
      credentials: 'include'
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => {
        setTransactions(data.depositWithdrawalList);
        setCurrentBalance(data.currentBalance);
      })
      .catch(error => {
        console.error('Error fetching transactions:', error);
      });
  }, []);


  function handleDashboard() {
    if (transactionType == "DEPOSIT") {
      fetch("http://localhost:7070/deposit", {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          amount: amount,
          transactionType: "DEPOSIT"
        })
      })
        .then(response => response.json())
        .then(data => {

          setCurrentBalance(data.balance);

          // Create a new transaction object based on the deposit/withdrawal
          const newTransaction = {
            name: transactionType,
            value: parseFloat(amount)
          };

          // Append the new transaction to the beginning of the list
          setTransactions(prevList => [...prevList, newTransaction]);
        })
        .catch(error => {
          console.error('Error during deposit:', error);
        });
    }
    if (transactionType === "WITHDRAWAL") {
      fetch("http://localhost:7070/withdraw", {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          amount: amount,
          transactionType: "WITHDRAWAL"
        })
      })
        .then(response => response.json())
        .then(data => {

          setCurrentBalance(data.balance);

          const newTransaction = {
            name: transactionType,
            value: -parseFloat(amount)
          };

          setTransactions(prevList => [...prevList, newTransaction]);
        })
        .catch(error => {
          console.error('Error during withdrawal:', error);
        });
    }
  }


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
        <h2>Dashboard</h2>
      </div>
      <hr />

      {/* Dashboard title and balance */}
      <div className="header-img">
        <div className="balance">
          <h2>Balance: £{currentBalance}</h2>
        </div>
      </div>

      {/* Deposit & Withdrawal */}
      <div className="transaction-section">
        <div className="btn-group">
          <input className="deposit-btn" type="radio" id="deposit-btn" name="dashboard-type" value="DEPOSIT" checked={transactionType === 'DEPOSIT'} onChange={() => setTransactionType('DEPOSIT')} />
          <label className="deposit-btn" htmlFor="deposit-btn">Deposit</label>
          <input className="deposit-btn" type="radio" id="withdraw-btn" name="dashboard-type" value="WITHDRAWAL" checked={transactionType === "WITHDRAWAL"} onChange={() => setTransactionType('WITHDRAWAL')} />
          <label className="deposit-btn" htmlFor="withdraw-btn">Withdraw</label>
        </div>
        <input type="number" placeholder="Enter Amount" className="transaction-input" onChange={handleAmountChange} value={amount}></input>
        <button onClick={handleDashboard} className="submit-btn">Submit</button>
      </div>

      {/* Logout Button */}
      <div style={{ textAlign: "center", marginTop: "20px" }}>
        <a href="/" className="logout-btn"><i className="fas fa-sign-out-alt"></i> Logout</a>
      </div>

    </div>
  );
}

export default Dashboard;