package service.customerService;

import domain.Customer;
import domain.location.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import repository.customerRepository.CustomerRepository;
import repository.exception.RepositoryException;
import service.exception.ServiceException;

import java.util.Optional;

@Builder
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;

    @Override
    public Optional<Customer> login(String mail, String password) throws ServiceException {
        Optional<Customer> customerTry = customerRepository.findCustomerByEmail(mail);
        if (customerTry.isPresent()) {
            return verifyPassword(password, customerTry.get());
        } else {
            throw new ServiceException("Authentication failed!");
        }
    }

    @Override
    public Optional<Customer> createAccount(String firstName, String lastName, String email, String password,
                                  String phoneNumber, Location customerLocation) throws ServiceException {

        Optional<Long> id = customerRepository.generateCustomerId();

        String errorsAfterValidation = customerValidation(firstName, lastName, email, password, phoneNumber, customerLocation);
        if (!errorsAfterValidation.matches("")) {
            throw new ServiceException(errorsAfterValidation);
        }

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

    private String customerValidation(String firstName, String lastName, String email, String password,
                                      String phoneNumber, Location location) {
        String error = "";

        if (firstName.equals("") || !firstName.matches("[a-zA-Z]+")) error += "Invalid first name!\n";

        if (lastName.equals("") || !lastName.matches("[a-zA-Z]+")) error += "Invalid last name!\n";

        if (email.equals("") || !email.matches("^[A-Za-z\\d+_.-]+@(.+)$")) error += "Invalid email!\n";

        if (password.length() < 6) error += "Invalid password!\n";

        if (phoneNumber.length() != 10 || !phoneNumber.matches("\\d+")) error += "Invalid phone number!\n";

        if (location.getAddress().length() < 10) error += "Invalid address!\n";

        return error;
    }
}
