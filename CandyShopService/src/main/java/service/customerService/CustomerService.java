package service.customerService;

import domain.Customer;
import domain.location.Location;
import exception.ValidationException;
import exception.ServiceException;

import java.util.Optional;

public interface CustomerService {
    /**
     * This method makes the login of a client with email and password
     *
     * @param mail     The mail for the Client who is logging in
     * @param password The password for the Client who is logging in
     * @return The logged in client
     * @throws ServiceException If authentication fails
     */
    Optional<Customer> login(String mail, String password) throws ServiceException;

    /**
     * This method creates a new account
     *
     * @param firstName        The first name for the new account
     * @param lastName         The last name for the new account
     * @param email            The email for the new account
     * @param password         The password for the new account
     * @param phoneNumber      The phone number for the new account
     * @param customerLocation The customer location for the new account
     * @return The new account created
     * @throws ServiceException    If the account creation fails
     * @throws ValidationException If the data entered for the new account are invalid
     */
    Optional<Customer> createAccount(String firstName, String lastName, String email, String password,
                                     String phoneNumber, Location customerLocation) throws ServiceException, ValidationException;

    /**
     * The method checks if the email already exists
     *
     * @param mail The email we were looking for
     * @return The account with the given mail
     */
    boolean checkIfEmailExists(String mail);
}
