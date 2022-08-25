package service.customerService;

import builder.CustomerBuilder;
import domain.Customer;
import domain.location.Location;
import exception.BuildException;
import exception.RepositoryException;
import exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.customerRepository.CustomerRepository;

import java.util.Optional;

@Builder
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private CustomerRepository customerRepository;

    @Override
    public Optional<Customer> login(String mail, String password) throws ServiceException {
        LOGGER.info("Login for customer with mail = {} and password = ****** - started", mail);
        Optional<Customer> customerTry = customerRepository.findCustomerByEmail(mail);
        if (customerTry.isPresent()) {
            LOGGER.info("Login for customer with mail = {} and password = ****** - finished", mail);
            return verifyPassword(password, customerTry.get());
        } else {
            LOGGER.warn("Login for customer with mail = {} and password = ****** - exception occurred -> {}", mail,
                    "This element already exists!");
            throw new ServiceException("Authentication failed!");
        }
    }

    @Override
    public Optional<Customer> createAccount(String firstName, String lastName, String email, String password,
                                            String phoneNumber, Location customerLocation)
            throws ServiceException, BuildException {
        LOGGER.info("Create account for customer with first name: {}, last name: {}, email: {}, password = ******, " +
                        "phone number = {}, address = {} - started", firstName, lastName, email, phoneNumber,
                customerLocation.getAddress());
        Optional<Long> id = customerRepository.generateCustomerId();

        if (id.isPresent()) {
            CustomerBuilder customerBuilder = new CustomerBuilder();
            Customer customer = customerBuilder.build(id.get(), firstName, lastName, email, password, phoneNumber,
                    customerLocation);
            try {
                customerRepository.add(customer);
            } catch (RepositoryException e) {
                LOGGER.warn("Create account for customer with first name: {}, last name: {}, email: {}, password = ******, " +
                                "phone number = {}, address = {} - exception occurred -> {}", firstName, lastName, email,
                        phoneNumber, customerLocation.getAddress(), e.getMessage());
                throw new ServiceException(e.getMessage());
            }
            LOGGER.info("Create account for customer with first name: {}, last name: {}, email: {}, password = ******, " +
                            "phone number = {}, address = {} - finished", firstName, lastName, email, phoneNumber,
                    customerLocation.getAddress());
            return Optional.of(customer);

        } else {
            LOGGER.error("Create account for customer with first name: {}, last name: {}, email: {}, password = ******, " +
                            "phone number = {}, address = {} - exception occurred -> {}", firstName, lastName, email,
                    phoneNumber, customerLocation.getAddress(), "Error: generateCustomerId");
            throw new RuntimeException("Error: generateCustomerId");
        }
    }

    @Override
    public boolean checkIfEmailExists(String mail) {
        LOGGER.info("CheckIfEmailExists for customer with email = {} - called", mail);
        return customerRepository.findCustomerByEmail(mail).isPresent();
    }

    private Optional<Customer> verifyPassword(String customerPassword, Customer account) throws ServiceException {
        LOGGER.info("VerifyPassword for customer with password = ****** - started");
        if (customerPassword.equals(account.getPassword())) {
            LOGGER.info("VerifyPassword for customer with password = ****** - finished");
            return Optional.of(account);
        } else {
            LOGGER.warn("VerifyPassword for customer with password = ****** - exception occurred -> {}", "Invalid password!");
            throw new ServiceException("Invalid password!\n");
        }
    }
}
