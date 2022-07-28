package service.customerService;

import domain.Customer;
import domain.location.Location;
import repository.customersRepository.CustomerInMemoryRepository;
import repository.customersRepository.CustomerRepository;
import service.exception.ServiceException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceImplTest {

    private static CustomerService customerService;

    @org.junit.jupiter.api.BeforeAll
    static void setUpAll() {
        System.out.println("Tests for CustomerServiceImpl");

        CustomerRepository customerRepository =
                new CustomerInMemoryRepository(CustomerInMemoryRepository.generateCustomers());
        customerService = new CustomerServiceImpl(customerRepository);
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.AfterAll
    static void tearDownAll() {
        System.out.println("Tests passed");
    }


    @org.junit.jupiter.api.Test
    void login() {

        try {
            Customer customer = customerService.login("br@gmail.com", "12345678");
            assertEquals(customer.getIdCustomer(), 1);
            assertEquals(customer.getEmail(), "br@gmail.com");
            assertEquals(customer.getFirstName(), "Razvan");
            assertEquals(customer.getLastName(), "Berendi");
            assertEquals(customer.getPhoneNumber(), "0751578787");
            assertEquals(customer.getPassword(), "12345678");
            assertEquals(customer.getCustomerLocation().getAddress(), "Aleea Rucar nr. 9, Bloc D13, ap. 1");
        } catch (ServiceException e) {
            fail();
        }

        try {
            customerService.login("brazvan1234567890@gmail.com", "12345678");
            fail();
        } catch (ServiceException e) {
            assertEquals(e.getMessage(), "Authentication failed!");
        }

        try {
            customerService.login("br@gmail.com", "1234dasdas678");
            fail();
        } catch (ServiceException e) {
            assertEquals(e.getMessage(), "Invalid password!\n");
        }


    }

    @org.junit.jupiter.api.Test
    void createAccount() {

        try {
            Customer customer = customerService.createAccount("Razvan", "Berendi", "berendi.rav2001@gmail.com",
                    "1234567890", "0751578787",
                    new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));
            assertEquals(customer.getIdCustomer(), 6);
            assertEquals(customer.getEmail(), "berendi.rav2001@gmail.com");
            assertEquals(customer.getFirstName(), "Razvan");
            assertEquals(customer.getLastName(), "Berendi");
            assertEquals(customer.getPhoneNumber(), "0751578787");
            assertEquals(customer.getPassword(), "1234567890");
            assertEquals(customer.getCustomerLocation().getAddress(), "Strada Peana nr. 10, bloc F7, ap. 5");
        } catch (Exception e) {
            fail();
        }

        try {
            Customer customer = customerService.createAccount("Razvan", "Berendi", "berendi.rav2001@gmail.com",
                    "1234567890", "1234",
                    new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));
            fail();
        } catch (ServiceException e) {
            assertEquals(e.getMessage(), "Invalid phone number!\n");
        }


    }


    @org.junit.jupiter.api.Test
    void verifCustomer() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        //private method - tested with reflection

        //args: String firstName, String lastName, String email, String password, String phoneNumber, Location location
        Method method = CustomerServiceImpl.class.getDeclaredMethod("verifCustomer",
                String.class, String.class, String.class, String.class, String.class, Location.class);
        method.setAccessible(true);

        String errors;

        errors = (String) method.invoke(customerService, "Razvan", "Berendi", "berendi.rav2001@gmail.com",
                "1234567890", "0751578787",
                new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));
        assertEquals(errors, "");

        errors = (String) method.invoke(customerService, "", "Berendi", "br@gmail.com", "1234567890", "0751578787",
                new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));
        assertEquals(errors, "Invalid first name!\n");

        errors = (String) method.invoke(customerService, "Razvan1234", "Berendi", "br@gmail.com", "1234567890",
                "0751578787",
                new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));
        assertEquals(errors, "Invalid first name!\n");

        errors = (String) method.invoke(customerService, "Razvan", "", "br@gmail.com", "1234567890", "0751578787",
                new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));
        assertEquals(errors, "Invalid last name!\n");

        errors = (String) method.invoke(customerService, "Razvan", "B3r3ndi", "br@gmail.com", "1234567890",
                "0751578787",
                new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));
        assertEquals(errors, "Invalid last name!\n");

        errors = (String) method.invoke(customerService, "Razvan", "Berendi", "", "1234567890", "0751578787",
                new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));
        assertEquals(errors, "Invalid email!\n");

        errors = (String) method.invoke(customerService, "Razvan", "Berendi", "razvan.gmail.com", "1234567890",
                "0751578787",
                new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));
        assertEquals(errors, "Invalid email!\n");

        errors = (String) method.invoke(customerService, "Razvan", "Berendi", "@gmail.com", "1234567890",
                "0751578787",
                new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));
        assertEquals(errors, "Invalid email!\n");

        errors = (String) method.invoke(customerService, "Razvan", "Berendi", "gmail.com@", "1234567890",
                "0751578787",
                new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));
        assertEquals(errors, "Invalid email!\n");

        errors = (String) method.invoke(customerService, "Razvan", "Berendi", "br@gmail.com", "", "0751578787",
                new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));
        assertEquals(errors, "Invalid password!\n");

        errors = (String) method.invoke(customerService, "Razvan", "Berendi", "br@gmail.com", "1234",
                "0751578787",
                new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));
        assertEquals(errors, "Invalid password!\n");

        errors = (String) method.invoke(customerService, "Razvan", "Berendi", "br@gmail.com", "1234567890", "",
                new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));
        assertEquals(errors, "Invalid phone number!\n");

        errors = (String) method.invoke(customerService, "Razvan", "Berendi", "br@gmail.com", "1234567890",
                "12e456789w", new Location(1, "Romania", "Cluj",
                        "Strada Peana nr. 10, bloc F7, ap. 5"));
        assertEquals(errors, "Invalid phone number!\n");

        errors = (String) method.invoke(customerService, "Razvan", "Berendi", "br@gmail.com", "1234567890",
                "09872", new Location(1, "Romania", "Cluj",
                        "Strada Peana nr. 10, bloc F7, ap. 5"));
        assertEquals(errors, "Invalid phone number!\n");

        errors = (String) method.invoke(customerService, "Razvan", "Berendi", "br@gmail.com", "1234567890",
                "098123132372", new Location(1, "Romania", "Cluj",
                        "Strada Peana nr. 10, bloc F7, ap. 5"));
        assertEquals(errors, "Invalid phone number!\n");

        errors = (String) method.invoke(customerService, "Razvan", "Berendi", "br@gmail.com", "1234567890",
                "0751578787", new Location(1, "Romania", "Cluj", ""));
        assertEquals(errors, "Invalid address!\n");


        errors = (String) method.invoke(customerService, "Razvan", "", "br@gmail.com", "", "",
                new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));
        assertEquals(errors, "Invalid last name!\n" +
                "Invalid password!\n" +
                "Invalid phone number!\n");

        errors = (String) method.invoke(customerService, "", "", "br@gmail.com", "1234567890", "",
                new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));
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

    @org.junit.jupiter.api.Test
    void findMail() {

        assertTrue(customerService.findMail("br@gmail.com"));
        assertTrue(customerService.findMail("asasr@gmail.com"));
        assertTrue(customerService.findMail("br@gmail.com"));
        assertTrue(customerService.findMail("br@gmail.com"));

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
}