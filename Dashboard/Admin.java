package Dashboard;

import AbstractClasses.User;
import Interfaces.AdminControls;

class Admin extends User implements AdminControls {
    public Admin(String username, String password) {
        super(username, password);
    }

    public void addUser(String username, String password) {
        System.out.println("User added: " + username);
    }

    public void updateUser(String username, String newDetails) {
        System.out.println("User updated: " + username);
    }

    public void deleteUser(String username) {
        System.out.println("User deleted: " + username);
    }

    public void monitorSystemLogs() {
        System.out.println("");
    }

    public void assignRole(String username, String role) {
        System.out.println("Assigned role " + role + " to user: " + username);
    }

    public void showDashboard() {
        System.out.println("Admin Dashboard");
    }
}