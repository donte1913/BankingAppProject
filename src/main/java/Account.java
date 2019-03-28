public class Account{

    private static int count;
    private double accountBalance;
    private int accountID;
    private String username; //foreign key references Customer.username column
    private String jaUsername; //joint account customer username sharing the possible joint account
    private boolean jointAccount;
    public enum AccountType {CHECKING, SAVINGS}


    AccountType type;
    public enum Status {DENIED, PENDING, APPROVED}
    Status st;

    //Insert into the account table with all six fields with their correct table field types rather than just the four
    // parameters
    public Account(double accountBalance, String username, boolean jointAccount, AccountType type, String jaUsername) {
        ++count;
        this.accountBalance = accountBalance;
        this.username = username;
        this.jaUsername = jaUsername;
        this.jointAccount = jointAccount;
        this.type = type;
        this.st = Status.PENDING;
        this.accountID = count;
    }

    public int getCount() {
        return count;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJaUsername() {
        return jaUsername;
    }

    public void setJaUsername(String jaUsername) {
        this.jaUsername = jaUsername;
    }

    public boolean isJointAccount() {
        return jointAccount;
    }

    public void setJointAccount(boolean jointAccount) {
        this.jointAccount = jointAccount;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }


    public AccountType getType() {
        return type;
    }

    public Status getSt() {
        return st;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

}//End of class
