package repository.customersRepository;

import domain.Customer;
import repository.exception.RepositoryException;

import java.util.List;

public class CustomersInMemoryRepository implements CustomersRepository {

    private List<Customer> customersMemoryList;

    public CustomersInMemoryRepository(List<Customer> customersMemoryList) {
        this.customersMemoryList = customersMemoryList;
    }

    @Override
    public void add(Customer elem) throws RepositoryException {
        if (!customersMemoryList.contains(elem))
            customersMemoryList.add(elem);
        else
            throw new RepositoryException("This element already exists!");
    }

    @Override
    public void update(Long aLong, Customer elem) throws RepositoryException {
        boolean exists = false;
        for (Customer customer : customersMemoryList) {
            if (customer.getIdCustomer() == elem.getIdCustomer()) {
                customersMemoryList.set(customersMemoryList.indexOf(customer), elem);
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
        for (Customer customer : customersMemoryList) {
            if (customer.getIdCustomer() == aLong) {
                customersMemoryList.remove(customer);
                exists = true;
                break;
            }
        }
        if (!exists)
            throw new RepositoryException("This element does not exist!");
    }

    @Override
    public List<Customer> findAll() {
        return customersMemoryList;
    }

    @Override
    public Customer findOneCustomer(String email) {
        for (Customer customer : customersMemoryList)
            if (email.equals(customer.getEmail())) return customer;
        return null;
    }
}
