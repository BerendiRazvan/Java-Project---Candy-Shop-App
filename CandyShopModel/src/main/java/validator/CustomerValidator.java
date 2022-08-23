package validator;

import domain.Customer;
import domain.location.Location;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class CustomerValidator {
    private static final String WORD_VALIDATION_REGULAR_EXPRESSION = "[a-zA-Z]+";
    private static final String EMAIL_VALIDATION_REGULAR_EXPRESSION = "^[A-Za-z\\d+_.-]+@(.+)$";
    private static final String NUMBER_VALIDATION_REGULAR_EXPRESSION = "\\d+";
    private static final int MINIMUM_PASSWORD_LENGTH = 6;
    private static final int PHONE_NUMBER_LENGTH = 10;

    public String validateCustomerFirstName(String firstName) {
        if (firstName.equals("") || !firstName.matches(WORD_VALIDATION_REGULAR_EXPRESSION))
            return "Invalid first name!\n";
        return "";
    }

    public String validateCustomerLastName(String lastName) {
        if (lastName.equals("") || !lastName.matches(WORD_VALIDATION_REGULAR_EXPRESSION))
            return "Invalid last name!\n";
        return "";
    }

    public String validateCustomerEmail(String email) {
        if (email.equals("") || !email.matches(EMAIL_VALIDATION_REGULAR_EXPRESSION))
            return "Invalid email!\n";
        return "";
    }

    public String validateCustomerPassword(String password) {
        if (password.length() < MINIMUM_PASSWORD_LENGTH)
            return "Invalid password!\n";
        return "";
    }

    public String validateCustomerPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != PHONE_NUMBER_LENGTH || !phoneNumber.matches(NUMBER_VALIDATION_REGULAR_EXPRESSION))
            return "Invalid phone number!\n";
        return "";
    }

    public String validateCustomerLocation(Location location) {
        LocationValidator validator = new LocationValidator();
        if (!validator.isValidLocation(location))
            return "Invalid address!\n";
        return "";
    }

    public boolean isValidCustomer(Customer customer) {
        return validateCustomer(customer).isEmpty();
    }

    public List<String> validateCustomer(Customer customer) {
        if (customer == null) return List.of("Customer can not be null!");
        List<String> errors = new ArrayList<>(
                Arrays.asList(validateCustomerFirstName(customer.getFirstName()),
                        validateCustomerLastName(customer.getLastName()),
                        validateCustomerEmail(customer.getEmail()),
                        validateCustomerPassword(customer.getPassword()),
                        validateCustomerPhoneNumber(customer.getPhoneNumber()),
                        validateCustomerLocation(customer.getLocation())));
        errors.removeAll(Collections.singleton(""));
        return errors;
    }
}
