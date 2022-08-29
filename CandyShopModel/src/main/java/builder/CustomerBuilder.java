package builder;

import domain.Customer;
import domain.location.Location;
import exception.ValidationException;
import lombok.NoArgsConstructor;
import validator.CustomerValidator;

@NoArgsConstructor
public class CustomerBuilder {
    public Customer build(long id, String firstName, String lastName, String email, String password,
                          String phoneNumber, Location customerLocation) throws ValidationException {
        Customer customer = Customer.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .location(customerLocation)
                .build();
        CustomerValidator validator = new CustomerValidator();

        if (validator.isValidCustomer(customer))
            return customer;
        else
            throw new ValidationException(validator.validateCustomer(customer).stream()
                    .reduce("", (result, error) -> result + error));
    }
}
