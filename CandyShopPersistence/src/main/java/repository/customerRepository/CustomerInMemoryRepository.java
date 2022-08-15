package repository.customerRepository;

import domain.Customer;
import domain.location.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import repository.exception.RepositoryException;

import java.util.Arrays;
import java.util.List;


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
        Customer customerToUpdate = findCustomerById(id);
        if (customerToUpdate == null)
            throw new RepositoryException("This element does not exist!");
        else
            customerList.set(customerList.indexOf(customerToUpdate), customer);
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        Customer customerToRemove = findCustomerById(id);
        if (customerToRemove == null)
            throw new RepositoryException("This element does not exist!");
        else
            customerList.remove(customerToRemove);
    }

    @Override
    public List<Customer> findAll() {
        return customerList;
    }

    @Override
    public Customer findCustomerByEmail(String email) {
        for (Customer customer : customerList)
            if (email.equals(customer.getEmail())) return customer;
        return null;
    }

    @Override
    public Customer findCustomerById(Long id) {
        for (Customer customer : customerList)
            if (customer.getId() == id) return customer;
        return null;
    }

    @Override
    public int generateCustomerId() {
        //the temporary method
        //it will no longer be needed after we add a db because the id will be automatically generated
        int id = 1;
        while (true) {
            boolean ok = true;
            for (var c : customerList)
                if (c.getId() == id) {
                    ok = false;
                    break;
                }

            if (ok) return id;
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
