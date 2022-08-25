package builder;

import domain.Customer;
import exception.ValidationException;
import lombok.NoArgsConstructor;
import repository.customerRepository.CustomerInMemoryRepository;

import java.util.List;

@NoArgsConstructor
public class CustomerInMemoryRepositoryBuilder {
    public CustomerInMemoryRepository build(List<Customer> customerList) throws ValidationException {
        return CustomerInMemoryRepository.builder()
                .customerList(customerList)
                .build();
    }
}
