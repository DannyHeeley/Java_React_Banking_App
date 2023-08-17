import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

function Transactions({currentBalance, setCurrentBalance}) {
  const [transactions, setTransactions] = useState([]);

  useEffect(() => {
    fetch("http://localhost:7070/transactions", {
      credentials: 'include'
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => {
        setTransactions(data.transactions);
        setCurrentBalance(data.currentBalance);
      })
      .catch(error => {
        console.error('Error fetching transactions:', error);
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
        <h2>Transactions</h2>
      </div>
      <hr />

      {/* Account information */}
      <div className="header-img">
        <div className="balance">
          <h2>Balance: £{currentBalance}</h2>
        </div>
      </div>

      {/* Transaction list */}
      <div className="list">
        {(() => {
          let endingBalance = currentBalance;
          return [...transactions]
                .filter(transaction => transaction.value != 0)
                .reverse()
                .map((transaction, index) => {
                  const postTransactionBalance = endingBalance;
                  if (transaction.name === "DEPOSIT" || transaction.name === "TRANSFER_IN") {
                    endingBalance -= transaction.value;  // This subtracts because the list is being displayed in reverse
                  } else if (transaction.name === "WITHDRAWAL" || transaction.name === "TRANSFER_OUT") {
                    endingBalance += transaction.value;  // This adds because the list is being displayed in reverse
                  } return (
                    <div className="item" key={index}>
                      <a href="#"><h4>{transaction.name}</h4></a>
                      <p>£{transaction.value}</p>
                      <p>Balance:</p>
                      <p>£{postTransactionBalance}</p>
                    </div>
                  );
                });
        })()}
      </div>

      <div style={{ textAlign: "center", marginTop: "20px" }}>
        <a href="/" className="logout-btn"><i className="fas fa-sign-out-alt"></i> Logout</a>
      </div>
    </div>

  );
}

export default Transactions;
