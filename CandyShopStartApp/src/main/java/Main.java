import builder.*;
import domain.Shop;
import exception.ValidationException;
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
import userInterface.UI;

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
        IngredientRepository ingredientRepository = new IngredientInMemoryRepository();
        SweetRepository sweetRepository = new SweetInMemoryRepository(ingredientRepository);
        CustomerRepository customerRepository = new CustomerInMemoryRepository();
        OrderRepository orderRepository = new OrderInMemoryRepository(shop, sweetRepository, customerRepository);

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
