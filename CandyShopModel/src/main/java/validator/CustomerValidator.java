package validator;

import domain.Customer;
import domain.location.Location;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class CustomerValidator {
    private static final String WORD_VALIDATION_REGULAR_EXPRESSION = "[a-zA-Z ]+";
    private static final String EMAIL_VALIDATION_REGULAR_EXPRESSION = "^[A-Za-z\\d+_.-]+@(.+)$";
    private static final String NUMBER_VALIDATION_REGULAR_EXPRESSION = "\\d+";
    private static final int MINIMUM_PASSWORD_LENGTH = 6;
    private static final int PHONE_NUMBER_LENGTH = 10;

    public boolean isValidCustomer(Customer customer) {
        return validateCustomer(customer).isEmpty();
    }

    public List<String> validateCustomer(Customer customer) {
        if (customer == null) return List.of("Customer can not be null!");
        List<String> errors = new ArrayList<>();

        String error = validateCustomerFirstName(customer.getFirstName());
        if (!error.matches(""))
            errors.add(error);

        error = validateCustomerLastName(customer.getLastName());
        if (!error.matches(""))
            errors.add(error);

        error = validateCustomerEmail(customer.getEmail());
        if (!error.matches(""))
            errors.add(error);

        error = validateCustomerPassword(customer.getPassword());
        if (!error.matches(""))
            errors.add(error);

        error = validateCustomerPhoneNumber(customer.getPhoneNumber());
        if (!error.matches(""))
            errors.add(error);

        error = validateCustomerLocation(customer.getLocation());
        if (!error.matches(""))
            errors.add(error);

        return errors;
    }

    private String validateCustomerFirstName(String firstName) {
        if (firstName.equals("") || !firstName.matches(WORD_VALIDATION_REGULAR_EXPRESSION))
            return "Invalid first name!\n";
        return "";
    }

    private String validateCustomerLastName(String lastName) {
        if (lastName.equals("") || !lastName.matches(WORD_VALIDATION_REGULAR_EXPRESSION))
            return "Invalid last name!\n";
        return "";
    }

    private String validateCustomerEmail(String email) {
        if (email.equals("") || !email.matches(EMAIL_VALIDATION_REGULAR_EXPRESSION))
            return "Invalid email!\n";
        return "";
    }

    private String validateCustomerPassword(String password) {
        if (password.length() < MINIMUM_PASSWORD_LENGTH)
            return "Invalid password!\n";
        return "";
    }

    private String validateCustomerPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != PHONE_NUMBER_LENGTH || !phoneNumber.matches(NUMBER_VALIDATION_REGULAR_EXPRESSION))
            return "Invalid phone number!\n";
        return "";
    }

    private String validateCustomerLocation(Location location) {
        LocationValidator validator = new LocationValidator();
        if (!validator.isValidLocation(location))
            return "Invalid address!\n";
        return "";
    }
}
