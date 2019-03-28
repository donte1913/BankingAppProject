import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class BankingAppTest {
    Customer cu;
    jdbConnector connector;


    @Test //Tests if customers can be created and stored in the DB with the given params
    public void createCustomerTest() {
        Customer cust = new Customer("", "", "", "");
    }


    @Test  //Create connection to DB and test connection to the BankingApp DB
    public void connectToDbTest() {
        connector = new jdbConnector();

        try {
            connector.db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Test //Test customer registration
    public void registerCustomerTest() {
        connector = new jdbConnector();
        File file = new File("C:\\Users\\Owner\\Desktop\\BankingApp\\src\\main\\resources\\register");
        Scanner read = null;
        BankingApp.setUserInput(2);

        try {
            read = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(connector.checkUsername(connector.db, read, "register"), 1, 0);

        try {
            connector.db.close();
            read.close();
        } catch (SQLException e) {
            e.getMessage();
        } catch (Exception d) {
            d.printStackTrace();
        }

    }


    @Test //Test customer login
    public void loginCustomerTest() {
        connector = new jdbConnector();
        File file = new File("C:\\Users\\Owner\\Desktop\\BankingApp\\src\\main\\resources\\login");
        Scanner read = null;
        try {
            read = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(connector.checkUsername(connector.db, read, "test"), 0, 0);

        try {
            connector.db.close();
            read.close();
        } catch (SQLException e) {
            e.getMessage();
        } catch (Exception d) {
            d.printStackTrace();
        }

    }


    @Test  //Test the customer insert stored procedure method
    public void insertCustomerTest() {
        cu = new Customer("A", "B", "C", "...D");
        connector = new jdbConnector();
        connector.insertCustomer(cu, connector.db);

        try {
            connector.db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Test //Test to see if customers can apply for a individual account
    public void createIndividualAccountTest() {
        connector = new jdbConnector();
        Account a = new Account(0.0, "test", false, Account.AccountType.SAVINGS, "");
        connector.insertIndividualAccount(connector.db, a);

        try {
            connector.db.close();
        } catch (SQLException e) {
            e.getMessage();
        }
    }


    @Test //Test to see if customers can apply for a joint account
    public void createJointAccountTest() {
        connector = new jdbConnector();
        Account a = new Account(0.0, "test2", true, Account.AccountType.CHECKING, "jointAccountUserTest");
        connector.insertJointAccount(connector.db, a);

        try {
            connector.db.close();
        } catch (SQLException e) {
            e.getMessage();
        }
    }


    @Test //Test if customers can withdraw from open accounts
    public void withdrawTest() {
        Account a = new Account(4.0, "test", true, Account.AccountType.CHECKING, "test1");
        Account b = new Account(0.0, "test", false, Account.AccountType.CHECKING, "");

        int amt = 2; //withdraw amount
        double currentBal = 0; // current account balance

        connector = new jdbConnector();
        a.setAccountID(5);
        currentBal = a.getAccountBalance();
        a.setAccountBalance(currentBal - amt);
        connector.updateAccount(connector.db, a, b, 1);

        assertEquals(a.getAccountBalance(), 2, 0);

        try {
            connector.db.close();
        } catch (SQLException e) {
            e.getMessage();
        }
    }


    @Test //Test if customers can deposit from open accounts
    public void depositTest() {
        Account a = new Account(4.0, "test", true, Account.AccountType.CHECKING, "test1");
        Account b = new Account(0.0, "test", false, Account.AccountType.CHECKING, "");

        int amt = 2; //deposit amount
        double currentBal = 0; // current account balance

        connector = new jdbConnector();
        a.setAccountID(5);
        currentBal = a.getAccountBalance();
        a.setAccountBalance(currentBal + amt);
        connector.updateAccount(connector.db, a, b, 2);

        assertEquals(a.getAccountBalance(), 6, 0);

        try {
            connector.db.close();
        } catch (SQLException e) {
            e.getMessage();
        }
    }


    @Test //Test if customers can transfer from open accounts
    public void transferTest() {
        Account a = new Account(5.0, "test", true, Account.AccountType.CHECKING, "test1");
        Account b = new Account(0.0, "test", false, Account.AccountType.CHECKING, "");

        int amt = 5; //transfer amount
        double currentBal = 0; // current account balance

        connector = new jdbConnector();
        a.setAccountID(5);
        currentBal = a.getAccountBalance();
        a.setAccountBalance(currentBal - amt);
        currentBal = b.getAccountBalance();
        b.setAccountBalance(currentBal + amt);
        connector.updateAccount(connector.db, a, b, 3);

        assertEquals(a.getAccountBalance(), 0, 0);
        assertEquals(b.getAccountBalance(), 5, 0);

        try {
            connector.db.close();
        } catch (SQLException e) {
            e.getMessage();
        }
    }


    @Test // Tests if members of the bank are able to view their customers information
    public void viewCustomerInformationTest() {
        connector = new jdbConnector();
        connector.viewCustomerInformation();

        try {
            connector.db.close();
        } catch (SQLException e) {
            e.getMessage();
        }
    }


    @Test //Tests if members of the bank are able to approve account applications
    public void approveAccountTest() {
        Scanner read = null;
        Account a = new Account(0.0, "test2", true, Account.AccountType.CHECKING, "jointAccountUserTest");
        File file = new File("C:\\Users\\Owner\\Desktop\\BankingApp\\src\\main\\resources\\approveAccount");
        a.setAccountID(1);
        connector = new jdbConnector();

        try {
            read = new Scanner(file);
            connector.approveDenyOpenApplications(read);
            connector.db.close();
            read.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.getMessage();
        }
    }


    @Test //Tests if members of the bank are able to deny account applications
    public void denyAccountTest() {
        Scanner read = null;
        Account a = new Account(0.0, "test2", true, Account.AccountType.CHECKING, "jointAccountUserTest");
        File file = new File("C:\\Users\\Owner\\Desktop\\BankingApp\\src\\main\\resources\\denyAccount");
        a.setAccountID(1);
        connector = new jdbConnector();
        try {
            read = new Scanner(file);
            connector.approveDenyOpenApplications(read);
            connector.db.close();
            read.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.getMessage();
        }
    }


    @Test //Tests if bank admins can cancel accounts
    public void cancelAccountTest() {
        Scanner read = null;
        Account a = new Account(0.0, "test2", true, Account.AccountType.CHECKING, "jointAccountUserTest");
        File file = new File("C:\\Users\\Owner\\Desktop\\BankingApp\\src\\main\\resources\\cancelAccount");
        a.setAccountID(1);
        connector = new jdbConnector();
        try {
            read = new Scanner(file);
            connector.cancelOpenAccounts(connector.db, read);
            connector.db.close();
            read.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }


    @Test //Requests garbage collection for the early stages of setting up db connections
    public void grabageCollectionTest() {
        System.gc();
    }


}//EoC