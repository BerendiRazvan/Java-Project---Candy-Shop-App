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
    /*
    Add to your user console the functionalities:
•	Order custom sweet: you can choose additional ingredients for an ordered sweet with custom amount
•	Make your own sweet: use a recipe with the format ingredient1,amount1; ingredient2,amount2
•	Remove ingredient for an ordered custom sweet
•	Update amount of an ingredient from the ordered custom sweet
•	Print the status of all ingredients and notify the user if the shop runs low on some ingredients

+Update: --ingredients cand cumpara cineva un dulce
     */


    public static void main(String[] args) {
        startApp();
    }

    public static void startApp() {
        System.out.println("\nWELCOME TO THE CANDY MY FRIEND :)\n");

        Shop shop = new Shop("Candy Crush Shop",
                new Location(1, "Romania", "Cluj-Napoca", "Str. Memorandumului, nr. 10"));

        //Repository
        IngredientRepository ingredientRepository = new IngredientInMemoryRepository(new ArrayList<>());
        ingredientRepository.generateIngredients();

        SweetRepository sweetRepository = new SweetInMemoryRepository(new ArrayList<>());
        sweetRepository.generateSweets(ingredientRepository);

        CustomerRepository customerRepository = new CustomerInMemoryRepository(new ArrayList<>());
        customerRepository.generateCustomers();

        OrderRepository orderRepository = new OrderInMemoryRepository(new ArrayList<>());
        orderRepository.generateOrders(shop, sweetRepository, customerRepository);

        //Service
        CustomerService customerService = new CustomerServiceImpl(customerRepository);
        OrderService orderService = new OrderServiceImpl(orderRepository, sweetRepository, ingredientRepository);
        SweetService sweetService = new SweetServiceImpl(sweetRepository, ingredientRepository);
        IngredientService ingredientService = new IngredientServiceImpl(ingredientRepository);

        //UI
        UI appUI = new UI(shop, customerService, sweetService, orderService, ingredientService);
        appUI.show();


        System.out.println("\nSEE YOU LATER, ALLIGATOR! :)\n");
    }

}
