
## Part 1 Description and Functional Requirements:

#### Leveraging Java 8, create an application that simulates simple banking transactions

- Build the application using Java 8.
- All interaction with the user should be done through the console using the Scanner class.
- Customers of the bank should be able to register with a username and password, and apply to open an account.
- Customers should be able to apply for joint accounts.
- Once the account is open, customers should be able to withdraw, deposit, and transfer funds between accounts.
- All basic validation should be done, such as trying to input negative amounts, overdrawing from accounts etc.
- Employees of the bank should be able to view all of their customers information. This includes:
  - Account information
  - Account balances
  - Personal information
- Employees should be able to approve/deny open applications for accounts.
- Bank admins should be able to view and edit all accounts which includes:
  - Approving/denying accounts
  - Withdrawing, depositing, transferring from all accounts
  - Canceling accounts
  - All information should be persisted using text files and serialization
- 100% test coverage is expected using JUnit.
- Following TDD model.
- Logging is to be accomplished using Log4J.
- All transactions should be logged.

## Part 2 Requirements:

- Create an SQL script that will create a user in an SQL database and a table schema for storing your bank users and account information.
- Your database should include at least 1 stored procedure.
- Have your bank application connect to your SQL database using JDBC and store all information that way.'
- You should use the DAO design pattern for data connectivity.
