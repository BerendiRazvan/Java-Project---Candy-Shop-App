package repository.customerRepository;

import domain.Customer;
import domain.location.Location;
import exception.RepositoryException;
import lombok.AllArgsConstructor;
import lombok.Builder;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Builder
@AllArgsConstructor
public class CustomerInMemoryRepository implements CustomerRepository {

    private List<Customer> customerList;

    @Override
    public void add(Customer customer) throws RepositoryException {
        if (!customerList.contains(customer))
            customerList.add(customer);
        else
            throw new RepositoryException("This element already exists!");
    }

    @Override
    public void update(Long id, Customer customer) throws RepositoryException {
        Optional<Customer> customerToUpdate = findCustomerById(id);
        if (customerToUpdate.isPresent())
            customerList.set(customerList.indexOf(customerToUpdate.get()), customer);
        else
            throw new RepositoryException("This element does not exist!");
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        Optional<Customer> customerToRemove = findCustomerById(id);
        if (customerToRemove.isPresent())
            customerList.remove(customerToRemove.get());
        else
            throw new RepositoryException("This element does not exist!");
    }

    @Override
    public List<Customer> findAll() {
        return customerList;
    }

    @Override
    public Optional<Customer> findCustomerByEmail(String email) {
        return customerList.stream()
                .filter(customer -> email.equals(customer.getEmail()))
                .findFirst();
    }

    @Override
    public Optional<Customer> findCustomerById(Long id) {
        return customerList.stream()
                .filter(customer -> id == customer.getId())
                .findFirst();
    }

    @Override
    public Optional<Long> generateCustomerId() {
        //the temporary method
        //it will no longer be needed after we add a db because the id will be automatically generated
        long id = 1;
        while (true) {
            boolean ok = true;
            for (var c : customerList)
                if (c.getId() == id) {
                    ok = false;
                    break;
                }

            if (ok) return Optional.of(id);
            id++;
        }
    }

    @Override
    public void generateCustomers() {
        customerList.addAll(Arrays.asList(
                Customer.builder()
                        .id(1)
                        .firstName("Razvan")
                        .lastName("Berendi")
                        .email("br@gmail.com")
                        .password("12345678")
                        .phoneNumber("0751578787")
                        .location(Location.builder()
                                .country("Romania")
                                .city("Cluj")
                                .address("Aleea Rucar nr. 9, Bloc D13, ap. 1")
                                .build())
                        .build(),

                Customer.builder()
                        .id(2)
                        .firstName("Ana")
                        .lastName("Pop")
                        .email("ap@gmail.com")
                        .password("12345678")
                        .phoneNumber("0751578709")
                        .location(Location.builder()
                                .country("Romania")
                                .city("Cluj")
                                .address("Aleea Peana nr. 9, Bloc D19, ap. 2")
                                .build())
                        .build(),

                Customer.builder()
                        .id(3)
                        .firstName("Cristian")
                        .lastName("Popescu")
                        .email("cp@gmail.com")
                        .password("12345678")
                        .phoneNumber("0751572287")
                        .location(Location.builder()
                                .country("Romania")
                                .city("Cluj")
                                .address("Str. Mehedinti nr. 5, Bloc I3, ap. 1")
                                .build())
                        .build(),

                Customer.builder()
                        .id(4)
                        .firstName("Rares")
                        .lastName("Marina")
                        .email("rm@gmail.com")
                        .password("12345678")
                        .phoneNumber("0264578787")
                        .location(Location.builder()
                                .country("Romania")
                                .city("Cluj")
                                .address("Str. Constanta nr. 9, Bloc A2, ap. 3")
                                .build())
                        .build(),

                Customer.builder()
                        .id(5)
                        .firstName("Andreea")
                        .lastName("Staciu")
                        .email("asasr@gmail.com")
                        .password("12345678")
                        .phoneNumber("0721578123")
                        .location(Location.builder()
                                .country("Romania")
                                .city("Cluj")
                                .address("Str. Memo nr. 10, Casa nr. 15")
                                .build())
                        .build()
        ));
    }


}
