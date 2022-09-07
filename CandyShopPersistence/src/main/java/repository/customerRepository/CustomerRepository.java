package repository.customerRepository;

import domain.Customer;
import repository.Repository;

import java.util.Optional;


public interface CustomerRepository extends Repository<Long, Customer> {
    /**
     * The method will search for Customer with the given email
     *
     * @param email The id for Customer you are looking for
     * @return Optional.of(Customer) - if Customer is found
     * Optional.empty() - else
     */
    Optional<Customer> findCustomerByEmail(String email);

    /**
     * The method will search for Customer with the given id
     *
     * @param id The id for Customer you are looking for
     * @return Optional.of(Customer) - if Customer is found
     * Optional.empty() - else
     */
    Optional<Customer> findCustomerById(Long id);

    /**
     * The method that will generate an id for Customer
     *
     * @return an available id
     */
    Optional<Long> generateCustomerId();

}
