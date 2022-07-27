
import domain.Shop;
import domain.location.Location;
import repository.customersRepository.CustomerInMemoryRepository;
import repository.customersRepository.CustomerRepository;
import repository.ordersRepository.OrderInMemoryRepository;
import repository.ordersRepository.OrderRepository;
import repository.sweetsRepository.SweetInMemoryRepository;
import repository.sweetsRepository.SweetRepository;
import service.Service;
import service.ServiceImpl;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        startApp();
    }

    public static void startApp() {
        System.out.println("\nWELCOME TO THE CANDY MY FRIEND :)\n");

        Shop myShop = new Shop("Candy Crush Shop",
                new Location(1, "Romania", "Cluj-Napoca", "Str. Memorandumului, nr. 10"));

        //Repository
        SweetRepository sweetRepository = new SweetInMemoryRepository(SweetInMemoryRepository.generateSweets());
        CustomerRepository customerRepository = new CustomerInMemoryRepository(CustomerInMemoryRepository.generateCustomers());
        OrderRepository orderRepository = new OrderInMemoryRepository(OrderInMemoryRepository
                .generateOrders(myShop, sweetRepository, customerRepository));
        // +++ UseCase2-RepoIngredients +++

        //Service
        Service service = new ServiceImpl(myShop, sweetRepository, customerRepository, orderRepository);

        //UI
        UI appUI = new UI(service);
        appUI.show();


        System.out.println("\nSEE YOU LATER, ALLIGATOR! :)\n");
    }

}
