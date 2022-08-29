package repository.customerRepository;

import builder.CustomerBuilder;
import builder.LocationBuilder;
import domain.Customer;
import exception.ValidationException;
import exception.RepositoryException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class CustomerInMemoryRepository implements CustomerRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerInMemoryRepository.class);
    private List<Customer> customerList;

    public CustomerInMemoryRepository() {
        this(new ArrayList<>());
        generateCustomers();
    }

    @Override
    public void add(Customer customer) throws RepositoryException {
        LOGGER.info("Add customer - started");
        if (!customerList.contains(customer)) {
            customerList.add(customer);
            LOGGER.info("Add customer - finished");
        } else {
            LOGGER.warn("Add customer - exception occurred -> {}", "This element already exists!");
            throw new RepositoryException("This element already exists!");
        }
    }

    @Override
    public void update(Long id, Customer customer) throws RepositoryException {
        LOGGER.info("Update customer with id = {} - started", id);
        Optional<Customer> customerToUpdate = findCustomerById(id);
        if (customerToUpdate.isPresent()) {
            customerList.set(customerList.indexOf(customerToUpdate.get()), customer);
            LOGGER.info("Update customer with id = {} - finished", id);
        } else {
            LOGGER.warn("Update customer with id = {} to - exception occurred -> {}", id,
                    "This element does not exist!");
            throw new RepositoryException("This element does not exist!");
        }
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        LOGGER.info("Delete customer with id = {} - started", id);
        Optional<Customer> customerToRemove = findCustomerById(id);
        if (customerToRemove.isPresent()) {
            customerList.remove(customerToRemove.get());
            LOGGER.info("Delete customer with id = {} - finished", id);
        } else {
            LOGGER.warn("Delete customer with id = {} - exception occurred -> {}", id, "This element does not exist!");
            throw new RepositoryException("This element does not exist!");
        }
    }

    @Override
    public List<Customer> findAll() {
        LOGGER.info("FindAll customers - called");
        return customerList;
    }

    @Override
    public Optional<Customer> findCustomerByEmail(String email) {
        LOGGER.info("FindCustomerByEmail for customer with email = {} - called", email);
        return customerList.stream()
                .filter(customer -> email.equals(customer.getEmail()))
                .findFirst();
    }

    @Override
    public Optional<Customer> findCustomerById(Long id) {
        LOGGER.info("FindCustomerById for customer with id = {} - called", id);
        return customerList.stream()
                .filter(customer -> id == customer.getId())
                .findFirst();
    }

    @Override
    public Optional<Long> generateCustomerId() {
        //the temporary method
        //it will no longer be needed after we add a db because the id will be automatically generated

        LOGGER.info("GenerateCustomerId - started");

        long id = 1;
        while (true) {
            boolean ok = true;
            for (var c : customerList)
                if (c.getId() == id) {
                    ok = false;
                    break;
                }

            if (ok) {
                LOGGER.info("GenerateCustomerId - finished");
                return Optional.of(id);
            }
            id++;
        }
    }

    private void generateCustomers() {
        LOGGER.info("GenerateCustomers - started");
        LocationBuilder locationBuilder = new LocationBuilder();
        CustomerBuilder customerBuilder = new CustomerBuilder();

        try {
            customerList.addAll(Arrays.asList(
                    customerBuilder.build(1, "Razvan", "Berendi", "br@gmail.com",
                            "12345678", "0751578787", locationBuilder.build("Romania", "Cluj",
                                    "Aleea Rucar nr. 9, Bloc D13, ap. 1")),

                    customerBuilder.build(2, "Ana", "Pop", "ap@gmail.com",
                            "12345678", "0751578709", locationBuilder.build("Romania", "Cluj",
                                    "Aleea Peana nr. 9, Bloc D19, ap. 2")),

                    customerBuilder.build(3, "Cristian", "Popescu", "cp@gmail.com",
                            "12345678", "0751572287", locationBuilder.build("Romania", "Cluj",
                                    "Str. Mehedinti nr. 5, Bloc I3, ap. 1")),

                    customerBuilder.build(4, "Rares", "Marina", "rm@gmail.com",
                            "12345678", "0264578787", locationBuilder.build("Romania", "Cluj",
                                    "Str. Constanta nr. 9, Bloc A2, ap. 3")),

                    customerBuilder.build(5, "Andreea", "Suciu", "as@gmail.com",
                            "12345678", "0721578123", locationBuilder.build("Romania", "Cluj",
                                    "Str. Memo nr. 10, Casa nr. 15"))
            ));
        } catch (ValidationException e) {
            System.out.println("Unfinished generation due to: " + e.getMessage());
            LOGGER.error("GenerateCustomers - exception occurred -> {}", e.getMessage());
        }
        LOGGER.info("GenerateCustomers - finished");
    }


}
