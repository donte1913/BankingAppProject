import java.io.File;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.Scanner;


public class BankingApp {

    private static int userInput;
    private static Scanner read;
    private static jdbConnector connector;
    static final Logger log = Logger.getLogger(BankingApp.class);

    /*
     * Purpose:
     *   Start the applications main menu screen and overall execute
     *   Tell the JVM where the log4j.properties file is located
     * */
    public static void main(String[] args) {
        PropertyConfigurator.configure(System.getProperty("user.dir") + File.separator +
                "\\src\\main\\resources\\log4j.properties");

        //Connect to AWS DB
        connector = new jdbConnector();
        mainMenu();

        try {
            connector.db.close();
            read.close();
        } catch (SQLException e) {
            e.getMessage();
        }
    }//end of main


    /*
     * Purpose:
     *   Display main menu options when first logging in application to login as a return user
     *   or to register as a new customer
     *
     * @return void
     */
    private static void mainMenu() {
        System.out.printf("Type the number for the action, then click enter.\n" +
                "1. Login\n" +
                "2. Register new account\n" +
                "3. Exit\n");
        try {
            read = new Scanner(System.in);

            //Check if user enters a number then presses enter
            if (read.hasNextInt() && read.hasNextLine()) {
                userInput = Integer.parseInt(read.nextLine());
                switch (userInput) {
                    case 1:
                        loginMenu(read);
                        break;
                    case 2:
                        registerMenu(read);
                        break;
                    case 3:
                        System.out.println("Goodbye");
                        log.info("Application exit");
                        System.exit(0);
                    default:
                        System.out.println(userInput + " is not a valid entry, please try again.");
                        break;
                }
            } else {
                System.out.println(read.nextLine() + " is not a number, please try again.");
            }

            mainMenu();
        } catch (NumberFormatException e) {
            System.out.println("No spaces are needed, type either 1 to login " +
                    "to the banking app or 2 to register as new customer, then press enter, or 3 to exit\n");

        } catch (Exception e) {
            System.err.println("ERROR: Something went really wrong in mainMenu!");
            e.printStackTrace();
        }

        mainMenu();
    }//end of mainMenu


    /*
     * Purpose:
     *   Display the register menu options when registering users in application for the first time
     *
     *   @return int
     */
    private static void registerMenu(Scanner read) {
        Customer cu = null;
        String firstName = "";
        String lastName = "";
        String username = "";
        String password = "";

        try {
            System.out.printf("Please enter your first name: ");
            if (read.hasNextLine()) {
                firstName = read.nextLine();
                System.out.println(firstName);
            }

            System.out.printf("Please enter your last name: ");
            if (read.hasNextLine()) {
                lastName = read.nextLine();
                System.out.println(lastName);
            }

            //Checks if the username the user enters is already in the customer array list
            //here if so catch exception
            do {
                System.out.printf("Please enter your username: ");

                if (read.hasNextLine())
                    username = read.nextLine();
            } while (connector.checkUsername(connector.db, read, username) == 0);

            System.out.printf("Please enter your password: ");
            if (read.hasNextLine()) {
                password = read.nextLine();
                System.out.println(password);
            }

            cu = new Customer(firstName, lastName, username, password);

            connector.insertCustomer(cu, connector.db);
            //add the customer to the Customers table

            System.out.printf("Thank you " + "%s" + ", you're registered" +
                    " and may now login with your username and password!\n", cu.getFirstName());

            log.info("New username registration: " + cu.getFirstName());

            loginMenu(read);
        } catch (Exception e) {
            System.out.println("Something went wrong with registering you.");
            System.exit(-1);
        }

    }//end of registerMenu


