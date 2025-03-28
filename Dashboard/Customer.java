package Dashboard;

import AbstractClasses.User;

public class Customer extends User {
    public Customer(String userId, String password) {
        super(userId, password);
    }

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    public Customer(String userId, String password, String username, String firstName, String lastName, String email, String phoneNumber) {
        super(userId, password);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void showDashboard() {
        System.out.println("User Dashboard");
    }
    public String getUserId(){
        return super.userId;
    }
}


