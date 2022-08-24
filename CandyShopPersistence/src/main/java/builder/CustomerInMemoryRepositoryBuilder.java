package builder;

import domain.Customer;
import exception.BuildException;
import lombok.NoArgsConstructor;
import repository.customerRepository.CustomerInMemoryRepository;

import java.util.List;

@NoArgsConstructor
public class CustomerInMemoryRepositoryBuilder {
    public CustomerInMemoryRepository build(List<Customer> customerList) throws BuildException {
        return CustomerInMemoryRepository.builder()
                .customerList(customerList)
                .build();
    }
}
