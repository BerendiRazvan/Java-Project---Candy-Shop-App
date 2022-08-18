package validator;

import domain.Customer;
import domain.location.Location;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomerValidator {
    private static final String ONLY_LETTERS_VALIDATION = "[a-zA-Z]+";
    private static final String EMAIL_VALIDATION = "^[A-Za-z\\d+_.-]+@(.+)$";
    private static final String ONLY_DIGITS_VALIDATOR = "\\d+";
    private static final int MINIMUM_PASSWORD_LENGTH = 6;
    private static final int MINIMUM_PHONE_NUMBER_LENGTH = 10;

    public String customerFirstNameValidator(String firstName) {
        if (firstName.equals("") || !firstName.matches(ONLY_LETTERS_VALIDATION))
            return "Invalid first name!\n";
        return "";
    }

    public String customerLastNameValidator(String lastName) {
        if (lastName.equals("") || !lastName.matches(ONLY_LETTERS_VALIDATION))
            return "Invalid last name!\n";
        return "";
    }

    public String customerEmailValidator(String email) {
        if (email.equals("") || !email.matches(EMAIL_VALIDATION))
            return "Invalid email!\n";
        return "";
    }

    public String customerPasswordValidator(String password) {
        if (password.length() < MINIMUM_PASSWORD_LENGTH)
            return "Invalid password!\n";
        return "";
    }

    public String customerPhoneNumberValidator(String phoneNumber) {
        if (phoneNumber.length() != MINIMUM_PHONE_NUMBER_LENGTH || !phoneNumber.matches(ONLY_DIGITS_VALIDATOR))
            return "Invalid phone number!\n";
        return "";
    }

    public String customerLocationValidator(Location location) {
        LocationValidator validator = new LocationValidator();
        if (!validator.isValidLocation(location))
            return "Invalid address!\n";
        return "";
    }

    public boolean isValidCustomer(Customer customer) {
        return customerValidation(customer).equals("");
    }

    public String customerValidation(Customer customer) {
        if (customer == null) return "Customer can not be null!";
        return customerFirstNameValidator(customer.getFirstName()) +
                customerLastNameValidator(customer.getLastName()) +
                customerEmailValidator(customer.getEmail()) +
                customerPasswordValidator(customer.getPassword()) +
                customerPhoneNumberValidator(customer.getPhoneNumber()) +
                customerLocationValidator(customer.getLocation());
    }
}
