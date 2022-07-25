import domain.Customer;
import domain.Shop;
import domain.location.Location;
import domain.order.Delivery;
import domain.order.Order;
import domain.order.PickUp;
import domain.sweet.Ingredient;
import domain.sweet.Recipe;
import domain.sweet.Sweet;
import domain.sweet.SweetTypes;
import repository.customersRepository.CustomersInMemoryRepository;
import repository.customersRepository.CustomersRepository;
import repository.ordersRepository.OrdersInMemoryRepository;
import repository.ordersRepository.OrdersRepository;
import repository.sweetsRepository.SweetsInMemoryRepository;
import repository.sweetsRepository.SweetsRepository;
import service.ServiceImpl;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        startApp();
    }

    public static void startApp() {
        System.out.println("\nWELCOME MY FRIEND :)\n");

        Shop myShop = new Shop("Candy Crush Shop",
                new Location(1, "Romania", "Cluj-Napoca", "Str. Memorandumului, nr. 10"));

        //Repository
        SweetsRepository sweetsRepository = new SweetsInMemoryRepository(generateSweets());
        CustomersRepository customersRepository = new CustomersInMemoryRepository(generateCustomers());
        OrdersRepository ordersRepository = new OrdersInMemoryRepository(generateOrders(myShop, sweetsRepository, customersRepository));
        // +++ UseCase2-RepoIngredients +++

        //Service
        ServiceImpl service = new ServiceImpl(myShop, sweetsRepository, customersRepository, ordersRepository);

        //UI
        UI appUI = new UI(service);
        appUI.show();


        System.out.println("\nSEE YOU LATER, ALLIGATOR! :)\n");
    }


    private static List<Sweet> generateSweets() {
        List<Sweet> sweetList = new ArrayList<>();

        sweetList.add(new Sweet(1,
                new Recipe(1, new ArrayList<>(List.of(
                        new Ingredient(1, "Sugar", 1.5),
                        new Ingredient(2, "Milk", 1),
                        new Ingredient(3, "Flour", 0.75)))),
                SweetTypes.Donut, 5));

        sweetList.add(new Sweet(2,
                new Recipe(2, new ArrayList<>(List.of(
                        new Ingredient(4, "Sugar", 1.7),
                        new Ingredient(5, "Flour", 0.95)))),
                SweetTypes.Donut, 5.5));

        sweetList.add(new Sweet(3,
                new Recipe(3, new ArrayList<>(List.of(
                        new Ingredient(6, "Sugar", 2.7),
                        new Ingredient(7, "Milk", 3.7),
                        new Ingredient(8, "Flour", 3.7),
                        new Ingredient(9, "Chocolate", 10)))),
                SweetTypes.Cake, 23.55));

        sweetList.add(new Sweet(4,
                new Recipe(4, new ArrayList<>(List.of(
                        new Ingredient(10, "Sugar", 0.7),
                        new Ingredient(11, "Flour", 0.7),
                        new Ingredient(12, "Chocolate", 1.5)))),
                SweetTypes.Croissant, 3.99));


        sweetList.add(new Sweet(5,
                new Recipe(5, new ArrayList<>(List.of(
                        new Ingredient(13, "Sugar", 0.25),
                        new Ingredient(14, "Flour", 0.3),
                        new Ingredient(15, "Honey", 2.5)))),
                SweetTypes.Waffles, 4.99));


        sweetList.add(new Sweet(6,
                new Recipe(6, new ArrayList<>(List.of(
                        new Ingredient(16, "Sugar", 0.55),
                        new Ingredient(17, "Flour", 1),
                        new Ingredient(18, "Caramel", 1.25)))),
                SweetTypes.Croissant, 3.39));


        sweetList.add(new Sweet(7,
                new Recipe(7, new ArrayList<>(List.of(
                        new Ingredient(19, "Milk", 1.5),
                        new Ingredient(21, "Sugat", 1.2),
                        new Ingredient(22, "Chocolate", 5.5)))),
                SweetTypes.HomemadeChocolate, 13.39));

        sweetList.add(new Sweet(8,
                new Recipe(8, new ArrayList<>(List.of(
                        new Ingredient(23, "Sugar", 0.23),
                        new Ingredient(20, "Chocolate", 2.5)))),
                SweetTypes.Donut, 3.25));


        sweetList.add(new Sweet(9,
                new Recipe(9, new ArrayList<>(List.of(
                        new Ingredient(25, "Sugar", 1.2),
                        new Ingredient(98, "Whipped cream", 2.5),
                        new Ingredient(97, "Vanilla", 3.35),
                        new Ingredient(26, "Flour", 4.7),
                        new Ingredient(27, "Strawberries", 5.5)))),
                SweetTypes.Cake, 49.99));


        sweetList.add(new Sweet(10,
                new Recipe(10, new ArrayList<>(List.of(
                        new Ingredient(28, "Sugar", 0.56),
                        new Ingredient(29, "Vanilla", 0.53),
                        new Ingredient(30, "Cacao", 2.2)))),
                SweetTypes.HomemadeChocolate, 3.99));


        sweetList.add(new Sweet(11,
                new Recipe(11, new ArrayList<>(List.of(
                        new Ingredient(31, "Sugar", 0.55),
                        new Ingredient(33, "Flour", 1.03)))),
                SweetTypes.Croissant, 1.99));

        sweetList.add(new Sweet(12,
                new Recipe(12, new ArrayList<>(List.of(
                        new Ingredient(35, "Flour", 0.5),
                        new Ingredient(36, "Vanilla", 0.5)))),
                SweetTypes.Donut, 2.11));

        sweetList.add(new Sweet(13,
                new Recipe(13, new ArrayList<>(List.of(
                        new Ingredient(44, "Eggs", 1.2),
                        new Ingredient(45, "Flour", 0.31),
                        new Ingredient(46, "Peanut butter", 1.33)))),
                SweetTypes.Waffles, 3.75));

        sweetList.add(new Sweet(14,
                new Recipe(14, new ArrayList<>(List.of(
                        new Ingredient(47, "Eggs", 0.5),
                        new Ingredient(55, "Flour", 0.25),
                        new Ingredient(54, "Milk", 0.15)))),
                SweetTypes.Waffles, 1.99));

        sweetList.add(new Sweet(15,
                new Recipe(15, new ArrayList<>(List.of(
                        new Ingredient(58, "Flour", 0.45),
                        new Ingredient(66, "Cheese", 0.5)))),
                SweetTypes.Croissant, 0.99));

        return sweetList;
    }

    private static List<Customer> generateCustomers() {
        List<Customer> customerList = new ArrayList<>();

        customerList.add(new Customer(1, "Razvan", "Berendi",
                "br@gmail.com", "12345678", "0751578787",
                new Location(1, "Romania", "Cluj", "Aleea Rucar nr. 9, Bloc D13, ap. 1")));

        customerList.add(new Customer(2, "Ana", "Pop",
                "ap@gmail.com", "12345678", "0751578709",
                new Location(2, "Romania", "Cluj", "Aleea Peana nr. 9, Bloc D19, ap. 2")));

        customerList.add(new Customer(3, "Cristian", "Popescu",
                "cp@gmail.com", "12345678", "0751572287",
                new Location(3, "Romania", "Cluj", "Str. Mehedinti nr. 5, Bloc I3, ap. 1")));

        customerList.add(new Customer(4, "Rares", "Marina",
                "rm@gmail.com", "12345678", "0264578787",
                new Location(4, "Romania", "Cluj", "Str. Constanta nr. 9, Bloc A2, ap. 3")));

        customerList.add(new Customer(5, "Andreea", "Staciu",
                "asasr@gmail.com", "12345678", "0721578123",
                new Location(5, "Romania", "Cluj", "Str. Memo nr. 10, Casa nr. 15")));


        return customerList;
    }


    private static List<Order> generateOrders(Shop shop, SweetsRepository sweetsRepository, CustomersRepository customersRepository) {
        List<Order> orderList = new ArrayList<>();

        orderList.add(new PickUp(1,
                randomOrder(sweetsRepository.findAll()),
                randomCustomer(customersRepository.findAll()), shop));
        orderList.add(new PickUp(2,
                randomOrder(sweetsRepository.findAll()),
                randomCustomer(customersRepository.findAll()), shop));
        orderList.add(new Delivery(3,
                randomOrder(sweetsRepository.findAll()),
                randomCustomer(customersRepository.findAll()), shop));
        orderList.add(new Delivery(4,
                randomOrder(sweetsRepository.findAll()),
                randomCustomer(customersRepository.findAll()), shop));
        orderList.add(new PickUp(5,
                randomOrder(sweetsRepository.findAll()),
                randomCustomer(customersRepository.findAll()), shop));
        orderList.add(new Delivery(6,
                randomOrder(sweetsRepository.findAll()),
                randomCustomer(customersRepository.findAll()), shop));
        orderList.add(new PickUp(7,
                randomOrder(sweetsRepository.findAll()),
                randomCustomer(customersRepository.findAll()), shop));


        return orderList;
    }

    private static Map<Sweet, Integer> randomOrder(List<Sweet> all) {
        Random random = new Random();
        int randomNumberOfSweets = random.nextInt(10 - 1) + 1;

        Map<Sweet, Integer> sweetIntegerMap = new HashMap<>();

        while (randomNumberOfSweets > 0) {
            Sweet sweet = all.get(random.nextInt(all.size()));
            sweetIntegerMap.merge(sweet, 1, Integer::sum);

            randomNumberOfSweets--;
        }

        return sweetIntegerMap;
    }

    private static Customer randomCustomer(List<Customer> all) {
        Random random = new Random();
        return all.get(random.nextInt(all.size()));
    }

}
