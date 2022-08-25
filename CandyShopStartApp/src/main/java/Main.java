import builder.*;
import domain.Shop;
import exception.ValidationException;
import repository.customerRepository.CustomerRepository;
import repository.ingredientRepository.IngredientRepository;
import repository.orderRepository.OrderRepository;
import repository.sweetRepository.SweetRepository;
import service.customerService.CustomerService;
import service.ingredientService.IngredientService;
import service.orderService.OrderService;
import service.sweetService.SweetService;
import ui.UI;

import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {
        try {
            startApp();
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void startApp() throws ValidationException {
        System.out.println("\nWELCOME TO THE CANDY SHOP MY FRIEND :)\n");

        ShopBuilder shopBuilder = new ShopBuilder();
        LocationBuilder locationBuilder = new LocationBuilder();

        Shop shop = shopBuilder.build("Candy Crush Shop", locationBuilder.build("Romania", "Cluj-Napoca",
                "Str. Memorandumului, nr. 10"));

        //Repository
        IngredientInMemoryRepositoryBuilder ingredientInMemoryRepositoryBuilder = new IngredientInMemoryRepositoryBuilder();
        SweetInMemoryRepositoryBuilder sweetInMemoryRepositoryBuilder = new SweetInMemoryRepositoryBuilder();
        CustomerInMemoryRepositoryBuilder customerInMemoryRepositoryBuilder = new CustomerInMemoryRepositoryBuilder();
        OrderInMemoryRepositoryBuilder orderInMemoryRepositoryBuilder = new OrderInMemoryRepositoryBuilder();

        IngredientRepository ingredientRepository = ingredientInMemoryRepositoryBuilder.build(new ArrayList<>());
        ingredientRepository.generateIngredients();

        SweetRepository sweetRepository = sweetInMemoryRepositoryBuilder.build(new ArrayList<>());
        sweetRepository.generateSweets(ingredientRepository);

        CustomerRepository customerRepository = customerInMemoryRepositoryBuilder.build(new ArrayList<>());
        customerRepository.generateCustomers();

        OrderRepository orderRepository = orderInMemoryRepositoryBuilder.build(new ArrayList<>());
        orderRepository.generateOrders(shop, sweetRepository, customerRepository);

        //Service
        CustomerServiceImplBuilder customerServiceImplBuilder = new CustomerServiceImplBuilder();
        OrderServiceImplBuilder orderServiceImplBuilder = new OrderServiceImplBuilder();
        SweetServiceImplBuilder sweetServiceImplBuilder = new SweetServiceImplBuilder();
        IngredientServiceImplBuilder ingredientServiceImplBuilder = new IngredientServiceImplBuilder();

        CustomerService customerService = customerServiceImplBuilder.build(customerRepository);
        OrderService orderService = orderServiceImplBuilder.build(orderRepository, sweetRepository, ingredientRepository);
        SweetService sweetService = sweetServiceImplBuilder.build(sweetRepository, ingredientRepository);
        IngredientService ingredientService = ingredientServiceImplBuilder.build(ingredientRepository);

        //ui.UI
        UIBuilder uiBuilder = new UIBuilder();

        UI appUI = uiBuilder.build(shop, customerService, sweetService, orderService, ingredientService);
        appUI.show();


        System.out.println("\nSEE YOU LATER, ALLIGATOR! :)\n");
    }

}
