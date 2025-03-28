package Dashboard;
import java.math.BigDecimal;

public class Account {
    private final String userId;
    private final String accountNumber;
    private BigDecimal balance;
    private String newDetails;
    private boolean isActive;
    private final String creationDate;
    private final String accountType;

    public Account(String userId, String accountNumber, BigDecimal balance,
                   String newDetails, boolean isActive, String creationDate, String accountType) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.newDetails = newDetails;
        this.isActive = isActive;
        this.creationDate = creationDate;
        this.accountType = accountType;
    }

    public String getUserId() {
        return userId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getNewDetails() {
        return newDetails;
    }

    public void setNewDetails(String newDetails) {
        this.newDetails = newDetails;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getAccountType() {
        return accountType;
    }
}


