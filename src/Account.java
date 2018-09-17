//Account.java by Shimer, Jacob D.
//This is where the account is initialised

class Account{
    //Create the balance and numOfTransactions variables
    private double balance = 0;
    private int numOfTransactions = 0;

    Account(double balance) {
            this.balance = balance;
    }

    //Getter and Setter for balance
    double getBalance() {
        return balance;
    }

    private void setBalance(double balance) {
        this.balance = balance;
    }

    //Method for withdrawing funds, it throws the InsufficientFundsException
    void withdraw(double amount) throws InsufficientFundsException{
        //Check if the number of transactions is less then 4, if it is it continues with the program like normal, if it
        //isn't then it continues with the program and add a $1.50 surcharge
        if (numOfTransactions < 4) {
            if (amount > getBalance()) {
                throw new InsufficientFundsException("Insufficient Funds: Your account only has " + String.format("%.2f",getBalance()));
            } else {
                setBalance(getBalance() - amount);
                numOfTransactions++;
            }
        } else {
            if ((amount + 1.5) > getBalance()) {
                throw new InsufficientFundsException("Insufficient Funds: Your account only has " + String.format("%.2f",getBalance()));
            } else {
                setBalance(getBalance() - (amount + 1.5));
                numOfTransactions++;
            }
        }
    }
    //Method for depositing
    void deposit(double amount) {
        setBalance(getBalance() + amount);
    }

    //Method for transferring money.  It calls the withdraw for the amount given
    void transferTo(double amount) throws InsufficientFundsException{
        withdraw(amount);
    }

}
