package service.customerService;

import domain.Customer;
import domain.location.Location;
import service.exception.ServiceException;

public interface CustomerService {
    Customer login(String mail, String password) throws ServiceException;

    //customer service
    Customer createAccount(String firstName, String lastName, String email, String password,
                           String phoneNumber, Location customerLocation) throws Exception;

    //customer service
    boolean findMail(String mail);
}
