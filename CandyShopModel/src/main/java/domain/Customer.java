package domain;

import domain.location.Location;

public class Customer {
    private long idCustomer;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Location customerLocation;

    public Customer(long idCustomer, String firstName, String lastName, String email, String password,
                    String phoneNumber, Location customerLocation) {
        this.idCustomer = idCustomer;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.customerLocation = customerLocation;
    }

    public long getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(long idCustomer) {
        this.idCustomer = idCustomer;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Location getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(Location customerLocation) {
        this.customerLocation = customerLocation;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName +
                "\nEmail: " + email +
                "\nPhone: " + phoneNumber +
                "\n" + customerLocation;
    }
}
