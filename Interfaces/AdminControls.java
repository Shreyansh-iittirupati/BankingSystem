package Interfaces;

public interface AdminControls {
    void addUser(String username, String password);

    void updateUser(String username, String newDetails);

    void deleteUser(String username);

    void monitorSystemLogs();

    void assignRole(String username, String role);
}
