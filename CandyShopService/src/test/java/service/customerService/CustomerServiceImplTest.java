package service.customerService;

import domain.Customer;
import domain.location.Location;
import org.junit.jupiter.api.*;
import repository.customersRepository.CustomerInMemoryRepository;
import repository.customersRepository.CustomerRepository;
import service.exception.ServiceException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


import static org.junit.jupiter.api.Assertions.*;
import static service.TestConstantValues.*;

class CustomerServiceImplTest {
    private static CustomerService customerService;
    private final Location location = new Location(ID, COUNTRY, CITY, ADDRESS);

    @BeforeAll
    static void setUpAll() {
        System.out.println("Tests for CustomerServiceImpl");

        CustomerRepository customerRepository =
                new CustomerInMemoryRepository(CustomerInMemoryRepository.generateCustomers());
        customerService = new CustomerServiceImpl(customerRepository);
    }

    @BeforeEach
    void setUp() {
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
                "Authentication failed!");

        assertThrowsExactly(ServiceException.class,
                () -> customerService.login(EMAIL, "1234dasdas678"),
                "Invalid password!\n");
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
                "Invalid phone number!\n");
    }

    @Test
    void testCustomerValidationForFirstName() throws InvocationTargetException, NoSuchMethodException,
            IllegalAccessException {
        String errors;

        errors = getCustomerValidation("", FIRST_NAME, EMAIL, PASSWORD, PHONE_NUMBER, location);
        assertEquals(errors, "Invalid first name!\n");

        errors = getCustomerValidation("Razvan1234", FIRST_NAME, EMAIL, PASSWORD, PHONE_NUMBER, location);
        assertEquals(errors, "Invalid first name!\n");
    }

    @Test
    void testCustomerValidationForLastName() throws NoSuchMethodException, SecurityException,
            InvocationTargetException, IllegalAccessException {
        String errors;

        errors = getCustomerValidation(FIRST_NAME, "", EMAIL, PASSWORD, PHONE_NUMBER, location);
        assertEquals(errors, "Invalid last name!\n");

        errors = getCustomerValidation(FIRST_NAME, "B3r3ndi", EMAIL, PASSWORD, PHONE_NUMBER, location);
        assertEquals(errors, "Invalid last name!\n");
    }

    @Test
    void testCustomerValidationForEmail() throws NoSuchMethodException, SecurityException,
            InvocationTargetException, IllegalAccessException {
        String errors;

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, "", PASSWORD, PHONE_NUMBER, location);
        assertEquals(errors, "Invalid email!\n");

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, "razvan.gmail.com", PASSWORD, PHONE_NUMBER, location);
        assertEquals(errors, "Invalid email!\n");

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, "@gmail.com", PASSWORD, PHONE_NUMBER, location);
        assertEquals(errors, "Invalid email!\n");

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, "gmail.com@", PASSWORD, PHONE_NUMBER, location);
        assertEquals(errors, "Invalid email!\n");
    }

    @Test
    void testCustomerValidationForPassword() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        String errors;

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, EMAIL, "", PHONE_NUMBER, location);
        assertEquals(errors, "Invalid password!\n");

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, EMAIL, "1234", PHONE_NUMBER, location);
        assertEquals(errors, "Invalid password!\n");
    }

    @Test
    void testCustomerValidationForPhoneNumber() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {

        String errors;

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, "", location);
        assertEquals(errors, "Invalid phone number!\n");

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, "12e456789w", location);
        assertEquals(errors, "Invalid phone number!\n");

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, "09872", location);
        assertEquals(errors, "Invalid phone number!\n");

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, "098123132372", location);
        assertEquals(errors, "Invalid phone number!\n");
    }

    @Test
    void testCustomerValidationForLocation() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        String errors;

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, PHONE_NUMBER,
                new Location(1, "Romania", "Cluj", ""));
        assertEquals(errors, "Invalid address!\n");

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, PHONE_NUMBER,
                new Location(1, "Romania", "Cluj", "aproape"));
        assertEquals(errors, "Invalid address!\n");
    }

    @Test
    void testCustomerValidationForMultipleFields() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {

        String errors;

        errors = getCustomerValidation(FIRST_NAME, "", EMAIL, "", "", location);
        assertEquals(errors, "Invalid last name!\n" +
                "Invalid password!\n" +
                "Invalid phone number!\n");

        errors = getCustomerValidation("", "", EMAIL, PASSWORD, "", location);
        assertEquals(errors, "Invalid first name!\n" +
                "Invalid last name!\n" +
                "Invalid phone number!\n");

        errors = getCustomerValidation("", "", "", "", "",
                new Location(1, "Romania", "Cluj", ""));
        assertEquals(errors, "Invalid first name!\n" +
                "Invalid last name!\n" +
                "Invalid email!\n" +
                "Invalid password!\n" +
                "Invalid phone number!\n" +
                "Invalid address!\n");
    }

    @Test
    void testCustomerValidationForValidCustomer() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        String errors;

        errors = getCustomerValidation(FIRST_NAME, LAST_NAME, "berendi.rav2001@gmail.com",
                PASSWORD, PHONE_NUMBER, location);
        assertEquals(errors, "");
    }


    @Test
    void testValidFindMail() {
        assertTrue(customerService.findMail("br@gmail.com"));
        assertTrue(customerService.findMail("asasr@gmail.com"));
        assertTrue(customerService.findMail("br@gmail.com"));
        assertTrue(customerService.findMail("br@gmail.com"));
    }

    @Test
    void testInvalidFindMail() {
        assertFalse(customerService.findMail(""));
        assertFalse(customerService.findMail("berendirazvan@gmail.com"));
        assertFalse(customerService.findMail("@gmail.com"));
        assertFalse(customerService.findMail("br"));
        assertFalse(customerService.findMail("br@gmail.com1234"));
        assertFalse(customerService.findMail("1234br@gmail.com1234"));
        assertFalse(customerService.findMail("1234br@gmail.com"));
        assertFalse(customerService.findMail(" "));
        assertFalse(customerService.findMail(" br@gmail.com "));
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