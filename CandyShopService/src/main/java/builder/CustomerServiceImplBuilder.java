package builder;

import exception.BuildException;
import lombok.NoArgsConstructor;
import repository.customerRepository.CustomerRepository;
import service.customerService.CustomerServiceImpl;

@NoArgsConstructor
public class CustomerServiceImplBuilder {
    public CustomerServiceImpl build(CustomerRepository customerRepository) throws BuildException {
        return CustomerServiceImpl.builder()
                .customerRepository(customerRepository)
                .build();
    }
}
