package service.orderService;

import domain.Customer;
import domain.Shop;
import domain.location.Location;
import domain.order.Order;
import domain.order.OrderType;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import domain.sweet.SweetType;
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

    private static OrderService orderService;

    @org.junit.jupiter.api.BeforeAll
    static void setUpAll() {
        System.out.println("Tests for OrderServiceImpl");
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        Shop myShop = new Shop("Candy Crush Shop",
                new Location(1, "Romania", "Cluj-Napoca", "Str. Memorandumului, nr. 10"));
        Customer customer = new Customer(1, "Razvan", "Berendi",
                "berendi.rav2001@gmail.com", "1234567890", "0751578787",
                new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));

        OrderRepository orderRepository = new OrderInMemoryRepository(new ArrayList<>());
        try {
            orderRepository.add(new Order(1, new HashMap<>(), OrderType.DELIVERY, customer, myShop));
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        orderService = new OrderServiceImpl(orderRepository);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {

    }

    @org.junit.jupiter.api.AfterAll
    static void tearDownAll() {
        System.out.println("Tests passed");
    }


    @org.junit.jupiter.api.Test
    void createOrder() {
        Shop myShop = new Shop("Candy Crush Shop",
                new Location(1, "Romania", "Cluj-Napoca", "Str. Memorandumului, nr. 10"));
        Customer customer = new Customer(1, "Razvan", "Berendi",
                "berendi.rav2001@gmail.com", "1234567890", "0751578787",
                new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));

        try {
            Order order = orderService.createOrder(customer, OrderType.DELIVERY, myShop);

            assertEquals(order.getCustomer(), customer);
            assertEquals(order.getShop(), myShop);
            assertEquals(order.getOrderType(), OrderType.DELIVERY);
            assertTrue(order.getOrderedSweets().isEmpty());

        } catch (ServiceException e) {
            fail();
        }
    }

    @org.junit.jupiter.api.Test
    void addToOrder() {

        Shop myShop = new Shop("Candy Crush Shop",
                new Location(1, "Romania", "Cluj-Napoca", "Str. Memorandumului, nr. 10"));
        Customer customer = new Customer(1, "Razvan", "Berendi",
                "berendi.rav2001@gmail.com", "1234567890", "0751578787",
                new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));
        Sweet sweet = new Sweet(1,
                new ArrayList<>(List.of(
                        new Ingredient(1, "Sugar", 1.5),
                        new Ingredient(2, "Milk", 1),
                        new Ingredient(3, "Flour", 0.75))),
                SweetType.DONUT, 5);

        try {
            Order order = orderService.createOrder(customer, OrderType.DELIVERY, myShop);

            double moneyMade = orderService.getMoneyMadeToday();
            orderService.addToOrder(order, sweet);

            assertEquals(moneyMade + sweet.getPrice(), orderService.getMoneyMadeToday());

            orderService.addToOrder(order, sweet);
            orderService.addToOrder(order, sweet);

            assertEquals(moneyMade + sweet.getPrice() * 3, orderService.getMoneyMadeToday());

        } catch (ServiceException e) {
            fail();
        }

        try {
            Order order = orderService.createOrder(customer, OrderType.DELIVERY, myShop);

            orderService.addToOrder(order, null);

            fail();
        } catch (ServiceException e) {
            assertEquals(e.getMessage(), "Invalid sweet id!");
        }

    }

    @org.junit.jupiter.api.Test
    void getOrderDetails() {
        String result = orderService.getOrderDetails(1);
        try {
            assertEquals(result, orderService.printOrderDetails("1"));
        } catch (ServiceException e) {
            fail();
        }
    }

    @org.junit.jupiter.api.Test
    void removeOrder() {
        assertEquals(orderService.getAllOrdersInADay().size(), 1);

        try {
            orderService.removeOrder(1L);
        } catch (ServiceException e) {
            fail();
        }
        assertEquals(orderService.getAllOrdersInADay().size(), 0);
        try {
            orderService.removeOrder(1L);
            fail();
        } catch (ServiceException e) {
            assertEquals(orderService.getAllOrdersInADay().size(), 0);
            assertEquals(e.getMessage(), "This element does not exist!");
        }
    }

    @org.junit.jupiter.api.Test
    void getAllOrdersInADay() {
        Shop myShop = new Shop("Candy Crush Shop",
                new Location(1, "Romania", "Cluj-Napoca", "Str. Memorandumului, nr. 10"));
        Customer customer = new Customer(1, "Razvan", "Berendi",
                "berendi.rav2001@gmail.com", "1234567890", "0751578787",
                new Location(1, "Romania", "Cluj", "Strada Peana nr. 10, bloc F7, ap. 5"));


        assertEquals(orderService.getAllOrdersInADay().size(), 1);

        try {
            orderService.createOrder(customer, OrderType.DELIVERY, myShop);
            orderService.createOrder(customer, OrderType.DELIVERY, myShop);
            orderService.createOrder(customer, OrderType.DELIVERY, myShop);
            orderService.createOrder(customer, OrderType.DELIVERY, myShop);

            assertEquals(orderService.getAllOrdersInADay().size(), 5);
            for (int i = 0; i < orderService.getAllOrdersInADay().size(); i++)
                assertEquals(orderService.getAllOrdersInADay().get(i).getIdOrder(), i + 1);


        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

    }

    @org.junit.jupiter.api.Test
    void getMoneyMadeToday() {
        assertEquals(orderService.getMoneyMadeToday(), 0);
        Sweet sweet = new Sweet(1,
                new ArrayList<>(List.of(
                        new Ingredient(1, "Sugar", 1.5),
                        new Ingredient(2, "Milk", 1),
                        new Ingredient(3, "Flour", 0.75))),
                SweetType.DONUT, 5);
        try {
            orderService.addToOrder(orderService.getAllOrdersInADay().get(0), sweet);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        assertEquals(orderService.getMoneyMadeToday(), 5);
    }

    @org.junit.jupiter.api.Test
    void getProfitMadeToday() {
        assertEquals(orderService.getProfitMadeToday(), 0);
        Sweet sweet = new Sweet(1,
                new ArrayList<>(List.of(
                        new Ingredient(1, "Sugar", 1.5),
                        new Ingredient(2, "Milk", 1),
                        new Ingredient(3, "Flour", 0.75))),
                SweetType.DONUT, 5);
        try {
            orderService.addToOrder(orderService.getAllOrdersInADay().get(0), sweet);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        assertEquals(orderService.getProfitMadeToday(), 1.75);
    }

    @org.junit.jupiter.api.Test
    void printOrderDetails() {
        try {
            String result = orderService.printOrderDetails("1");
            assertEquals(result, orderService.getOrderDetails(1));
        } catch (ServiceException e) {
            fail();
        }

        try {
            String result = orderService.printOrderDetails("1234567");
            fail();
        } catch (ServiceException e) {
            assertEquals(e.getMessage(), "Invalid order number/id!");
        }

        try {
            String result = orderService.printOrderDetails("adsasdads");
            fail();
        } catch (ServiceException e) {
            assertEquals(e.getMessage(), "Invalid order number/id!");
        }
    }

    @org.junit.jupiter.api.Test
    void getProfit() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        //private method - tested with reflection

        //args: Map<Sweet, Integer> orderedSweets
        Method method = OrderServiceImpl.class.getDeclaredMethod("getProfit",
                Map.class);
        method.setAccessible(true);

        double result = (double) method.invoke(orderService,
                orderService.getAllOrdersInADay().get(0).getOrderedSweets());

        assertEquals(result, 0);

        Sweet sweet = new Sweet(1,
                new ArrayList<>(List.of(
                        new Ingredient(1, "Sugar", 1.5),
                        new Ingredient(2, "Milk", 1),
                        new Ingredient(3, "Flour", 0.75))),
                SweetType.DONUT, 5);
        try {
            orderService.addToOrder(orderService.getAllOrdersInADay().get(0), sweet);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        result = (double) method.invoke(orderService,
                orderService.getAllOrdersInADay().get(0).getOrderedSweets());

        assertEquals(result, 1, 75);
    }

    @org.junit.jupiter.api.Test
    void addSweetToOrder() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        //private method - tested with reflection

        //args: Order order, Sweet sweet
        Method method1 = OrderServiceImpl.class.getDeclaredMethod("addSweetToOrder",
                Order.class, Sweet.class);
        method1.setAccessible(true);

        //args: Order order, Sweet sweet, int quantity
        Method method2 = OrderServiceImpl.class.getDeclaredMethod("addSweetToOrder",
                Order.class, Sweet.class, int.class);
        method2.setAccessible(true);


        Sweet sweet = new Sweet(1,
                new ArrayList<>(List.of(
                        new Ingredient(1, "Sugar", 1.5),
                        new Ingredient(2, "Milk", 1),
                        new Ingredient(3, "Flour", 0.75))),
                SweetType.DONUT, 5);

        assertTrue(orderService.getAllOrdersInADay().get(0).getOrderedSweets().isEmpty());

        method1.invoke(orderService, orderService.getAllOrdersInADay().get(0), sweet);

        assertTrue(orderService.getAllOrdersInADay().get(0).getOrderedSweets().containsKey(sweet));
        assertEquals(orderService.getAllOrdersInADay().get(0).getOrderedSweets().get(sweet), 1);


        method2.invoke(orderService, orderService.getAllOrdersInADay().get(0), sweet, 5);

        assertTrue(orderService.getAllOrdersInADay().get(0).getOrderedSweets().containsKey(sweet));
        assertEquals(orderService.getAllOrdersInADay().get(0).getOrderedSweets().get(sweet), 6);

    }

}