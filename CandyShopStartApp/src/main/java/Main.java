import domain.Shop;
import domain.location.Location;
import repository.customersRepository.CustomerInMemoryRepository;
import repository.customersRepository.CustomerRepository;
import repository.ordersRepository.OrderInMemoryRepository;
import repository.ordersRepository.OrderRepository;
import repository.sweetsRepository.SweetInMemoryRepository;
import repository.sweetsRepository.SweetRepository;
import service.customerService.CustomerService;
import service.customerService.CustomerServiceImpl;
import service.orderService.OrderService;
import service.orderService.OrderServiceImpl;
import service.sweetService.SweetService;
import service.sweetService.SweetServiceImpl;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        startApp();
    }

    public static void startApp() {
        System.out.println("\nWELCOME TO THE CANDY MY FRIEND :)\n");

        Shop myShop = new Shop("Candy Crush Shop", new Location(1, "Romania", "Cluj-Napoca", "Str. Memorandumului, nr. 10"));

        //Repository
        SweetRepository sweetRepository =
                new SweetInMemoryRepository(new ArrayList<>());
        sweetRepository.generateSweets();
        CustomerRepository customerRepository =
                new CustomerInMemoryRepository(new ArrayList<>());
        customerRepository.generateCustomers();
        OrderRepository orderRepository =
                new OrderInMemoryRepository(new ArrayList<>());
        orderRepository.generateOrders(myShop, sweetRepository, customerRepository);
        // +++ UseCase2-RepoIngredients +++

        //Service
        CustomerService customerService = new CustomerServiceImpl(customerRepository);
        OrderService orderService = new OrderServiceImpl(orderRepository);
        SweetService sweetService = new SweetServiceImpl(sweetRepository);

        //UI
        UI appUI = new UI(myShop, customerService, sweetService, orderService);
        appUI.show();


        System.out.println("\nSEE YOU LATER, ALLIGATOR! :)\n");
    }

}