    /*
     * Purpose:
     *  Display the login menu options for returning users
     *
     *
     * @return void
     * */
    private static void loginMenu(Scanner read) {
        String username = "";
        String pwd = "";

        System.out.println("Which type of user would you like to log in as?\n" +
                "1. Customer\n" +
                "2. Employee\n" +
                "3. Bank Administrator\n" +
                "4. Exit");
        try {
            //Check if user enters a number then presses enter
            if (read.hasNextInt() && read.hasNextLine()) {
                userInput = Integer.parseInt(read.nextLine());
                switch (userInput) {
                    case 1:
                        /*
                         * Searches for the customer username in the Customers table
                         *   if password not typed correctly by 3 tries, system will exit
                         */
                        do {
                            System.out.println("Press 1 to go back or please enter your username: ");
                            if (read.hasNextLine()) {
                                username = read.nextLine();//Get username
                                if (username.equals("1"))
                                    mainMenu();
                            }
                        } while (connector.checkUsername(connector.db, read, username) == -1);

                        //Customer has logged in successfully!
                        log.info("Successful login attempt by user: " + username);
                        customerMenu(read, username);
                        break;
                    case 2:
                        //TODO: Search for employee username in the employee table in DB
                        System.out.printf("Please enter your employee username: ");
                        if (read.hasNextLine()) {
                            username = read.nextLine();//Get username
                            if (username.equals("employee")) {
                                System.out.printf("Please enter your password: ");
                                if (read.hasNextLine()) {
                                    pwd = read.nextLine();
                                    if (pwd.equals("test")) {
                                        log.info("Successful login attempt by employee");
                                        employeeMenu(read, username);
                                    } else {
                                        System.out.println("Invalid password, returning to login menu");
                                        loginMenu(read);
                                    }
                                }
                            } else {
                                System.out.println("Invalid username, returning to login menu");
                                loginMenu(read);
                            }
                        }
                        break;
                    case 3:
                        //TODO: Search for bank administrator username in the bank administrator table
                        System.out.printf("Please enter your bank administrator username: ");
                        if (read.hasNextLine()) {
                            username = read.nextLine();//Get username
                            if (username.equals("admin")) {
                                System.out.printf("Please enter your password: ");
                                if (read.hasNextLine()) {
                                    pwd = read.nextLine();
                                    if (pwd.equals("test")) {
                                        log.info("Successful login attempt by bank admin");
                                        bankAdminMenu(read, username);
                                    } else {
                                        System.out.println("Invalid password, returning to login menu");
                                        loginMenu(read);
                                    }
                                }
                            } else {
                                System.out.println("Invalid username, returning to login menu");
                                loginMenu(read);
                            }
                        }
                        break;
                    case 4:
                        System.out.println("Goodbye");
                        log.info("Application exit");
                        System.exit(0);

                    default:
                        System.out.println(userInput + " is not a valid entry, please try again.");
                        break;
                }
                loginMenu(read);
            }
        } catch (NumberFormatException e) {
            System.out.println("No spaces are needed.\n\nOptions:\n" +
                    "Press 1 to login as a customer, then press enter.\n" +
                    "Press 2 to login as an employee, then press enter.\n" +
                    "Press 3 to login as a bank administrator, then press enter.\n" +
                    "Press 4 to exit, then press enter.\n");
            loginMenu(read);
        } catch (Exception e) {
            System.err.println("ERROR: Something went really wrong in loginMenu!");
            e.printStackTrace();
        }

    }//end of loginMenu


