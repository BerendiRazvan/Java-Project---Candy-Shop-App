package service.orderService;

import repository.ordersRepository.OrderInMemoryRepository;
import repository.ordersRepository.OrderRepository;

import java.util.ArrayList;

class OrderServiceImplTest {

    private static OrderService orderService;

    @org.junit.jupiter.api.BeforeAll
    static void setUpAll() {
        System.out.println("Tests for OrderServiceImpl");
        OrderRepository orderRepository = new OrderInMemoryRepository(new ArrayList<>());
        orderService = new OrderServiceImpl(orderRepository);
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
    void createOrder() {
    }

    @org.junit.jupiter.api.Test
    void addToOrder() {
    }

    @org.junit.jupiter.api.Test
    void getOrderDetails() {
    }

    @org.junit.jupiter.api.Test
    void removeOrder() {
    }

    @org.junit.jupiter.api.Test
    void getAllOrdersInADay() {
    }

    @org.junit.jupiter.api.Test
    void getMoneyMadeToday() {
    }

    @org.junit.jupiter.api.Test
    void getProfitMadeToday() {
    }

    @org.junit.jupiter.api.Test
    void printOrderDetails() {
    }

    @org.junit.jupiter.api.Test
    void getProfit() {
    }

    @org.junit.jupiter.api.Test
    void addSweetToOrder() {
    }

    @org.junit.jupiter.api.Test
    void removeSweetToOrder() {
    }
}