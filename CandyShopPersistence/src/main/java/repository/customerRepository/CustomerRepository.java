package repository.customerRepository;

import domain.Customer;
import repository.Repository;


public interface CustomerRepository extends Repository<Long, Customer> {
    Customer findCustomerByEmail(String email);

    Customer findCustomerById(Long id);

    int generateCustomerId();

    void generateCustomers();
}
