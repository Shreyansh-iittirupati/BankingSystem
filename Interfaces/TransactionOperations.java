package Interfaces;

public interface TransactionOperations {
    void depositFunds(String accountNumber, double amount);

    void withdrawFunds(String accountNumber, double amount);

    void transferFunds(String fromAccount, String toAccount, double amount);
}