    /*
     * Purpose:
     *  Display the customer menu options for customers that have registered already with the app
     *
     * @return void
     * */
    protected static void customerMenu(Scanner read, String username) {
        //ja = joint account
        String jaFirstName = "";
        String jaLastName = "";
        String jaUsername = "";
        String jaPassword = "";

        System.out.printf("Please enter the number to perform the matching action:\n" +
                "1. Apply for a new account\n" +
                "2. View Account Applications\n" +
                "3. View/Edit Open Accounts\n" +
                "4. Logout\n");
        try {

            if (read.hasNextInt() && read.hasNextLine()) {
                userInput = Integer.parseInt(read.nextLine());
                //Customer menu option selection
                switch (userInput) {
                    case 1:
                        System.out.println("Joint account policy:\nCustomers may only apply for one joint" +
                                " checking account with one other customer.");
                        System.out.println("Which type of account would you like to apply for?\n"
                                + "1. Checking\n" +
                                "2. Savings");

                        if (read.hasNextInt() && read.hasNextLine()) {
                            userInput = Integer.parseInt(read.nextLine());
                            //Checking or Savings account type option selection
                            switch (userInput) {
                                case 1:
                                    System.out.println("Will this account be joint with another customer?\n"
                                            + "1. Yes\n2. No");

                                    if (read.hasNextInt() && read.hasNextLine()) {
                                        userInput = Integer.parseInt(read.nextLine());

                                        switch (userInput) {
                                            //Customer has selected to apply for joint checking account
                                            case 1:
                                                System.out.printf("Please enter the joint account customer's first " +
                                                        "name: ");
                                                if (read.hasNextLine()) {
                                                    jaFirstName = read.nextLine();
                                                    System.out.println(jaFirstName);
                                                }

                                                System.out.printf("Please enter the joint account customer's last " +
                                                        "name: ");
                                                if (read.hasNextLine()) {
                                                    jaLastName = read.nextLine();
                                                    System.out.println(jaLastName);
                                                }

                                                System.out.printf("Please enter the joint account customer's user " +
                                                        "name: ");
                                                if (read.hasNextLine()) {
                                                    jaUsername = read.nextLine();
                                                    System.out.println(jaUsername);
                                                }

                                                System.out.printf("Please enter the joint account customer's " +
                                                        "password: ");
                                                if (read.hasNextLine()) {
                                                    jaPassword = read.nextLine();
                                                    System.out.println(jaPassword);
                                                }
                                                //Checks for joint account customer
                                                if (connector.checkForJointAccountCustomer(connector.db, jaFirstName,
                                                        jaLastName,
                                                        jaUsername, jaPassword)) {
                                                    System.out.println("Sending joint checking account application " +
                                                            "with " + jaUsername + " for approval. " +
                                                            "You may now monitor the approval status in the \"View " +
                                                            "Account Applications\" menu.");

                                                    Account account = new Account(0.0, username, true,
                                                            Account.AccountType.CHECKING, jaUsername);

                                                    connector.insertJointAccount(connector.db, account);
                                                    log.info("Joint checking account application request sent from user " + username + " with " + jaUsername);
                                                } else
                                                    System.out.println("Could not find customer for joint account" +
                                                            " application, returning to customer menu.");

                                                break;
                                            //Individual checking account application option selection
                                            case 2:
                                                System.out.println("Sending individual checking account application for approval." +
                                                        " You may now monitor the approval status in the \"View " +
                                                        "Account Applications\" menu.");
                                                Account account = new Account(0.0, username, false,
                                                        Account.AccountType.CHECKING, "");

                                                connector.insertIndividualAccount(connector.db, account);
                                                log.info("Individual checking account application request sent from user " + username);

                                                break;
                                            default:
                                                System.out.println(userInput + " is not a valid entry, going back to " +
                                                        "customer menu..");
                                                break;
                                        }
                                        customerMenu(read, username);
                                    }

                                    //Individual savings account application option selection
                                case 2:
                                    System.out.println("Sending individual savings account application for approval." +
                                            " You may now monitor the approval status in the \"View " +
                                            "Account Applications\" menu.");
                                    Account account = new Account(0.0, username, false,
                                            Account.AccountType.SAVINGS, "");

                                    connector.insertIndividualAccount(connector.db, account);
                                    log.info("Individual savings account application request sent from user " + username);
                                    break;
                                default:
                                    System.out.println(userInput + " is not a valid entry, going back to " +
                                            "customer menu..");
                                    break;
                            }
                            customerMenu(read, username);
                        }
                    case 2:
                        //View Account Applications
                        connector.viewAccountApplications(connector.db, username);
                        System.out.print("\nYour current account applications are listed above \n\n");
                        log.info(username + " viewed their account application(s)");
                        break;
                    case 3:
                        //View/Edit Open Accounts menu for customers
                        //implements withdraw, deposit, and transferring funds in this menu
                        connector.viewEditOpenAccounts(connector.db, read, username);
                        log.info(username + " attempted to edit their open account(s)");
                        break;
                    case 4:
                        log.info("User " + username + " logged out");
                        mainMenu();
                        break;
                    default:
                        System.out.println(userInput + " is not a valid entry, please try again.");
                        break;
                }
                customerMenu(read, username);
            }
        } catch (NumberFormatException e) {
            System.out.println("No spaces are needed, options are:\n" +
                    "Press 1 to apply for a new account, then press enter.\n" +
                    "Press 2 to view your current account applications, then press enter.\n" +
                    "Press 3 to view and edit your open accounts, then press enter.\n" +
                    "Press 4 to logout, then press enter.\n");

        } catch (Exception e) {
            System.err.println("ERROR: Something went really wrong in customerMenu!");
            e.printStackTrace();
        }

        customerMenu(read, username);
    }//End of customerMenu


