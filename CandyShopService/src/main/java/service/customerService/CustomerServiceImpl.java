package service.customerService;

import domain.Customer;
import domain.Shop;
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
        Customer customerTry = customerRepository.findOneCustomer(mail);
        if (customerTry != null) {
            if (password.equals(customerTry.getPassword())) {
                return customerTry;
            } else
                throw new ServiceException("Invalid password!\n");
        } else {
            throw new ServiceException("Authentication failed!");
        }
    }


    @Override
    public Customer createAccount(String firstName, String lastName, String email, String password,
                                  String phoneNumber, Location customerLocation) throws Exception {

        int id = 1;
        while (true) {
            boolean ok = true;
            for (var c : customerRepository.findAll())
                if (c.getIdCustomer() == id) {
                    ok = false;
                    break;
                }

            if (ok) break;
            id++;
        }

        String verif = verifCustomer(firstName, lastName, email, password, phoneNumber, customerLocation);
        if (!verif.matches("")) {
            throw new ServiceException(verif);
        }

        Customer customer = new Customer(id, firstName, lastName, email, password, phoneNumber, customerLocation);
        try {
            customerRepository.add(customer);
        } catch (RepositoryException e) {
            throw new Exception(e.getMessage());
        }

        return customer;
    }


    private String verifCustomer(String firstName, String lastName, String email, String password, String phoneNumber,
                                 Location location) {
        String error = "";

        if (firstName.equals("") || !firstName.matches("[a-zA-Z]+"))
            error += "Invalid first name!\n";

        if (lastName.equals("") || !lastName.matches("[a-zA-Z]+"))
            error += "Invalid last name!\n";

        if (email.equals("") || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            error += "Invalid email!\n";

        if (password.length() < 6)
            error += "Invalid password!\n";

        if (phoneNumber.length() != 10 || !phoneNumber.matches("[0-9]+"))
            error += "Invalid phone number!\n";

        if (location.getAddress().length() < 10)
            error += "Invalid address!\n";

        return error;
    }


    @Override
    public boolean findMail(String mail) {
        return customerRepository.findOneCustomer(mail) != null;
    }


}
