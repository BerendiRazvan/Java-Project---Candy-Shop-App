package service.customerService;

import domain.Customer;
import domain.location.Location;
import exception.ValidationException;
import exception.ServiceException;

import java.util.Optional;

public interface CustomerService {
    /**
     *
     * @param mail
     * @param password
     * @return
     * @throws ServiceException
     */
    Optional<Customer> login(String mail, String password) throws ServiceException;

    /**
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @param phoneNumber
     * @param customerLocation
     * @return
     * @throws ServiceException
     * @throws ValidationException
     */
    Optional<Customer> createAccount(String firstName, String lastName, String email, String password,
                           String phoneNumber, Location customerLocation) throws ServiceException, ValidationException;

    /**
     *
     * @param mail
     * @return
     */
    boolean checkIfEmailExists(String mail);
}
