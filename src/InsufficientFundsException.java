//InsufficientFundsException.java by Shimer, Jacob D.
//Simple exception if the account does not have enough funds

public class InsufficientFundsException extends Exception {
    InsufficientFundsException(String s) {
        super(s);
    }
}
