package service.orderService;

import domain.Customer;
import domain.Shop;
import domain.location.Location;
import domain.order.Order;
import domain.order.OrderType;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import domain.sweet.SweetType;
import org.junit.jupiter.api.*;
import repository.exception.RepositoryException;
import repository.ordersRepository.OrderInMemoryRepository;
import repository.ordersRepository.OrderRepository;
import service.exception.ServiceException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {
    private static final String COUNTRY = "Romania";
    private static final String CITY = "Cluj";
    private static final String ADDRESS = "Aleea Rucar nr. 9, Bloc D13, ap. 1";
    private static final String SHOP_NAME = "Candy Crush Shop";
    private static final int ID = 1;
    private static final String FIRST_NAME = "Razvan";
    private static final String LAST_NAME = "Berendi";
    private static final String EMAIL = "br@gmail.com";
    private static final String PHONE_NUMBER = "0751578787";
    private static final String PASSWORD = "12345678";
    private static final double SWEET_PRICE = 5;
    private final Shop myShop = new Shop(SHOP_NAME, new Location(ID, COUNTRY, CITY, ADDRESS));
    private final Customer customer = new Customer(ID, FIRST_NAME, LAST_NAME,
            EMAIL, PASSWORD, PHONE_NUMBER, new Location(ID, COUNTRY, CITY, ADDRESS));
    private final Sweet sweet = new Sweet(ID,
            new ArrayList<>(List.of(
                    new Ingredient(1, "Sugar", 1.5),
                    new Ingredient(2, "Milk", 1),
                    new Ingredient(3, "Flour", 0.75))),
            SweetType.DONUT, SWEET_PRICE);
    private static OrderService orderService;

    @BeforeAll
    static void setUpAll() {
        System.out.println("Tests for OrderServiceImpl");
    }

    @BeforeEach
    void setUp() {
        OrderRepository orderRepository = new OrderInMemoryRepository(new ArrayList<>());
        try {
            orderRepository.add(new Order(1, new HashMap<>(), OrderType.DELIVERY, customer, myShop));
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        orderService = new OrderServiceImpl(orderRepository);
    }

    @AfterEach
    void tearDown() {

    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Tests passed");
    }


    @Test
    void testCreateOrder() throws ServiceException {
        Order order = orderService.createOrder(customer, OrderType.DELIVERY, myShop);

        assertEquals(order.getCustomer(), customer);
        assertEquals(order.getShop(), myShop);
        assertEquals(order.getOrderType(), OrderType.DELIVERY);
        assertTrue(order.getOrderedSweets().isEmpty());
    }

    @Test
    void testValidAddToOrder() throws ServiceException {
        Order order = orderService.createOrder(customer, OrderType.DELIVERY, myShop);

        double moneyMade = orderService.getMoneyMadeToday();
        orderService.addToOrder(order, sweet);

        assertEquals(moneyMade + sweet.getPrice(), orderService.getMoneyMadeToday());

        orderService.addToOrder(order, sweet);
        orderService.addToOrder(order, sweet);

        assertEquals(moneyMade + sweet.getPrice() * 3, orderService.getMoneyMadeToday());
    }

    @Test
    void testInvalidAddToOrder() {
        assertThrowsExactly(ServiceException.class,
                () -> orderService.addToOrder(
                        orderService.createOrder(customer, OrderType.DELIVERY, myShop), null),
                "Invalid sweet id!");
    }


    @Test
    void testGetOrderDetails() throws ServiceException {
        String result = orderService.getOrderDetails(1);
        assertEquals(result, orderService.printOrderDetails("1"));
    }


    @Test
    void testValidRemoveOrder() throws ServiceException {
        assertEquals(orderService.getAllOrdersInADay().size(), 1);
        orderService.removeOrder(1L);
        assertEquals(orderService.getAllOrdersInADay().size(), 0);
    }

    @Test
    void testInvalidRemoveOrder() throws ServiceException {
        orderService.removeOrder(1L);
        assertThrowsExactly(ServiceException.class,
                () -> orderService.removeOrder(1L),
                "This element does not exist!");
        assertEquals(orderService.getAllOrdersInADay().size(), 0);
    }


    @Test
    void testGetAllOrdersInADay() throws ServiceException {
        assertEquals(orderService.getAllOrdersInADay().size(), 1);

        orderService.createOrder(customer, OrderType.DELIVERY, myShop);
        orderService.createOrder(customer, OrderType.DELIVERY, myShop);
        orderService.createOrder(customer, OrderType.DELIVERY, myShop);
        orderService.createOrder(customer, OrderType.DELIVERY, myShop);

        assertEquals(orderService.getAllOrdersInADay().size(), 5);
        for (int i = 0; i < orderService.getAllOrdersInADay().size(); i++)
            assertEquals(orderService.getAllOrdersInADay().get(i).getId(), i + 1);
    }

    @Test
    void testGetMoneyMadeToday() throws ServiceException {
        assertEquals(orderService.getMoneyMadeToday(), 0);
        orderService.addToOrder(orderService.getAllOrdersInADay().get(0), sweet);
        assertEquals(orderService.getMoneyMadeToday(), 5);
    }

    @Test
    void testGetProfitMadeToday() throws ServiceException {
        assertEquals(orderService.getProfitMadeToday(), 0);
        orderService.addToOrder(orderService.getAllOrdersInADay().get(0), sweet);
        assertEquals(orderService.getProfitMadeToday(), 1.75);
    }

    @Test
    void testValidPrintOrderDetails() throws ServiceException {
        String result = orderService.printOrderDetails("1");
        assertEquals(result, orderService.getOrderDetails(1));
    }

    @Test
    void testInvalidPrintOrderDetails() {
        assertThrowsExactly(ServiceException.class,
                () -> orderService.printOrderDetails("1234567"),
                "Invalid order number/id!");
        assertThrowsExactly(ServiceException.class,
                () -> orderService.printOrderDetails("adsasdads"),
                "Invalid order number/id!");
    }

    @Test
    void testGetProfit() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException, ServiceException {
        //private method - tested with reflection

        //args: Map<Sweet, Integer> orderedSweets
        Method method = OrderServiceImpl.class.getDeclaredMethod("getProfit",
                Map.class);
        method.setAccessible(true);

        double result = (double) method.invoke(orderService,
                orderService.getAllOrdersInADay().get(0).getOrderedSweets());
        assertEquals(result, 0);

        orderService.addToOrder(orderService.getAllOrdersInADay().get(0), sweet);
        result = (double) method.invoke(orderService,
                orderService.getAllOrdersInADay().get(0).getOrderedSweets());
        assertEquals(result, 1.75);
    }


    @Test
    void addOneSweetToOrder() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        //private method - tested with reflection

        //args: Order order, Sweet sweet
        Method method1 = OrderServiceImpl.class.getDeclaredMethod("addSweetToOrder",
                Order.class, Sweet.class);
        method1.setAccessible(true);

        assertTrue(orderService.getAllOrdersInADay().get(0).getOrderedSweets().isEmpty());

        method1.invoke(orderService, orderService.getAllOrdersInADay().get(0), sweet);

        assertTrue(orderService.getAllOrdersInADay().get(0).getOrderedSweets().containsKey(sweet));
        assertEquals(orderService.getAllOrdersInADay().get(0).getOrderedSweets().get(sweet), 1);
    }

    @Test
    void testAddQuantityOfSweetToOrder() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        //private method - tested with reflection

        //args: Order order, Sweet sweet, int quantity
        Method method2 = OrderServiceImpl.class.getDeclaredMethod("addSweetToOrder",
                Order.class, Sweet.class, int.class);
        method2.setAccessible(true);

        method2.invoke(orderService, orderService.getAllOrdersInADay().get(0), sweet, 5);

        assertTrue(orderService.getAllOrdersInADay().get(0).getOrderedSweets().containsKey(sweet));
        assertEquals(orderService.getAllOrdersInADay().get(0).getOrderedSweets().get(sweet), 5);

    }

}