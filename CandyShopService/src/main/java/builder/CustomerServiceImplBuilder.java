package builder;

import exception.ValidationException;
import lombok.NoArgsConstructor;
import repository.customerRepository.CustomerRepository;
import service.customerService.CustomerServiceImpl;

@NoArgsConstructor
public class CustomerServiceImplBuilder {
    public CustomerServiceImpl build(CustomerRepository customerRepository) throws ValidationException {
        return CustomerServiceImpl.builder()
                .customerRepository(customerRepository)
                .build();
    }
}
