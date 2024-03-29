package service.orderService;

import builder.*;
import domain.Customer;
import domain.Shop;
import domain.order.Order;
import domain.order.OrderType;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import domain.sweet.SweetType;
import exception.ValidationException;
import exception.RepositoryException;
import exception.ServiceException;
import org.junit.jupiter.api.*;
import repository.ingredientRepository.IngredientInMemoryRepository;
import repository.ingredientRepository.IngredientRepository;
import repository.orderRepository.OrderInMemoryRepository;
import repository.orderRepository.OrderRepository;
import repository.sweetRepository.SweetInMemoryRepository;
import repository.sweetRepository.SweetRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static service.ConstantValues.*;

class OrderServiceImplTest {
    private Shop shop;
    private Customer customer;
    private Sweet sweet;
    private Ingredient ingredient;
    private OrderService orderService;

    @BeforeAll
    static void setUpAll() {
        System.out.println("Tests for OrderServiceImpl");
    }

    @BeforeEach
    void setUp() throws ValidationException {
        ShopBuilder shopBuilder = new ShopBuilder();
        CustomerBuilder customerBuilder = new CustomerBuilder();
        IngredientBuilder ingredientBuilder = new IngredientBuilder();
        SweetBuilder sweetBuilder = new SweetBuilder();
        OrderBuilder orderBuilder = new OrderBuilder();
        LocationBuilder locationBuilder = new LocationBuilder();

        shop = shopBuilder.build(ID, SHOP_NAME, locationBuilder.build(COUNTRY, CITY, ADDRESS));

        customer = customerBuilder.build(ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, PHONE_NUMBER,
                locationBuilder.build(COUNTRY, CITY, ADDRESS));

        ingredient = ingredientBuilder.build(ID, INGREDIENT_NAME, INGREDIENT_PRICE, AMOUNT);

        OrderRepository orderRepository = new OrderInMemoryRepository(new ArrayList<>());

        try {
            orderRepository.add(orderBuilder.build(ID, new HashMap<>(), OrderType.DELIVERY, customer, shop));
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }

        IngredientRepository ingredientRepository = new IngredientInMemoryRepository();

        Ingredient ingredient1 = ingredientRepository.findIngredientById(1L).isPresent() ?
                ingredientRepository.findIngredientById(1L).get() : fail();
        Ingredient ingredient2 = ingredientRepository.findIngredientById(2L).isPresent() ?
                ingredientRepository.findIngredientById(2L).get() : fail();
        Ingredient ingredient3 = ingredientRepository.findIngredientById(3L).isPresent() ?
                ingredientRepository.findIngredientById(3L).get() : fail();

        sweet = sweetBuilder.build(ID, new ArrayList<>(List.of(ingredient1, ingredient2, ingredient3)), SweetType.DONUT,
                SWEET_PRICE);

        SweetRepository sweetRepository = new SweetInMemoryRepository(ingredientRepository);
        orderService = new OrderServiceImpl(orderRepository, sweetRepository, ingredientRepository);
    }

    @AfterEach
    void tearDown() {

    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Tests passed");
    }


    @Test
    void testCreateOrder() throws ServiceException, ValidationException {
        Optional<Order> order = orderService.createOrder(customer, OrderType.DELIVERY, shop);
        if (order.isPresent()) {
            assertEquals(order.get().getCustomer(), customer);
            assertEquals(order.get().getShop(), shop);
            assertEquals(order.get().getOrderType(), OrderType.DELIVERY);
            assertTrue(order.get().getOrderedSweets().isEmpty());
        } else fail("Order createOrder failed");
    }

    @Test
    void testValidAddToOrder() throws ServiceException, ValidationException {
        Optional<Order> order = orderService.createOrder(customer, OrderType.DELIVERY, shop);
        if (order.isPresent()) {
            double moneyMade = orderService.getMoneyMadeToday();
            orderService.addToOrder(order.get(), sweet);

            assertEquals(moneyMade + sweet.getTotalPrice(), orderService.getMoneyMadeToday());

            orderService.addToOrder(order.get(), sweet);
            orderService.addToOrder(order.get(), sweet);

            assertEquals(moneyMade + sweet.getTotalPrice() * 3, orderService.getMoneyMadeToday());
        } else fail("Order addToOrder failed");
    }

