package repository.customersRepository;

import domain.Customer;
import repository.exception.RepositoryException;

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
}
