package service.customerService;

import domain.Customer;
import domain.location.Location;
import org.junit.jupiter.api.*;
import repository.customersRepository.CustomerInMemoryRepository;
import repository.customersRepository.CustomerRepository;
import service.exception.ServiceException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceImplTest {

    private static CustomerService customerService;

    private static final int ID = 1;
    private static final String FIRST_NAME = "Razvan";
    private static final String LAST_NAME = "Berendi";
    private static final String EMAIL = "br@gmail.com";
    private static final String PHONE_NUMBER = "0751578787";
    private static final String PASSWORD = "12345678";
    private static final Location LOCATION =
            new Location(1, "Romania", "Cluj", "Aleea Rucar nr. 9, Bloc D13, ap. 1");


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

    private void validTestsLogin() {
        try {
            Customer customer = customerService.login(EMAIL, PASSWORD);
            assertEquals(customer.getId(), ID);
            assertEquals(customer.getEmail(), EMAIL);
            assertEquals(customer.getFirstName(), FIRST_NAME);
            assertEquals(customer.getLastName(), LAST_NAME);
            assertEquals(customer.getPhoneNumber(), PHONE_NUMBER);
            assertEquals(customer.getPassword(), PASSWORD);
            assertEquals(customer.getCustomerLocation().getAddress(), LOCATION.getAddress());
        } catch (ServiceException e) {
            fail();
        }
    }

    private void invalidTestsLogin() {
        try {
            customerService.login("brazvan1234567890@gmail.com", PASSWORD);
            fail();
        } catch (ServiceException e) {
            assertEquals(e.getMessage(), "Authentication failed!");
        }

        try {
            customerService.login(EMAIL, "1234dasdas678");
            fail();
        } catch (ServiceException e) {
            assertEquals(e.getMessage(), "Invalid password!\n");
        }
    }


    @Test
    void login() {
        validTestsLogin();
        invalidTestsLogin();
    }

    private void validTestsCreateAccount() {
        try {
            Customer customer = customerService.createAccount(FIRST_NAME, LAST_NAME, "berendi.rav2001@gmail.com",
                    PASSWORD, PHONE_NUMBER, LOCATION);
            assertEquals(customer.getId(), 6);
            assertEquals(customer.getEmail(), "berendi.rav2001@gmail.com");
            assertEquals(customer.getFirstName(), FIRST_NAME);
            assertEquals(customer.getLastName(), LAST_NAME);
            assertEquals(customer.getPhoneNumber(), PHONE_NUMBER);
            assertEquals(customer.getPassword(), PASSWORD);
            assertEquals(customer.getCustomerLocation().getAddress(), LOCATION.getAddress());
        } catch (ServiceException e) {
            fail();
        }
    }

    private void invalidTestsCreateAccount() {
        try {
            Customer customer = customerService.createAccount(FIRST_NAME, LAST_NAME, "berendi.rav2001@gmail.com",
                    PASSWORD, "1234", LOCATION);
            fail();
        } catch (ServiceException e) {
            assertEquals(e.getMessage(), "Invalid phone number!\n");
        }
    }

    @Test
    void createAccount() {
        validTestsCreateAccount();
        invalidTestsCreateAccount();
    }

    private void verifCustomerFirstName() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        //private method - tested with reflection

        //args: String firstName, String lastName, String email, String password, String phoneNumber, Location location
        Method method = CustomerServiceImpl.class.getDeclaredMethod("verifCustomer",
                String.class, String.class, String.class, String.class, String.class, Location.class);
        method.setAccessible(true);

        String errors;

        errors = (String) method.invoke(customerService, "", FIRST_NAME, EMAIL, PASSWORD, PHONE_NUMBER, LOCATION);
        assertEquals(errors, "Invalid first name!\n");

        errors = (String) method.invoke(customerService, "Razvan1234", FIRST_NAME, EMAIL, PASSWORD, PHONE_NUMBER,
                LOCATION);
        assertEquals(errors, "Invalid first name!\n");
    }

    private void verifCustomerLastName() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        //private method - tested with reflection

        //args: String firstName, String lastName, String email, String password, String phoneNumber, Location location
        Method method = CustomerServiceImpl.class.getDeclaredMethod("verifCustomer",
                String.class, String.class, String.class, String.class, String.class, Location.class);
        method.setAccessible(true);

        String errors;

        errors = (String) method.invoke(customerService, FIRST_NAME, "", EMAIL, PASSWORD, PHONE_NUMBER, LOCATION);
        assertEquals(errors, "Invalid last name!\n");

        errors = (String) method.invoke(customerService, FIRST_NAME, "B3r3ndi", EMAIL, PASSWORD, PHONE_NUMBER,
                LOCATION);
        assertEquals(errors, "Invalid last name!\n");

    }

    private void verifCustomerMail() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        //private method - tested with reflection

        //args: String firstName, String lastName, String email, String password, String phoneNumber, Location location
        Method method = CustomerServiceImpl.class.getDeclaredMethod("verifCustomer",
                String.class, String.class, String.class, String.class, String.class, Location.class);
        method.setAccessible(true);

        String errors;

        errors = (String) method.invoke(customerService, FIRST_NAME, LAST_NAME, "", PASSWORD, PHONE_NUMBER,
                LOCATION);
        assertEquals(errors, "Invalid email!\n");

        errors = (String) method.invoke(customerService, FIRST_NAME, LAST_NAME, "razvan.gmail.com", PASSWORD,
                PHONE_NUMBER, LOCATION);
        assertEquals(errors, "Invalid email!\n");

        errors = (String) method.invoke(customerService, FIRST_NAME, LAST_NAME, "@gmail.com", PASSWORD,
                PHONE_NUMBER, LOCATION);
        assertEquals(errors, "Invalid email!\n");

        errors = (String) method.invoke(customerService, FIRST_NAME, LAST_NAME, "gmail.com@", PASSWORD,
                PHONE_NUMBER, LOCATION);
        assertEquals(errors, "Invalid email!\n");
    }

    private void verifCustomerPassword() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        //private method - tested with reflection

        //args: String firstName, String lastName, String email, String password, String phoneNumber, Location location
        Method method = CustomerServiceImpl.class.getDeclaredMethod("verifCustomer",
                String.class, String.class, String.class, String.class, String.class, Location.class);
        method.setAccessible(true);

        String errors;

        errors = (String) method.invoke(customerService, FIRST_NAME, LAST_NAME, EMAIL, "", PHONE_NUMBER,
                LOCATION);
        assertEquals(errors, "Invalid password!\n");

        errors = (String) method.invoke(customerService, FIRST_NAME, LAST_NAME, EMAIL, "1234", PHONE_NUMBER,
                LOCATION);
        assertEquals(errors, "Invalid password!\n");
    }

    private void verifCustomerPhoneNumber() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        //private method - tested with reflection

        //args: String firstName, String lastName, String email, String password, String phoneNumber, Location location
        Method method = CustomerServiceImpl.class.getDeclaredMethod("verifCustomer",
                String.class, String.class, String.class, String.class, String.class, Location.class);
        method.setAccessible(true);

        String errors;

        errors = (String) method.invoke(customerService, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, "",
                new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));
        assertEquals(errors, "Invalid phone number!\n");

        errors = (String) method.invoke(customerService, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD,
                "12e456789w", LOCATION);
        assertEquals(errors, "Invalid phone number!\n");

        errors = (String) method.invoke(customerService, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD,
                "09872", LOCATION);
        assertEquals(errors, "Invalid phone number!\n");

        errors = (String) method.invoke(customerService, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD,
                "098123132372", LOCATION);

        assertEquals(errors, "Invalid phone number!\n");
    }

    private void verifCustomerLocation() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        //private method - tested with reflection

        //args: String firstName, String lastName, String email, String password, String phoneNumber, Location location
        Method method = CustomerServiceImpl.class.getDeclaredMethod("verifCustomer",
                String.class, String.class, String.class, String.class, String.class, Location.class);
        method.setAccessible(true);

        String errors;

        errors = (String) method.invoke(customerService, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, PHONE_NUMBER,
                new Location(1, "Romania", "Cluj", ""));

        assertEquals(errors, "Invalid address!\n");

        errors = (String) method.invoke(customerService, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, PHONE_NUMBER,
                new Location(1, "Romania", "Cluj", "aproape"));

        assertEquals(errors, "Invalid address!\n");
    }

    private void verifCustomerMultipleFields() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        //private method - tested with reflection

        //args: String firstName, String lastName, String email, String password, String phoneNumber, Location location
        Method method = CustomerServiceImpl.class.getDeclaredMethod("verifCustomer",
                String.class, String.class, String.class, String.class, String.class, Location.class);
        method.setAccessible(true);

        String errors;

        errors = (String) method.invoke(customerService, FIRST_NAME, "", EMAIL, "", "", LOCATION);
        assertEquals(errors, "Invalid last name!\n" +
                "Invalid password!\n" +
                "Invalid phone number!\n");

        errors = (String) method.invoke(customerService, "", "", EMAIL, PASSWORD, "", LOCATION);
        assertEquals(errors, "Invalid first name!\n" +
                "Invalid last name!\n" +
                "Invalid phone number!\n");

        errors = (String) method.invoke(customerService, "", "", "", "", "",
                new Location(1, "Romania", "Cluj", ""));
        assertEquals(errors, "Invalid first name!\n" +
                "Invalid last name!\n" +
                "Invalid email!\n" +
                "Invalid password!\n" +
                "Invalid phone number!\n" +
                "Invalid address!\n");
    }

    private void verifValidCustomer() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        //private method - tested with reflection

        //args: String firstName, String lastName, String email, String password, String phoneNumber, Location location
        Method method = CustomerServiceImpl.class.getDeclaredMethod("verifCustomer",
                String.class, String.class, String.class, String.class, String.class, Location.class);
        method.setAccessible(true);

        String errors;

        errors = (String) method.invoke(customerService, FIRST_NAME, LAST_NAME, "berendi.rav2001@gmail.com",
                PASSWORD, PHONE_NUMBER, LOCATION);
        assertEquals(errors, "");
    }


    @Test
    void verifCustomer() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        verifValidCustomer();
        verifCustomerFirstName();
        verifCustomerLastName();
        verifCustomerMail();
        verifCustomerPassword();
        verifCustomerPhoneNumber();
        verifCustomerLocation();
        verifCustomerMultipleFields();
    }

    private void validTestsFindMail() {
        assertTrue(customerService.findMail("br@gmail.com"));
        assertTrue(customerService.findMail("asasr@gmail.com"));
        assertTrue(customerService.findMail("br@gmail.com"));
        assertTrue(customerService.findMail("br@gmail.com"));
    }

    private void invalidTestsFindMail() {
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

    @Test
    void findMail() {
        validTestsFindMail();
        invalidTestsFindMail();
    }
}