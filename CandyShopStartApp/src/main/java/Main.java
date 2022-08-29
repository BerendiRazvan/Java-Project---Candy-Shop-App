import builder.*;
import domain.Shop;
import exception.CandyShopException;
import exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws CandyShopException {
        try {
            LOGGER.info("Application - started");
            startApp();
            LOGGER.info("Application - finished");
        } catch (ValidationException e) {
            LOGGER.error("Application - exception occurred -> {}", e.getMessage());
            throw new CandyShopException(e.getMessage());
        }
    }

    public static void startApp() throws ValidationException {
        System.out.println("\nWELCOME TO THE CANDY SHOP MY FRIEND :)\n");

        ShopBuilder shopBuilder = new ShopBuilder();
        LocationBuilder locationBuilder = new LocationBuilder();

        Shop shop = shopBuilder.build("Candy Crush Shop", locationBuilder.build("Romania", "Cluj-Napoca",
                "Str. Memorandumului, nr. 10"));
        LOGGER.info("Shop initialized - started");

        //Repository
        IngredientRepository ingredientRepository = new IngredientInMemoryRepository();
        SweetRepository sweetRepository = new SweetInMemoryRepository(ingredientRepository);
        CustomerRepository customerRepository = new CustomerInMemoryRepository();
        OrderRepository orderRepository = new OrderInMemoryRepository(shop, sweetRepository, customerRepository);
        LOGGER.info("Repositories initialized");

        //Service
        CustomerService customerService = new CustomerServiceImpl(customerRepository);
        OrderService orderService = new OrderServiceImpl(orderRepository, sweetRepository, ingredientRepository);
        SweetService sweetService = new SweetServiceImpl(sweetRepository, ingredientRepository);
        IngredientService ingredientService = new IngredientServiceImpl(ingredientRepository);
        LOGGER.info("Services initialized");

        //UI
        UI appUI = new UI(shop, customerService, sweetService, orderService, ingredientService);
        appUI.show();
        LOGGER.info("UI initialized");

        System.out.println("\nSEE YOU LATER, ALLIGATOR! :)\n");
    }

}
