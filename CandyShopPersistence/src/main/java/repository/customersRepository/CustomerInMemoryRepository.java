package repository.customersRepository;

import domain.Customer;
import domain.location.Location;
import repository.exception.RepositoryException;

import java.util.ArrayList;
import java.util.List;

public class CustomerInMemoryRepository implements CustomerRepository {

    private List<Customer> customerList;

    public CustomerInMemoryRepository(List<Customer> customerList) {
        this.customerList = customerList;
    }

    @Override
    public void add(Customer elem) throws RepositoryException {
        if (!customerList.contains(elem))
            customerList.add(elem);
        else
            throw new RepositoryException("This element already exists!");
    }

    @Override
    public void update(Long aLong, Customer elem) throws RepositoryException {
        boolean exists = false;
        for (Customer customer : customerList) {
            if (customer.getIdCustomer() == elem.getIdCustomer()) {
                customerList.set(customerList.indexOf(customer), elem);
                exists = true;
                break;
            }
        }
        if (!exists)
            throw new RepositoryException("This element does not exist!");
    }

    @Override
    public void delete(Long aLong) throws RepositoryException {
        boolean exists = false;
        for (Customer customer : customerList) {
            if (customer.getIdCustomer() == aLong) {
                customerList.remove(customer);
                exists = true;
                break;
            }
        }
        if (!exists)
            throw new RepositoryException("This element does not exist!");
    }

    @Override
    public List<Customer> findAll() {
        return customerList;
    }

    @Override
    public Customer findOneCustomer(String email) {
        for (Customer customer : customerList)
            if (email.equals(customer.getEmail())) return customer;
        return null;
    }


    public static List<Customer> generateCustomers() {
        List<Customer> customerList = new ArrayList<>();

        customerList.add(new Customer(1, "Razvan", "Berendi",
                "br@gmail.com", "12345678", "0751578787",
                new Location(1, "Romania", "Cluj", "Aleea Rucar nr. 9, Bloc D13, ap. 1")));

        customerList.add(new Customer(2, "Ana", "Pop",
                "ap@gmail.com", "12345678", "0751578709",
                new Location(2, "Romania", "Cluj", "Aleea Peana nr. 9, Bloc D19, ap. 2")));

        customerList.add(new Customer(3, "Cristian", "Popescu",
                "cp@gmail.com", "12345678", "0751572287",
                new Location(3, "Romania", "Cluj", "Str. Mehedinti nr. 5, Bloc I3, ap. 1")));

        customerList.add(new Customer(4, "Rares", "Marina",
                "rm@gmail.com", "12345678", "0264578787",
                new Location(4, "Romania", "Cluj", "Str. Constanta nr. 9, Bloc A2, ap. 3")));

        customerList.add(new Customer(5, "Andreea", "Staciu",
                "asasr@gmail.com", "12345678", "0721578123",
                new Location(5, "Romania", "Cluj", "Str. Memo nr. 10, Casa nr. 15")));
        return customerList;
    }
}