    //Display the options for the employee
    public static void employeeMenu(Scanner read, String username) {
        System.out.println("Hello, " + username);
        System.out.println("Please type the number for the action, then click enter.\n" +
                "1. View Customer Information\n" +
                "2. Approve/Deny Account Applications\n" +
                "3. Logout");
        try {
            //Check if user enters a number then presses enter
            if (read.hasNextInt() && read.hasNextLine()) {
                userInput = Integer.parseInt(read.nextLine());
                switch (userInput) {
                    case 1:
                        connector.viewCustomerInformation();
                        log.info("Employee viewed their customers information");
                        break;
                    case 2:
                        connector.approveDenyOpenApplications(read);
                        log.info("Employee attempted to approve/deny customer account application(s)");
                        break;
                    case 3:
                        log.info("Employee logged out");
                        mainMenu();
                        break;
                    default:
                        System.out.println(userInput + " is not a valid entry, please try again.");
                        break;
                }
                employeeMenu(read, username);
            }
        } catch (NumberFormatException e) {
            System.out.println("No spaces are needed.\n\nOptions:\n" +
                    "Press 1 to view customer information, then press enter.\n" +
                    "Press 2 to approve or deny account applications, then press enter.\n" +
                    "Press 3 to logout, then press enter.\n");

        } catch (Exception e) {
            System.err.println("ERROR: Something went really wrong in employeeMenu!");
            e.printStackTrace();
        }
        employeeMenu(read, username);

    }//End of employeeMenu

    //Display the options for the bank admin
    public static void bankAdminMenu(Scanner read, String username) {
        System.out.println("Hello, " + username);
        System.out.println("Please type the number for the action, then click enter.\n" +
                "1. View Customer Information\n" +
                "2. Approve/Deny Account Applications\n" +
                "3. View/Edit Open Accounts\n" +
                "4. Cancel Open Accounts\n" +
                "5. Logout");
        try {
            //Check if user enters a number then presses enter
            if (read.hasNextInt() && read.hasNextLine()) {
                userInput = Integer.parseInt(read.nextLine());
                switch (userInput) {
                    case 1:
                        connector.viewCustomerInformation();
                        log.info("Bank admin viewed customers information");
                        break;
                    case 2:
                        connector.approveDenyOpenApplications(read);
                        log.info("Bank admin attempted to approve/deny customer account application(s)");
                        break;
                    case 3:
                        connector.viewEditOpenAccounts(connector.db, read, username);
                        log.info("Bank admin attempted to edit customers open account(s)");
                        break;
                    case 4:
                        connector.cancelOpenAccounts(connector.db, read);
                        log.info("Bank admin attempted to cancel customer account(s) ");
                        break;
                    case 5:
                        log.info("Bank admin logged out");
                        mainMenu();
                        break;
                    default:
                        System.out.println(userInput + " is not a valid entry, please try again.");
                        break;
                }
                bankAdminMenu(read, username);
            }
        } catch (NumberFormatException e) {
            System.out.println("No spaces are needed.\n\nOptions:\n" +
                    "Press 1 to view customer information, then press enter.\n" +
                    "Press 2 to approve or deny account applications, then press enter.\n" +
                    "Press 3 to view and edit open customer accounts, then press enter.\n" +
                    "Press 4 to cancel open customer accounts, then press enter.\n" +
                    "Press 5 to logout, then press enter.");

        } catch (Exception e) {
            System.err.println("ERROR: Something went really wrong in bankAdminMenu!");
            e.printStackTrace();
        }

        bankAdminMenu(read, username);
    }//End of bank admin menu


    public static int getUserInput() {
        return userInput;
    }

    public static void setUserInput(int userInput) {
        BankingApp.userInput = userInput;
    }


}//EoC