    @Test
    void testInvalidAddToOrder() throws ServiceException, ValidationException {
        Optional<Order> order = orderService.createOrder(customer, OrderType.DELIVERY, shop);
        order.ifPresent(value -> assertThrowsExactly(ServiceException.class,
                () -> orderService.addToOrder(value, null),
                SWEET_ID_EXCEPTION));
    }


    @Test
    void testValidGetOrderDetails() throws ServiceException {
        String result = String.valueOf(orderService.getOrderDetails("1"));
        assertTrue(result.contains(FIRST_NAME));
        assertTrue(result.contains(LAST_NAME));
        assertTrue(result.contains(EMAIL));
        assertTrue(result.contains(PHONE_NUMBER));
    }

    @Test
    void testInvalidGetOrderDetails() {
        assertThrowsExactly(ServiceException.class,
                () -> orderService.getOrderDetails("1234567"),
                ORDER_ID_EXCEPTION);
        assertThrowsExactly(ServiceException.class,
                () -> orderService.getOrderDetails("adsasdads"),
                ORDER_ID_EXCEPTION);
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
                ELEMENT_DOES_NOT_EXIST_EXCEPTION);
        assertEquals(orderService.getAllOrdersInADay().size(), 0);
    }


    @Test
    void testGetAllOrdersInADay() throws ServiceException, ValidationException {
        assertEquals(orderService.getAllOrdersInADay().size(), 1);

        orderService.createOrder(customer, OrderType.DELIVERY, shop);
        orderService.createOrder(customer, OrderType.DELIVERY, shop);
        orderService.createOrder(customer, OrderType.DELIVERY, shop);
        orderService.createOrder(customer, OrderType.DELIVERY, shop);

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
        assertEquals(orderService.getProfitMadeToday(), 2.58);
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
        assertEquals(result, 2.58);
    }


    @Test
    void addOneSweetToOrder() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        //private method - tested with reflection

        //args: Order order, Sweet sweet
        Method method = OrderServiceImpl.class.getDeclaredMethod("addSweetToOrder",
                Order.class, Sweet.class);
        method.setAccessible(true);

        assertTrue(orderService.getAllOrdersInADay().get(0).getOrderedSweets().isEmpty());

        method.invoke(orderService, orderService.getAllOrdersInADay().get(0), sweet);

        assertTrue(orderService.getAllOrdersInADay().get(0).getOrderedSweets().containsKey(sweet));
        assertEquals(orderService.getAllOrdersInADay().get(0).getOrderedSweets().get(sweet), 1);
    }

    @Test
    void testAddQuantityOfSweetToOrder() throws NoSuchMethodException, SecurityException, InvocationTargetException,
            IllegalAccessException {
        //private method - tested with reflection

        //args: Order order, Sweet sweet, int quantity
        Method method = OrderServiceImpl.class.getDeclaredMethod("addSweetToOrder",
                Order.class, Sweet.class, int.class);
        method.setAccessible(true);

        method.invoke(orderService, orderService.getAllOrdersInADay().get(0), sweet, 5);

        assertTrue(orderService.getAllOrdersInADay().get(0).getOrderedSweets().containsKey(sweet));
        assertEquals(orderService.getAllOrdersInADay().get(0).getOrderedSweets().get(sweet), 5);

    }

    @Test
    void testAddExtraIngredientToOrderedSweet() throws ServiceException, ValidationException {
        addSweetForTest();
        orderService.addExtraIngredientToOrderedSweet(orderService.getAllOrdersInADay().get(0), sweet, ingredient, "2");
        orderService.getAllOrdersInADay().get(0)
                .getOrderedSweets()
                .forEach((key, value) -> {
                    assertEquals(key.getSweetType(), sweet.getSweetType());
                    assertEquals(key.getExtraPrice(), INGREDIENT_PRICE * 2);
                    assertEquals(key.getExtraIngredients().size(), 2);
                    assertEquals(key.getExtraIngredients().get(0), ingredient);
                    assertEquals(key.getExtraIngredients().get(1), ingredient);
                    assertEquals(value, 1);
                });
    }

    @Test
    void testInvalidAddExtraIngredientToOrderedSweet() {
        assertThrowsExactly(ServiceException.class,
                () -> orderService.addExtraIngredientToOrderedSweet(orderService.getAllOrdersInADay().get(0),
                        null, ingredient, "2"),
                INVALID_SWEET_EXCEPTION);

        assertThrowsExactly(ServiceException.class,
                () -> orderService.addExtraIngredientToOrderedSweet(orderService.getAllOrdersInADay().get(0),
                        sweet, ingredient, "2"),
                SWEET_NOT_ORDERED_EXCEPTION);

        assertThrowsExactly(ServiceException.class,
                () -> orderService.addExtraIngredientToOrderedSweet(orderService.getAllOrdersInADay().get(0),
                        sweet, null, "2"),
                INVALID_INGREDIENT_EXCEPTION);

        assertThrowsExactly(ServiceException.class,
                () -> orderService.addExtraIngredientToOrderedSweet(orderService.getAllOrdersInADay().get(0),
                        sweet, ingredient, "1234567"),
                INVALID_AMOUNT_EXCEPTION);

        assertThrowsExactly(ServiceException.class,
                () -> orderService.addExtraIngredientToOrderedSweet(orderService.getAllOrdersInADay().get(0),
                        sweet, ingredient, "-1234567"),
                INVALID_AMOUNT_EXCEPTION);

        assertThrowsExactly(ServiceException.class,
                () -> orderService.addExtraIngredientToOrderedSweet(orderService.getAllOrdersInADay().get(0),
                        sweet, ingredient, "dsdsadada"),
                INVALID_AMOUNT_EXCEPTION);
    }

    @Test
    void testUpdateExtraIngredientForOrderedSweet() throws ServiceException {
        addSweetForTest();

        orderService.updateExtraIngredientForOrderedSweet(orderService.getAllOrdersInADay().get(0), sweet, ingredient, "2");
        orderService.getAllOrdersInADay().get(0)
                .getOrderedSweets()
                .forEach((key, value) -> {
                    assertEquals(key.getSweetType(), sweet.getSweetType());
                    assertEquals(key.getExtraPrice(), INGREDIENT_PRICE * 2);
                    assertEquals(key.getExtraIngredients().size(), 2);
                    assertEquals(key.getExtraIngredients().get(0), ingredient);
                    assertEquals(key.getExtraIngredients().get(1), ingredient);
                    assertEquals(value, 1);
                });

        orderService.updateExtraIngredientForOrderedSweet(orderService.getAllOrdersInADay().get(0), sweet, ingredient, "1");

        orderService.getAllOrdersInADay().get(0)
                .getOrderedSweets()
                .forEach((key, value) -> {
                    assertEquals(key.getSweetType(), sweet.getSweetType());
                    assertEquals(key.getExtraPrice(), INGREDIENT_PRICE);
                    assertEquals(key.getExtraIngredients().size(), 1);
                    assertEquals(key.getExtraIngredients().get(0), ingredient);
                    assertEquals(value, 1);
                });
    }

    @Test
    void testDeleteExtraIngredientForOrderedSweet() throws ServiceException, ValidationException {
        addSweetForTest();
        orderService.addExtraIngredientToOrderedSweet(orderService.getAllOrdersInADay().get(0), sweet, ingredient, "2");

        orderService.getAllOrdersInADay().get(0)
                .getOrderedSweets()
                .forEach((key, value) -> {
                    try {
                        orderService.deleteExtraIngredientForOrderedSweet(orderService.getAllOrdersInADay().get(0), key,
                                key.getExtraIngredients().get(0));
                    } catch (ServiceException e) {
                        throw new RuntimeException(e);
                    }
                });

        orderService.getAllOrdersInADay().get(0)
                .getOrderedSweets()
                .forEach((key, value) -> {
                    assertEquals(key.getSweetType(), sweet.getSweetType());
                    assertEquals(key.getExtraPrice(), 0);
                    assertEquals(key.getExtraIngredients().size(), 0);
                    assertEquals(value, 1);
                });
    }

    private void addSweetForTest() throws ServiceException {
        orderService.addToOrder(orderService.getAllOrdersInADay().get(0), sweet);
        assertEquals(sweet.getExtraIngredients().size(), 0);

        orderService.getAllOrdersInADay().get(0)
                .getOrderedSweets()
                .forEach((key, value) -> {
                    assertEquals(key.getSweetType(), sweet.getSweetType());
                    assertEquals(key.getExtraPrice(), 0);
                    assertEquals(key.getExtraIngredients().size(), 0);
                    assertEquals(value, 1);
                });
    }

}