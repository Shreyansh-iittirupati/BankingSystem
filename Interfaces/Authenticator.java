package Interfaces;

public interface Authenticator {
    boolean authenticate(String username, String password);

    void registerUser(String username, String password);

    void resetPassword(String username);
}
