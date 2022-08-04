package service.customerService;

import domain.Customer;
import domain.location.Location;
import service.exception.ServiceException;

public interface CustomerService {
    Customer login(String mail, String password) throws ServiceException;

    Customer createAccount(String firstName, String lastName, String email, String password,
                           String phoneNumber, Location customerLocation) throws ServiceException;

    boolean checkIfEmailExists(String mail);
}
