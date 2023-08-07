function show(formId) {
  ['create', 'deposit', 'withdraw', 'display'].forEach(id => {
    document.getElementById(id).style.display = id === formId ? 'block' : 'none';
  });
}

function createAccount() {
  const username = document.getElementById('username-create').value;
  const accountType = document.getElementById('account-type').value;

  // Implement logic to create an account using the backend API
  console.log(`Creating ${accountType} for ${username}`);
}

function deposit() {
  const username = document.getElementById('username-deposit').value;
  const amount = document.getElementById('amount-deposit').value;

  // Implement logic to deposit into an account using the backend API
  console.log(`Depositing ${amount} to ${username}'s account`);
}

function withdraw() {
  const username = document.getElementById('username-withdraw').value;
  const amount = document.getElementById('amount-withdraw').value;

  // Implement logic to withdraw from an account using the backend API
  console.log(`Withdrawing ${amount} from ${username}'s account`);
}

function displayAccountInfo() {
  const username = document.getElementById('username-display').value;

  // Implement logic to fetch account info using the backend API
  document.getElementById('account-info').innerText = `Account details for ${username} ...`;
}

function fetchAccountBalance() {
  // Use fetch to get account balance
  fetch("/api/account/balance") // replace with the endpoint URL
    .then(response => response.json())
    .then(data => {
      // assuming the server returns a JSON object like { balance: 350.49 }
      document.querySelector('.balance h2').textContent = "Balance: £" + data.balance;
    })
    .catch(error => {
      console.error('There was an error fetching the account balance:', error);
    });
}

// Call the function to update the balance on page load
fetchAccountBalance();
