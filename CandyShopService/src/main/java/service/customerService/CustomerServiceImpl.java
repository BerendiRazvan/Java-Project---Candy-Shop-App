package service.customerService;

import domain.Customer;
import domain.location.Location;
import repository.customersRepository.CustomerRepository;
import repository.exception.RepositoryException;
import service.exception.ServiceException;

public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    public Customer login(String mail, String password) throws ServiceException {
        Customer customerTry = customerRepository.findCustomerByEmail(mail);
        if (customerTry != null) {
            if (password.equals(customerTry.getPassword())) {
                return customerTry;
            } else throw new ServiceException("Invalid password!\n");
        } else {
            throw new ServiceException("Authentication failed!");
        }
    }


    @Override
    public Customer createAccount(String firstName, String lastName, String email, String password,
                                  String phoneNumber, Location customerLocation) throws ServiceException {

        int id = generateCustomerId();

        String errorsAfterValidation = customerValidation(firstName, lastName, email, password, phoneNumber, customerLocation);
        if (!errorsAfterValidation.matches("")) {
            throw new ServiceException(errorsAfterValidation);
        }

        Customer customer = new Customer(id, firstName, lastName, email, password, phoneNumber, customerLocation);
        try {
            customerRepository.add(customer);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }

        return customer;
    }

    private int generateCustomerId() {
        //the temporary method
        //it will no longer be needed after we add a db because the id will be automatically generated
        int id = 1;
        while (true) {
            boolean ok = true;
            for (var c : customerRepository.findAll())
                if (c.getId() == id) {
                    ok = false;
                    break;
                }

            if (ok) return id;
            id++;
        }
    }


    private String customerValidation(String firstName, String lastName, String email, String password,
                                      String phoneNumber, Location location) {
        String error = "";

        if (firstName.equals("") || !firstName.matches("[a-zA-Z]+")) error += "Invalid first name!\n";

        if (lastName.equals("") || !lastName.matches("[a-zA-Z]+")) error += "Invalid last name!\n";

        if (email.equals("") || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) error += "Invalid email!\n";

        if (password.length() < 6) error += "Invalid password!\n";

        if (phoneNumber.length() != 10 || !phoneNumber.matches("[0-9]+")) error += "Invalid phone number!\n";

        if (location.getAddress().length() < 10) error += "Invalid address!\n";

        return error;
    }


    @Override
    public boolean findMail(String mail) {
        return customerRepository.findCustomerByEmail(mail) != null;
    }


}
