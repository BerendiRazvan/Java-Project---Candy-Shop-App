package repository.customersRepository;

import domain.Customer;
import repository.Repository;


public interface CustomersRepository extends Repository<Long, Customer> {
    Customer findOneCustomer(String email);
}
