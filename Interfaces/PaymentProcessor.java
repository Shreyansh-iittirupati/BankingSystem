package Interfaces;

public interface PaymentProcessor {
    boolean validateUPIDetails(String upiID);

    String checkPaymentStatus(String transactionID);

    void processPayment(String fromAccount, String toAccount, double amount);
}
