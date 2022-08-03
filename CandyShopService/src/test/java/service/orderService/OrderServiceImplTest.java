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

    private static final Shop MY_SHOP = new Shop("Candy Crush Shop",
            new Location(1, "Romania", "Cluj-Napoca", "Str. Memorandumului, nr. 10"));
    private static final Customer CUSTOMER = new Customer(1, "Razvan", "Berendi",
            "berendi.rav2001@gmail.com", "1234567890", "0751578787",
            new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));
    private static final Sweet SWEET = new Sweet(1,
            new ArrayList<>(List.of(
                    new Ingredient(1, "Sugar", 1.5),
                    new Ingredient(2, "Milk", 1),
                    new Ingredient(3, "Flour", 0.75))),
            SweetType.DONUT, 5);
    private static OrderService orderService;

    @BeforeAll
    static void setUpAll() {
        System.out.println("Tests for OrderServiceImpl");
    }

    @BeforeEach
    void setUp() {
        OrderRepository orderRepository = new OrderInMemoryRepository(new ArrayList<>());
        try {
            orderRepository.add(new Order(1, new HashMap<>(), OrderType.DELIVERY, CUSTOMER, MY_SHOP));
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
    void createOrder() throws ServiceException {

        Order order = orderService.createOrder(CUSTOMER, OrderType.DELIVERY, MY_SHOP);

        assertEquals(order.getCustomer(), CUSTOMER);
        assertEquals(order.getShop(), MY_SHOP);
        assertEquals(order.getOrderType(), OrderType.DELIVERY);
        assertTrue(order.getOrderedSweets().isEmpty());

    }

    private void testValidAddToOrder(Customer customer, Sweet sweet, Shop myShop) throws ServiceException {

        Order order = orderService.createOrder(customer, OrderType.DELIVERY, myShop);

        double moneyMade = orderService.getMoneyMadeToday();
        orderService.addToOrder(order, sweet);

        assertEquals(moneyMade + sweet.getPrice(), orderService.getMoneyMadeToday());

        orderService.addToOrder(order, sweet);
        orderService.addToOrder(order, sweet);

        assertEquals(moneyMade + sweet.getPrice() * 3, orderService.getMoneyMadeToday());

    }

    private void testInvalidAddToOrder(Customer customer, Sweet sweet, Shop myShop) {
        assertThrowsExactly(ServiceException.class,
                () -> {
                    Order order = orderService.createOrder(customer, OrderType.DELIVERY, myShop);
                    orderService.addToOrder(order, null);
                },
                "Invalid sweet id!");
    }

    @Test
    void addToOrder() throws ServiceException {
        testValidAddToOrder(CUSTOMER, SWEET, MY_SHOP);
        testInvalidAddToOrder(CUSTOMER, SWEET, MY_SHOP);
    }

    @Test
    void getOrderDetails() throws ServiceException {
        String result = orderService.getOrderDetails(1);
        assertEquals(result, orderService.printOrderDetails("1"));
    }


    private void testValidRemoveOrder() throws ServiceException {
        assertEquals(orderService.getAllOrdersInADay().size(), 1);
        orderService.removeOrder(1L);
        assertEquals(orderService.getAllOrdersInADay().size(), 0);
    }

    private void testInvalidRemoveOrder() {
        assertThrowsExactly(ServiceException.class,
                () -> {
                    orderService.removeOrder(1L);
                },
                "This element does not exist!");
        assertEquals(orderService.getAllOrdersInADay().size(), 0);
    }

    @Test
    void removeOrder() throws ServiceException {
        testValidRemoveOrder();
        testInvalidRemoveOrder();
    }

    @Test
    void getAllOrdersInADay() throws ServiceException {
        assertEquals(orderService.getAllOrdersInADay().size(), 1);

        orderService.createOrder(CUSTOMER, OrderType.DELIVERY, MY_SHOP);
        orderService.createOrder(CUSTOMER, OrderType.DELIVERY, MY_SHOP);
        orderService.createOrder(CUSTOMER, OrderType.DELIVERY, MY_SHOP);
        orderService.createOrder(CUSTOMER, OrderType.DELIVERY, MY_SHOP);

        assertEquals(orderService.getAllOrdersInADay().size(), 5);
        for (int i = 0; i < orderService.getAllOrdersInADay().size(); i++)
            assertEquals(orderService.getAllOrdersInADay().get(i).getId(), i + 1);
    }

    @Test
    void getMoneyMadeToday() throws ServiceException {
        assertEquals(orderService.getMoneyMadeToday(), 0);
        orderService.addToOrder(orderService.getAllOrdersInADay().get(0), SWEET);
        assertEquals(orderService.getMoneyMadeToday(), 5);
    }

    @Test
    void getProfitMadeToday() throws ServiceException {
        assertEquals(orderService.getProfitMadeToday(), 0);
        orderService.addToOrder(orderService.getAllOrdersInADay().get(0), SWEET);
        assertEquals(orderService.getProfitMadeToday(), 1.75);
    }

    private void testValidPrintOrderDetails() throws ServiceException {
        String result = orderService.printOrderDetails("1");
        assertEquals(result, orderService.getOrderDetails(1));
    }

    private void testInvalidPrintOrderDetails() {
        assertThrowsExactly(ServiceException.class,
                () -> {
                    String result = orderService.printOrderDetails("1234567");
                },
                "Invalid order number/id!");
        assertThrowsExactly(ServiceException.class,
                () -> {
                    String result = orderService.printOrderDetails("adsasdads");
                },
                "Invalid order number/id!");
    }

    @Test
    void printOrderDetails() throws ServiceException {
        testValidPrintOrderDetails();
        testInvalidPrintOrderDetails();
    }

    @Test
    void getProfit() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException, ServiceException {
        //private method - tested with reflection

        //args: Map<Sweet, Integer> orderedSweets
        Method method = OrderServiceImpl.class.getDeclaredMethod("getProfit",
                Map.class);
        method.setAccessible(true);

        double result = (double) method.invoke(orderService,
                orderService.getAllOrdersInADay().get(0).getOrderedSweets());
        assertEquals(result, 0);

        orderService.addToOrder(orderService.getAllOrdersInADay().get(0), SWEET);
        result = (double) method.invoke(orderService,
                orderService.getAllOrdersInADay().get(0).getOrderedSweets());
        assertEquals(result, 1.75);
    }


    private void addOneSweetToOrder() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        //private method - tested with reflection

        //args: Order order, Sweet sweet
        Method method1 = OrderServiceImpl.class.getDeclaredMethod("addSweetToOrder",
                Order.class, Sweet.class);
        method1.setAccessible(true);

        method1.invoke(orderService, orderService.getAllOrdersInADay().get(0), SWEET);

        assertTrue(orderService.getAllOrdersInADay().get(0).getOrderedSweets().containsKey(SWEET));
        assertEquals(orderService.getAllOrdersInADay().get(0).getOrderedSweets().get(SWEET), 1);
    }

    @Test
    private void addQuantityOfSweetToOrder() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        //private method - tested with reflection

        //args: Order order, Sweet sweet, int quantity
        Method method2 = OrderServiceImpl.class.getDeclaredMethod("addSweetToOrder",
                Order.class, Sweet.class, int.class);
        method2.setAccessible(true);

        method2.invoke(orderService, orderService.getAllOrdersInADay().get(0), SWEET, 5);

        assertTrue(orderService.getAllOrdersInADay().get(0).getOrderedSweets().containsKey(SWEET));
        assertEquals(orderService.getAllOrdersInADay().get(0).getOrderedSweets().get(SWEET), 6);

    }

    @Test
    void addSweetToOrder() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        assertTrue(orderService.getAllOrdersInADay().get(0).getOrderedSweets().isEmpty());
        addOneSweetToOrder();
        addQuantityOfSweetToOrder();
    }

}