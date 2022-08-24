package service.customerService;

import domain.Customer;
import domain.location.Location;
import exception.BuildException;
import exception.ServiceException;

import java.util.Optional;

public interface CustomerService {
    Optional<Customer> login(String mail, String password) throws ServiceException;

    Optional<Customer> createAccount(String firstName, String lastName, String email, String password,
                           String phoneNumber, Location customerLocation) throws ServiceException, BuildException;

    boolean checkIfEmailExists(String mail);
}
