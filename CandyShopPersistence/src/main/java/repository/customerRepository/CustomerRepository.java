package repository.customerRepository;

import domain.Customer;
import exception.ValidationException;
import repository.Repository;

import java.util.Optional;


public interface CustomerRepository extends Repository<Long, Customer> {
    Optional<Customer> findCustomerByEmail(String email);

    Optional<Customer> findCustomerById(Long id);

    Optional<Long> generateCustomerId();

}
