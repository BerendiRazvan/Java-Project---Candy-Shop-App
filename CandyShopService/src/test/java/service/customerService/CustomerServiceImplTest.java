package service.customerService;

import builder.*;
import domain.Customer;
import domain.location.Location;
import exception.ValidationException;
import exception.ServiceException;
import org.junit.jupiter.api.*;
import repository.customerRepository.CustomerInMemoryRepository;
import repository.customerRepository.CustomerRepository;
import validator.CustomerValidator;

import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static service.ConstantValues.*;

class CustomerServiceImplTest {
    private CustomerService customerService;
    private CustomerValidator validator;
    private Location location;
    private CustomerBuilder customerBuilder;
    private LocationBuilder locationBuilder;

    @BeforeAll
    static void setUpAll() {
        System.out.println("Tests for CustomerServiceImpl");
    }

    @BeforeEach
    void setUp() throws ValidationException {
        validator = new CustomerValidator();

        customerBuilder = new CustomerBuilder();
        locationBuilder = new LocationBuilder();

        location = locationBuilder.build(COUNTRY, CITY, ADDRESS);

        CustomerRepository customerRepository = new CustomerInMemoryRepository();
        customerService = new CustomerServiceImpl(customerRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Tests passed");
    }

    @Test
    void testValidLogin() throws ServiceException {
        Optional<Customer> customer = customerService.login(EMAIL, PASSWORD);
        if (customer.isPresent()) {
            assertEquals(customer.get().getId(), ID);
            assertEquals(customer.get().getEmail(), EMAIL);
            assertEquals(customer.get().getFirstName(), FIRST_NAME);
            assertEquals(customer.get().getLastName(), LAST_NAME);
            assertEquals(customer.get().getPhoneNumber(), PHONE_NUMBER);
            assertEquals(customer.get().getPassword(), PASSWORD);
            assertEquals(customer.get().getLocation().getAddress(), ADDRESS);
        } else fail("Customer login failed");
    }

    @Test
    void testInvalidLogin() {
        assertThrowsExactly(ServiceException.class,
                () -> customerService.login("brazvan1234567890@gmail.com", PASSWORD),
                AUTHENTICATION_EXCEPTION);

        assertThrowsExactly(ServiceException.class,
                () -> customerService.login(EMAIL, "1234dasdas678"),
                CUSTOMER_PASSWORD_EXCEPTION);
    }


    @Test
    void testValidCreateAccount() throws ServiceException, ValidationException {
        Optional<Customer> customer = customerService.createAccount(FIRST_NAME, LAST_NAME, "berendi.rav2001@gmail.com",
                PASSWORD, PHONE_NUMBER, location);
        if (customer.isPresent()) {
            assertEquals(customer.get().getId(), 6);
            assertEquals(customer.get().getEmail(), "berendi.rav2001@gmail.com");
            assertEquals(customer.get().getFirstName(), FIRST_NAME);
            assertEquals(customer.get().getLastName(), LAST_NAME);
            assertEquals(customer.get().getPhoneNumber(), PHONE_NUMBER);
            assertEquals(customer.get().getPassword(), PASSWORD);
            assertEquals(customer.get().getLocation().getAddress(), ADDRESS);
        } else fail("Customer login failed");
    }

    @Test
    void testInvalidCreateAccount() {
        assertThrowsExactly(ValidationException.class,
                () -> customerService.createAccount(FIRST_NAME, LAST_NAME, "berendi.rav2001@gmail.com", PASSWORD,
                        "1234", location),
                CUSTOMER_PHONE_NUMBER_EXCEPTION);
    }

    @Test
    void testCustomerValidationForFirstName() {
        assertThrowsExactly(ValidationException.class,
                () -> validator.validateCustomer(customerBuilder.build(ID, "", LAST_NAME, EMAIL, PASSWORD,
                        PHONE_NUMBER, location)),
                CUSTOMER_FIRST_NAME_EXCEPTION);

        assertThrowsExactly(ValidationException.class,
                () -> validator.validateCustomer(customerBuilder.build(ID, "Razvan1234", LAST_NAME, EMAIL,
                        PASSWORD, PHONE_NUMBER, location)),
                CUSTOMER_FIRST_NAME_EXCEPTION);
    }

    @Test
    void testCustomerValidationForLastName() {
        assertThrowsExactly(ValidationException.class,
                () -> validator.validateCustomer(customerBuilder.build(ID, FIRST_NAME, "", EMAIL, PASSWORD,
                        PHONE_NUMBER, location)),
                CUSTOMER_LAST_NAME_EXCEPTION);

        assertThrowsExactly(ValidationException.class,
                () -> validator.validateCustomer(customerBuilder.build(ID, FIRST_NAME, "B3r3ndi", EMAIL,
                        PASSWORD, PHONE_NUMBER, location)),
                CUSTOMER_LAST_NAME_EXCEPTION);
    }

    @Test
    void testCustomerValidationForEmail() {
        assertThrowsExactly(ValidationException.class,
                () -> validator.validateCustomer(customerBuilder.build(ID, FIRST_NAME, LAST_NAME, "", PASSWORD,
                        PHONE_NUMBER, location)),
                CUSTOMER_EMAIL_EXCEPTION);

        assertThrowsExactly(ValidationException.class,
                () -> validator.validateCustomer(customerBuilder.build(ID, FIRST_NAME, LAST_NAME, "razvan.gmail.com",
                        PASSWORD, PHONE_NUMBER, location)),
                CUSTOMER_EMAIL_EXCEPTION);

        assertThrowsExactly(ValidationException.class,
                () -> validator.validateCustomer(customerBuilder.build(ID, FIRST_NAME, LAST_NAME, "@gmail.com",
                        PASSWORD, PHONE_NUMBER, location)),
                CUSTOMER_EMAIL_EXCEPTION);

        assertThrowsExactly(ValidationException.class,
                () -> validator.validateCustomer(customerBuilder.build(ID, FIRST_NAME, LAST_NAME, "gmail.com@",
                        PASSWORD, PHONE_NUMBER, location)),
                CUSTOMER_EMAIL_EXCEPTION);
    }

    @Test
    void testCustomerValidationForPassword() {
        assertThrowsExactly(ValidationException.class,
                () -> validator.validateCustomer(customerBuilder.build(ID, FIRST_NAME, LAST_NAME, EMAIL, "",
                        PHONE_NUMBER, location)),
                CUSTOMER_PASSWORD_EXCEPTION);

        assertThrowsExactly(ValidationException.class,
                () -> validator.validateCustomer(customerBuilder.build(ID, FIRST_NAME, LAST_NAME, EMAIL, "1234",
                        PHONE_NUMBER, location)),
                CUSTOMER_PASSWORD_EXCEPTION);
    }

    @Test
    void testCustomerValidationForPhoneNumber() {
        assertThrowsExactly(ValidationException.class,
                () -> validator.validateCustomer(customerBuilder.build(ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD,
                        "", location)),
                CUSTOMER_PHONE_NUMBER_EXCEPTION);

        assertThrowsExactly(ValidationException.class,
                () -> validator.validateCustomer(customerBuilder.build(ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD,
                        "12e456789w", location)),
                CUSTOMER_PHONE_NUMBER_EXCEPTION);

        assertThrowsExactly(ValidationException.class,
                () -> validator.validateCustomer(customerBuilder.build(ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD,
                        "09872", location)),
                CUSTOMER_PHONE_NUMBER_EXCEPTION);

        assertThrowsExactly(ValidationException.class,
                () -> validator.validateCustomer(customerBuilder.build(ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD,
                        "098123132372", location)),
                CUSTOMER_PHONE_NUMBER_EXCEPTION);
    }

    @Test
    void testCustomerValidationForLocation() {
        assertThrowsExactly(ValidationException.class,
                () -> validator.validateCustomer(customerBuilder.build(ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD,
                        PHONE_NUMBER, locationBuilder.build(COUNTRY, CITY, ""))),
                CUSTOMER_ADDRESS_EXCEPTION);

        assertThrowsExactly(ValidationException.class,
                () -> validator.validateCustomer(customerBuilder.build(ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD,
                        PHONE_NUMBER, locationBuilder.build(COUNTRY, CITY, "aicia"))),
                CUSTOMER_ADDRESS_EXCEPTION);
    }

    @Test
    void testCustomerValidationForMultipleFields() {
        assertThrowsExactly(ValidationException.class,
                () -> validator.validateCustomer(customerBuilder.build(ID, FIRST_NAME, "", EMAIL, "",
                        "", location)),
                CUSTOMER_LAST_NAME_EXCEPTION +
                        CUSTOMER_PASSWORD_EXCEPTION +
                        CUSTOMER_PHONE_NUMBER_EXCEPTION);

        assertThrowsExactly(ValidationException.class,
                () -> validator.validateCustomer(customerBuilder.build(ID, "", "", EMAIL, PASSWORD,
                        "", location)),
                CUSTOMER_FIRST_NAME_EXCEPTION +
                        CUSTOMER_LAST_NAME_EXCEPTION +
                        CUSTOMER_PHONE_NUMBER_EXCEPTION);

        assertThrowsExactly(ValidationException.class,
                () -> validator.validateCustomer(customerBuilder.build(ID, "", "", EMAIL, PASSWORD,
                        "", locationBuilder.build("Romania", "Cluj", ""))),
                CUSTOMER_FIRST_NAME_EXCEPTION +
                        CUSTOMER_LAST_NAME_EXCEPTION +
                        CUSTOMER_EMAIL_EXCEPTION +
                        CUSTOMER_PASSWORD_EXCEPTION +
                        CUSTOMER_PHONE_NUMBER_EXCEPTION +
                        CUSTOMER_ADDRESS_EXCEPTION);
    }

    @Test
    void testCustomerValidationForValidCustomer() throws ValidationException {
        List<String> errors;

        errors = validator.validateCustomer(customerBuilder.build(ID, FIRST_NAME, LAST_NAME,
                "berendi.rav2001@gmail.com", PASSWORD, PHONE_NUMBER, location));
        assertTrue(errors.isEmpty());
    }


    @Test
    void testValidCheckIfEmailExists() {
        assertTrue(customerService.checkIfEmailExists("br@gmail.com"));
        assertTrue(customerService.checkIfEmailExists("as@gmail.com"));
        assertTrue(customerService.checkIfEmailExists("br@gmail.com"));
        assertTrue(customerService.checkIfEmailExists("br@gmail.com"));
    }

    @Test
    void testInvalidCheckIfEmailExists() {
        assertFalse(customerService.checkIfEmailExists(""));
        assertFalse(customerService.checkIfEmailExists("berendirazvan@gmail.com"));
        assertFalse(customerService.checkIfEmailExists("@gmail.com"));
        assertFalse(customerService.checkIfEmailExists("br"));
        assertFalse(customerService.checkIfEmailExists("br@gmail.com1234"));
        assertFalse(customerService.checkIfEmailExists("1234br@gmail.com1234"));
        assertFalse(customerService.checkIfEmailExists("1234br@gmail.com"));
        assertFalse(customerService.checkIfEmailExists(" "));
        assertFalse(customerService.checkIfEmailExists(" br@gmail.com "));
    }

}