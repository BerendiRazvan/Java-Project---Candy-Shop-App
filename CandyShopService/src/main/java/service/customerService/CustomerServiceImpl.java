package service.customerService;

import domain.Customer;
import domain.location.Location;
import exception.RepositoryException;
import exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import repository.customerRepository.CustomerRepository;
import validator.CustomerValidator;

import java.util.Optional;

@Builder
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;

    @Override
    public Optional<Customer> login(String mail, String password) throws ServiceException {
        Optional<Customer> customerTry = customerRepository.findCustomerByEmail(mail);
        if (customerTry.isPresent())
            return verifyPassword(password, customerTry.get());
        else
            throw new ServiceException("Authentication failed!");
    }

    @Override
    public Optional<Customer> createAccount(String firstName, String lastName, String email, String password,
                                            String phoneNumber, Location customerLocation) throws ServiceException {
        Optional<Long> id = customerRepository.generateCustomerId();

        if (id.isPresent()) {
            Customer customer = Customer.builder()
                    .id(id.get())
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .password(password)
                    .phoneNumber(phoneNumber)
                    .location(customerLocation)
                    .build();

            CustomerValidator validator = new CustomerValidator();
            if (!validator.isValidCustomer(customer))
                throw new ServiceException(validator.validateCustomer(customer));

            try {
                customerRepository.add(customer);
            } catch (RepositoryException e) {
                throw new ServiceException(e.getMessage());
            }
            return Optional.of(customer);

        } else throw new RuntimeException("Error: generateCustomerId");
    }

    @Override
    public boolean checkIfEmailExists(String mail) {
        return customerRepository.findCustomerByEmail(mail).isPresent();
    }

    private Optional<Customer> verifyPassword(String customerPassword, Customer account) throws ServiceException {
        if (customerPassword.equals(account.getPassword()))
            return Optional.of(account);
        else
            throw new ServiceException("Invalid password!\n");
    }
}
