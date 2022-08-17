import domain.Shop;
import domain.location.Location;
import repository.customerRepository.CustomerInMemoryRepository;
import repository.customerRepository.CustomerRepository;
import repository.ingredientRepository.IngredientInMemoryRepository;
import repository.ingredientRepository.IngredientRepository;
import repository.orderRepository.OrderInMemoryRepository;
import repository.orderRepository.OrderRepository;
import repository.sweetRepository.SweetInMemoryRepository;
import repository.sweetRepository.SweetRepository;
import service.customerService.CustomerService;
import service.customerService.CustomerServiceImpl;
import service.ingredientService.IngredientService;
import service.ingredientService.IngredientServiceImpl;
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
        System.out.println("\nWELCOME TO THE CANDY SHOP MY FRIEND :)\n");

        Shop shop = Shop.builder()
                .name("Candy Crush Shop")
                .location(Location.builder()
                        .country("Romania")
                        .city("Cluj-Napoca")
                        .address("Str. Memorandumului, nr. 10")
                        .build())
                .build();

        //Repository
        IngredientRepository ingredientRepository = IngredientInMemoryRepository.builder()
                .ingredientList(new ArrayList<>())
                .build();
        ingredientRepository.generateIngredients();

        SweetRepository sweetRepository = SweetInMemoryRepository.builder()
                .sweetList(new ArrayList<>())
                .build();
        sweetRepository.generateSweets(ingredientRepository);

        CustomerRepository customerRepository = CustomerInMemoryRepository.builder()
                .customerList(new ArrayList<>())
                .build();
        customerRepository.generateCustomers();

        OrderRepository orderRepository = OrderInMemoryRepository.builder()
                .orderList(new ArrayList<>())
                .build();
        orderRepository.generateOrders(shop, sweetRepository, customerRepository);

        //Service
        CustomerService customerService = CustomerServiceImpl.builder()
                .customerRepository(customerRepository)
                .build();
        OrderService orderService = OrderServiceImpl.builder()
                .orderRepository(orderRepository)
                .sweetRepository(sweetRepository)
                .ingredientRepository(ingredientRepository)
                .build();
        SweetService sweetService = SweetServiceImpl.builder()
                .sweetRepository(sweetRepository)
                .ingredientRepository(ingredientRepository)
                .build();
        IngredientService ingredientService = IngredientServiceImpl.builder()
                .ingredientRepository(ingredientRepository)
                .build();

        //UI
        UI appUI = UI.builder()
                .shop(shop)
                .customerService(customerService)
                .sweetService(sweetService)
                .orderService(orderService)
                .ingredientService(ingredientService)
                .build();
        appUI.show();


        System.out.println("\nSEE YOU LATER, ALLIGATOR! :)\n");
    }

}
