import builder.*;
import domain.Shop;
import exception.CandyShopException;
import exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.customerRepository.CustomerDataBaseRepository;
import repository.customerRepository.CustomerInMemoryRepository;
import repository.customerRepository.CustomerRepository;
import repository.ingredientRepository.IngredientDataBaseRepository;
import repository.ingredientRepository.IngredientInMemoryRepository;
import repository.ingredientRepository.IngredientRepository;
import repository.orderRepository.OrderDataBaseRepository;
import repository.orderRepository.OrderInMemoryRepository;
import repository.orderRepository.OrderRepository;
import repository.sweetRepository.SweetDataBaseRepository;
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

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws CandyShopException {
        System.out.println("\nWELCOME TO THE CANDY SHOP MY FRIEND :)\n");

        try {
            LOGGER.info("Application - started");
            startApp();
            LOGGER.info("Application - finished");
        } catch (ValidationException e) {
            LOGGER.error("Application - exception occurred -> {}", e.getMessage());
            throw new CandyShopException(e.getMessage());
        }

        System.out.println("\nSEE YOU LATER, ALLIGATOR! :)\n");
    }

    public static void startApp() throws CandyShopException {
        EntityManagerFactory entityManagerFactory;
        entityManagerFactory = Persistence.createEntityManagerFactory("JavaSummerPractice2022");

        LOGGER.info("EntityManagerFactory initialized");

        ShopBuilder shopBuilder = new ShopBuilder();
        LocationBuilder locationBuilder = new LocationBuilder();

        Shop shop = shopBuilder.build(1L, "Candy Crush Shop", locationBuilder.build("Romania", "Cluj-Napoca",
                "Str. Memorandumului, nr. 10"));
        LOGGER.info("Shop initialized");

        //UI
        UI appUI = new UI(shop);

        //Repository
        IngredientRepository ingredientRepository;
        SweetRepository sweetRepository;
        CustomerRepository customerRepository;
        OrderRepository orderRepository;
        LOGGER.info("Repositories created");

        while (true) {
            try {
                if (appUI.persistenceOptionForDataBase()) {
                    //Data Base Repository
                    ingredientRepository = new IngredientDataBaseRepository(entityManagerFactory);
                    sweetRepository = new SweetDataBaseRepository(entityManagerFactory);
                    customerRepository = new CustomerDataBaseRepository(entityManagerFactory);
                    orderRepository = new OrderDataBaseRepository(entityManagerFactory);
                    LOGGER.info("Data Base Repositories initialized");
                    break;
                }
                //In Memory Repository - with data generation
                ingredientRepository = new IngredientInMemoryRepository();
                sweetRepository = new SweetInMemoryRepository(ingredientRepository);
                customerRepository = new CustomerInMemoryRepository();
                orderRepository = new OrderInMemoryRepository(shop, sweetRepository, customerRepository);
                LOGGER.info("In Memory Repositories initialized");
                break;
            } catch (Exception ex) {
                LOGGER.warn("Invalid option for repositories persistence initialization");
            }
        }

        //Service
        CustomerService customerService = new CustomerServiceImpl(customerRepository);
        OrderService orderService = new OrderServiceImpl(orderRepository, sweetRepository, ingredientRepository);
        SweetService sweetService = new SweetServiceImpl(sweetRepository, ingredientRepository);
        IngredientService ingredientService = new IngredientServiceImpl(ingredientRepository);
        LOGGER.info("Services initialized");

        //UI
        appUI = new UI(shop, customerService, sweetService, orderService, ingredientService);
        appUI.show();
        LOGGER.info("UI initialized");

        entityManagerFactory.close();
    }
}
