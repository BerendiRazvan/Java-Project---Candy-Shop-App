package service.customerService;

import domain.Customer;
import domain.location.Location;
import org.junit.jupiter.api.*;
import repository.customerRepository.CustomerInMemoryRepository;
import repository.customerRepository.CustomerRepository;
import service.exception.ServiceException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;
import static service.ConstantValues.*;

class CustomerServiceImplTest {
    private CustomerService customerService;
    private Location location;

    @BeforeAll
    static void setUpAll() {
        System.out.println("Tests for CustomerServiceImpl");
    }

    @BeforeEach
    void setUp() {
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
        Customer customer = customerService.login(EMAIL, PASSWORD);
        assertEquals(customer.getId(), ID);
        assertEquals(customer.getEmail(), EMAIL);
        assertEquals(customer.getFirstName(), FIRST_NAME);
        assertEquals(customer.getLastName(), LAST_NAME);
        assertEquals(customer.getPhoneNumber(), PHONE_NUMBER);
        assertEquals(customer.getPassword(), PASSWORD);
        assertEquals(customer.getLocation().getAddress(), ADDRESS);
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
        Customer customer = customerService.createAccount(FIRST_NAME, LAST_NAME, "berendi.rav2001@gmail.com",
                PASSWORD, PHONE_NUMBER, location);
        assertEquals(customer.getId(), 6);
        assertEquals(customer.getEmail(), "berendi.rav2001@gmail.com");
        assertEquals(customer.getFirstName(), FIRST_NAME);
        assertEquals(customer.getLastName(), LAST_NAME);
        assertEquals(customer.getPhoneNumber(), PHONE_NUMBER);
        assertEquals(customer.getPassword(), PASSWORD);
        assertEquals(customer.getLocation().getAddress(), ADDRESS);
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

        errors = getCustomerValidation("", FIRST_NAME, EMAIL, PASSWORD, PHONE_NUMBER, location);
        assertEquals(errors, CUSTOMER_FIRST_NAME_EXCEPTION);

        errors = getCustomerValidation("Razvan1234", FIRST_NAME, EMAIL, PASSWORD, PHONE_NUMBER, location);
        assertEquals(errors, CUSTOMER_FIRST_NAME_EXCEPTION);
    }

    @Test
    void testCustomerValidationForLastName() throws NoSuchMethodException, SecurityException,
            InvocationTargetException, IllegalAccessException {
        String errors;

        errors = getCustomerValidation(FIRST_NAME, "", EMAIL, PASSWORD, PHONE_NUMBER, location);
        assertEquals(errors, CUSTOMER_LAST_NAME_EXCEPTION);

        errors = getCustomerValidation(FIRST_NAME, "B3r3ndi", EMAIL, PASSWORD, PHONE_NUMBER, location);
        assertEquals(errors, CUSTOMER_LAST_NAME_EXCEPTION);
    }

    @Test
    void testCustomerValidationForEmail() throws NoSuchMethodException, SecurityException,
            InvocationTargetException, IllegalAccessException {
        String errors;

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, "", PASSWORD, PHONE_NUMBER, location);
        assertEquals(errors, CUSTOMER_EMAIL_EXCEPTION);

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, "razvan.gmail.com", PASSWORD, PHONE_NUMBER, location);
        assertEquals(errors, CUSTOMER_EMAIL_EXCEPTION);

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, "@gmail.com", PASSWORD, PHONE_NUMBER, location);
        assertEquals(errors, CUSTOMER_EMAIL_EXCEPTION);

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, "gmail.com@", PASSWORD, PHONE_NUMBER, location);
        assertEquals(errors, CUSTOMER_EMAIL_EXCEPTION);
    }

    @Test
    void testCustomerValidationForPassword() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        String errors;

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, EMAIL, "", PHONE_NUMBER, location);
        assertEquals(errors, CUSTOMER_PASSWORD_EXCEPTION);

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, EMAIL, "1234", PHONE_NUMBER, location);
        assertEquals(errors, CUSTOMER_PASSWORD_EXCEPTION);
    }

    @Test
    void testCustomerValidationForPhoneNumber() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {

        String errors;

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, "", location);
        assertEquals(errors, CUSTOMER_PHONE_NUMBER_EXCEPTION);

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, "12e456789w", location);
        assertEquals(errors, CUSTOMER_PHONE_NUMBER_EXCEPTION);

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, "09872", location);
        assertEquals(errors, CUSTOMER_PHONE_NUMBER_EXCEPTION);

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, "098123132372", location);
        assertEquals(errors, CUSTOMER_PHONE_NUMBER_EXCEPTION);
    }

    @Test
    void testCustomerValidationForLocation() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        String errors;

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, PHONE_NUMBER,
                Location.builder()
                        .country(COUNTRY)
                        .city(CITY)
                        .address("")
                        .build());
        assertEquals(errors, CUSTOMER_ADDRESS_EXCEPTION);

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, PHONE_NUMBER,
                Location.builder()
                        .country(COUNTRY)
                        .city(CITY)
                        .address("aproape")
                        .build());
        assertEquals(errors, CUSTOMER_ADDRESS_EXCEPTION);
    }

    @Test
    void testCustomerValidationForMultipleFields() throws NoSuchMethodException, SecurityException,
            InvocationTargetException, IllegalAccessException {

        String errors;

        errors = getCustomerValidation(FIRST_NAME, "", EMAIL, "", "", location);
        assertEquals(errors,
                CUSTOMER_LAST_NAME_EXCEPTION +
                        CUSTOMER_PASSWORD_EXCEPTION +
                        CUSTOMER_PHONE_NUMBER_EXCEPTION);

        errors = getCustomerValidation("", "", EMAIL, PASSWORD, "", location);
        assertEquals(errors,
                CUSTOMER_FIRST_NAME_EXCEPTION +
                        CUSTOMER_LAST_NAME_EXCEPTION +
                        CUSTOMER_PHONE_NUMBER_EXCEPTION);

        errors = getCustomerValidation("", "", "", "", "",
                new Location("Romania", "Cluj", ""));
        assertEquals(errors,
                CUSTOMER_FIRST_NAME_EXCEPTION +
                        CUSTOMER_LAST_NAME_EXCEPTION +
                        CUSTOMER_EMAIL_EXCEPTION +
                        CUSTOMER_PASSWORD_EXCEPTION +
                        CUSTOMER_PHONE_NUMBER_EXCEPTION +
                        CUSTOMER_ADDRESS_EXCEPTION);
    }

    @Test
    void testCustomerValidationForValidCustomer() throws NoSuchMethodException, SecurityException,
            InvocationTargetException, IllegalAccessException {
        String errors;

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, "berendi.rav2001@gmail.com",
                PASSWORD, PHONE_NUMBER, location);
        assertEquals(errors, "");
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

    private String getCustomerValidation(String firstName, String lastName, String email, String password,
                                         String phoneNumber, Location location) throws NoSuchMethodException,
            SecurityException, InvocationTargetException, IllegalAccessException {
        //private method - tested with reflection

        //args: String firstName, String lastName, String email, String password, String phoneNumber, Location location
        Method method = CustomerServiceImpl.class.getDeclaredMethod("customerValidation",
                String.class, String.class, String.class, String.class, String.class, Location.class);
        method.setAccessible(true);

        return (String) method.invoke(customerService, firstName, lastName, email, password, phoneNumber, location);
    }

}