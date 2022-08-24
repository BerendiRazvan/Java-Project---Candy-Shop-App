package repository.customerRepository;

import builder.CustomerBuilder;
import builder.LocationBuilder;
import domain.Customer;
import exception.BuildException;
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
    public void generateCustomers() throws BuildException {
        LocationBuilder locationBuilder = new LocationBuilder();
        CustomerBuilder customerBuilder = new CustomerBuilder();

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
    }


}
