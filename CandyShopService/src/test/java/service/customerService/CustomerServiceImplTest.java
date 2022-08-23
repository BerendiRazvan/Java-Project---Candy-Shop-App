package service.customerService;

import domain.Customer;
import domain.location.Location;
import exception.ServiceException;
import org.junit.jupiter.api.*;
import repository.customerRepository.CustomerInMemoryRepository;
import repository.customerRepository.CustomerRepository;
import validator.CustomerValidator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static service.ConstantValues.*;

class CustomerServiceImplTest {
    private CustomerService customerService;
    private CustomerValidator validator;
    private Location location;

    @BeforeAll
    static void setUpAll() {
        System.out.println("Tests for CustomerServiceImpl");
    }

    @BeforeEach
    void setUp() {
        validator = new CustomerValidator();

        location = Location.builder()
                .country(COUNTRY)
                .city(CITY)
                .address(ADDRESS)
                .build();

        CustomerRepository customerRepository = CustomerInMemoryRepository.builder()
                .customerList(new ArrayList<>())
                .build();
        customerRepository.generateCustomers();

        customerService = CustomerServiceImpl.builder()
                .customerRepository(customerRepository)
                .build();
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
    void testValidCreateAccount() throws ServiceException {
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
        assertThrowsExactly(ServiceException.class,
                () -> customerService.createAccount(FIRST_NAME, LAST_NAME, "berendi.rav2001@gmail.com", PASSWORD,
                        "1234", location),
                CUSTOMER_PHONE_NUMBER_EXCEPTION);
    }

    @Test
    void testCustomerValidationForFirstName() throws InvocationTargetException, NoSuchMethodException,
            IllegalAccessException {
        String errors;

        errors = validator.validateCustomerFirstName("");
        assertEquals(errors, CUSTOMER_FIRST_NAME_EXCEPTION);

        errors = validator.validateCustomerFirstName("Razvan1234");
        assertEquals(errors, CUSTOMER_FIRST_NAME_EXCEPTION);
    }

    @Test
    void testCustomerValidationForLastName() throws NoSuchMethodException, SecurityException,
            InvocationTargetException, IllegalAccessException {
        String errors;

        errors = validator.validateCustomerLastName("");
        assertEquals(errors, CUSTOMER_LAST_NAME_EXCEPTION);

        errors = validator.validateCustomerLastName("B3r3ndi");
        assertEquals(errors, CUSTOMER_LAST_NAME_EXCEPTION);
    }

    @Test
    void testCustomerValidationForEmail() throws NoSuchMethodException, SecurityException,
            InvocationTargetException, IllegalAccessException {
        String errors;

        errors = validator.validateCustomerEmail("");
        assertEquals(errors, CUSTOMER_EMAIL_EXCEPTION);

        errors = validator.validateCustomerEmail("razvan.gmail.com");
        assertEquals(errors, CUSTOMER_EMAIL_EXCEPTION);

        errors = validator.validateCustomerEmail("@gmail.com");
        assertEquals(errors, CUSTOMER_EMAIL_EXCEPTION);

        errors = validator.validateCustomerEmail("gmail.com@");
        assertEquals(errors, CUSTOMER_EMAIL_EXCEPTION);
    }

    @Test
    void testCustomerValidationForPassword() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        String errors;

        errors = validator.validateCustomerPassword("");
        assertEquals(errors, CUSTOMER_PASSWORD_EXCEPTION);

        errors = validator.validateCustomerPassword("1234");
        assertEquals(errors, CUSTOMER_PASSWORD_EXCEPTION);
    }

    @Test
    void testCustomerValidationForPhoneNumber() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {

        String errors;

        errors = validator.validateCustomerPhoneNumber("");
        assertEquals(errors, CUSTOMER_PHONE_NUMBER_EXCEPTION);

        errors = validator.validateCustomerPhoneNumber("12e456789w");
        assertEquals(errors, CUSTOMER_PHONE_NUMBER_EXCEPTION);

        errors = validator.validateCustomerPhoneNumber("09872");
        assertEquals(errors, CUSTOMER_PHONE_NUMBER_EXCEPTION);

        errors = validator.validateCustomerPhoneNumber("098123132372");
        assertEquals(errors, CUSTOMER_PHONE_NUMBER_EXCEPTION);
    }

    @Test
    void testCustomerValidationForLocation() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        String errors;

        errors = validator.validateCustomerLocation(Location.builder()
                .country(COUNTRY)
                .city(CITY)
                .address("")
                .build());
        assertEquals(errors, CUSTOMER_ADDRESS_EXCEPTION);

        errors = validator.validateCustomerLocation(Location.builder()
                .country(COUNTRY)
                .city(CITY)
                .address("")
                .build());
        assertEquals(errors, CUSTOMER_ADDRESS_EXCEPTION);
    }

    @Test
    void testCustomerValidationForMultipleFields() throws NoSuchMethodException, SecurityException,
            InvocationTargetException, IllegalAccessException {

        List<String> errors;

        errors = validator.validateCustomer(Customer.builder()
                .firstName(FIRST_NAME)
                .lastName("")
                .email(EMAIL)
                .password("")
                .phoneNumber("")
                .location(location)
                .build());
        assertEquals(errors,
                List.of(CUSTOMER_LAST_NAME_EXCEPTION,
                        CUSTOMER_PASSWORD_EXCEPTION,
                        CUSTOMER_PHONE_NUMBER_EXCEPTION));

        errors = validator.validateCustomer(Customer.builder()
                .firstName("")
                .lastName("")
                .email(EMAIL)
                .password(PASSWORD)
                .phoneNumber("")
                .location(location)
                .build());
        assertEquals(errors,
                List.of(CUSTOMER_FIRST_NAME_EXCEPTION,
                        CUSTOMER_LAST_NAME_EXCEPTION,
                        CUSTOMER_PHONE_NUMBER_EXCEPTION));

        errors = validator.validateCustomer(Customer.builder()
                .firstName("")
                .lastName("")
                .email("")
                .password("")
                .phoneNumber("")
                .location(new Location("Romania", "Cluj", ""))
                .build());
        assertEquals(errors,
                List.of(CUSTOMER_FIRST_NAME_EXCEPTION,
                        CUSTOMER_LAST_NAME_EXCEPTION,
                        CUSTOMER_EMAIL_EXCEPTION,
                        CUSTOMER_PASSWORD_EXCEPTION,
                        CUSTOMER_PHONE_NUMBER_EXCEPTION,
                        CUSTOMER_ADDRESS_EXCEPTION));
    }

    @Test
    void testCustomerValidationForValidCustomer() throws NoSuchMethodException, SecurityException,
            InvocationTargetException, IllegalAccessException {
        List<String> errors;

        errors = validator.validateCustomer(Customer.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email("berendi.rav2001@gmail.com")
                .password(PASSWORD)
                .phoneNumber(PHONE_NUMBER)
                .location(location)
                .build());
        assertTrue(errors.isEmpty());
    }


    @Test
    void testValidCheckIfEmailExists() {
        assertTrue(customerService.checkIfEmailExists("br@gmail.com"));
        assertTrue(customerService.checkIfEmailExists("asasr@gmail.com"));
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