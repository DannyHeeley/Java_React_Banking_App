import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

function Transfers({ currentBalance, setCurrentBalance }) {
  const [transactionType, setTransactionType] = useState('TRANSFER_OUT');
  const [transfers, setTransfers] = useState([]);
  const [amount, setAmount] = useState(0);
  const [accNum, setAccNum] = useState(0);
  const [message, setMessage] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  function handleAmountChange(event) {
    setAmount(event.target.value);
  }

  function handleAccNumChange(event) {
    setAccNum(event.target.value);
  }

  useEffect(() => {
    setIsLoading(true);
    fetch("http://localhost:7070/transferList", {credentials: 'include'})
      .then(response => {
        if (!response.ok) {throw new Error('Network response was not ok');}
        return response.json();
      })
      .then(data => {
        if (!data.transfers || data.transfers.length === 0) {
          setMessage('Nothing here.');
        }
        setCurrentBalance(data.currentBalance);
        setTransfers(data.transfers);
        setIsLoading(false);
      })
      .catch(error => {
        console.error('Error fetching transactions:', error);
        setMessage('Error fetching transactions.');
        setIsLoading(false);
      });
  }, []);

  function handleTransfer() {
    setIsLoading(true);
    setMessage('Processing transfer...');

    const enteredAmount = parseFloat(amount);

    if (enteredAmount <= 0) {
        setMessage('Please enter a valid positive amount.');
        return;
    }

    if (enteredAmount > currentBalance) {
        setMessage('Insufficient funds for the transfer.');
        return;
    }

    fetch("http://localhost:7070/transfer", {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        transferAmount: parseFloat(amount),
        transactionType: "TRANSFER_OUT",
        accountNumber: parseInt(accNum, 10)
      })
    })
      .then(response => response.json())
      .then(data => {
        setIsLoading(false);
        setCurrentBalance(data.balance);

        const newTransfer = {
          transactionType: transactionType,
          value: parseFloat(amount),
          accountNumber: parseInt(accNum, 10)
        };

        setTransfers(prevList => [...prevList, newTransfer]);
        setMessage('Transfer processed successfully.');
        setAmount(0);
        setAccNum(0);
      })
      .catch(error => {
        setIsLoading(false);
        console.error('Error during withdrawal:', error);
        setMessage('Error during withdrawal.');
      });
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
        <h2>Transfers</h2>
      </div>
      <hr />

      {/* Account information */}
      <div className="header-img">
        <div className="balance">
          <h2>Balance: £{currentBalance}</h2>
        </div>
      </div>

      {/* Transfer section */}
      <div className="transfer-section">
        <h5 className='transfer-tag'>Account Number:</h5>
        <input onChange={handleAccNumChange} value={accNum} type="number" placeholder="Enter Account Number" className="transfer-input" />
        <h5 className='transfer-tag'>Amount:</h5>
        <input onChange={handleAmountChange} value={amount} type="number" placeholder="Enter Amount" className="transfer-input" />
        <button onClick={handleTransfer} className="submit-btn">Submit Transfer</button>
      </div>

      {isLoading && <div className="loading-spinner">Loading...</div>}
      <div className="message">
        <p>{message}</p>
      </div>

      <div style={{ textAlign: "center", marginTop: "20px" }}>
        <a href="/" className="logout-btn"><i className="fas fa-sign-out-alt"></i> Logout</a>
      </div>
    </div>
  );
}

export default Transfers;
