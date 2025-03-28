package Interfaces;

public interface AccountOperations {

    void updateAccount(String accountNumber, String newDetails);

    void deleteAccount(String accountNumber);

    void suspendAccount(String accountNumber);

    void reactivateAccount(String accountNumber);

    double getAccountBalance(String accountNumber);

    String getAccountDetails(String accountNumber);
}