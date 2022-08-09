import domain.Customer;
import domain.Shop;
import domain.location.Location;
import domain.order.Order;
import domain.order.OrderType;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import service.customerService.CustomerService;
import service.exception.ServiceException;
import service.ingredientService.IngredientService;
import service.orderService.OrderService;
import service.sweetService.SweetService;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UI {
    private final Shop shop;
    private CustomerService customerService;
    private SweetService sweetService;
    private OrderService orderService;
    private IngredientService ingredientService;

    private final String menu;
    private final String menuOpt1;

    private  static final String expressionVerification = "^(([a-zA-Z ]*)(,)(\\d*)(;))*$";

    public UI(Shop shop, CustomerService customerService, SweetService sweetService, OrderService orderService,
              IngredientService ingredientService) {
        this.shop = shop;
        this.customerService = customerService;
        this.sweetService = sweetService;
        this.orderService = orderService;
        this.ingredientService = ingredientService;

        menu = "\nOptions:\n" +
                "1 - Order sweets\n" +
                "2 - Print order details\n" +
                "3 - View orders and profit for a day\n" +
                "X - Exit";

        menuOpt1 = "\nOptions:\n" +
                "1 - Add sweet to order\n" +
                "2 - Add extra ingredients for an ordered sweet\n" +
                "3 - Update amount of an ingredient for an ordered sweet\n" +
                "4 - Remove extra ingredients for an ordered sweet\n" +
                "5 - Create and add custom sweet\n" +
                "6 - View order details\n" +
                "7 - Finish order\n" +
                "X - Cancel and exit";
    }


    public void show() {
        label:
        while (true) {
            printShopInfo();
            System.out.println(menu);

            Scanner scanner = new Scanner(System.in);
            System.out.print("Choose an option: ");
            String option = scanner.nextLine().toUpperCase();

            switch (option) {
                case "1":
                    option1();
                    break;
                case "2":
                    option2();
                    break;
                case "3":
                    option3();
                    break;
                case "X":
                    System.out.println("Bye, see you next time!\n");
                    break label;
                default:
                    System.out.println("This is an invalid option! Try again...\n");
                    break;
            }

        }

    }


    private void option1() {
        //  •	Ordered sweets: The user should be able to select what type of sweet to be ordered,
        //          introduce the customer name and order type.
        Scanner scanner = new Scanner(System.in);

        printShopSweets();

        Customer customer = null;
        while (customer == null) {
            customer = loginOption();
        }

        System.out.println("\nYour account is:\n");
        System.out.println(customer);

        OrderType orderType = OrderType.DELIVERY;
        System.out.print("\nYour order will be delivered to you, do you want delivery with pickup?\nAnswer (Yes/No):");

        String deliveryOpt = scanner.nextLine().toUpperCase();

        if (deliveryOpt.matches("YES"))
            orderType = OrderType.PICKUP;


        try {
            //init order
            Order order = orderService.createOrder(customer, orderType, shop);
            label:
            while (true) {
                System.out.println(menuOpt1);

                System.out.print("Choose an option: ");
                String option = scanner.nextLine().toUpperCase();

                switch (option) {
                    case "1":
                        System.out.print("Choose a sweet (enter sweet id): ");
                        String sweetId = scanner.nextLine().toUpperCase();
                        try {
                            orderService.addToOrder(order, sweetService.findSweetById(sweetId));
                            System.out.println("Sweet added, yummy :)");
                        } catch (ServiceException e) {
                            System.out.println("Oh no, we failed to add your sweet :(");
                            System.out.println(e.getMessage());
                        }
                        break;
                    case "2":
                        System.out.println("Available ingredients:");
                        ingredientService.showAllIngredientsInStock().forEach(System.out::print);
                        System.out.println(orderService.getOrderDetails(order.getId()));
                        System.out.print("Enter the ID for the ordered sweet you want to change:");
                        String orderedSweetIdForAdd = scanner.nextLine();
                        System.out.print("Enter the ID for the extra ingredient you want to add:");
                        String ingredientIdForAdd = scanner.nextLine();
                        System.out.print("Enter the amount you want to add:");
                        String amountForAdd = scanner.nextLine();
                        try {
                            orderService.addExtraIngredientToOrderedSweet(order,
                                    sweetService.findSweetById(orderedSweetIdForAdd),
                                    ingredientService.findIngredientById(ingredientIdForAdd), amountForAdd);
                            System.out.println("Sweet modified :)");
                        } catch (ServiceException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case "3":
                        System.out.println("Available ingredients:");
                        ingredientService.showAllIngredientsInStock().forEach(System.out::print);
                        System.out.println(orderService.getOrderDetails(order.getId()));
                        System.out.print("Enter the ID for the ordered sweet you want to change:");
                        String orderedSweetIdForUpdate = scanner.nextLine();
                        System.out.print("Enter the ID for the extra ingredient you want to update:");
                        String ingredientIdForUpdate = scanner.nextLine();
                        System.out.print("Enter the amount you want:");
                        String amountForUpdate = scanner.nextLine();
                        try {
                            orderService.updateExtraIngredientForOrderedSweet(order,
                                    sweetService.findSweetById(orderedSweetIdForUpdate),
                                    ingredientService.findIngredientById(ingredientIdForUpdate), amountForUpdate);
                            System.out.println("Sweet modified :)");
                        } catch (ServiceException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case "4":
                        System.out.println("Available ingredients:");
                        ingredientService.showAllIngredientsInStock().forEach(System.out::print);
                        System.out.println(orderService.getOrderDetails(order.getId()));
                        System.out.print("Enter the ID for the ordered sweet you want to change:");
                        String orderedSweetIdForDelete = scanner.nextLine();
                        System.out.print("Enter the ID for the extra ingredient you want to delete:");
                        String ingredientIdForDelete = scanner.nextLine();
                        try {
                            orderService.deleteExtraIngredientForOrderedSweet(order,
                                    sweetService.findSweetById(orderedSweetIdForDelete),
                                    ingredientService.findIngredientById(ingredientIdForDelete));
                            System.out.println("Sweet modified :)");
                        } catch (ServiceException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case "5":
                        System.out.println("\nAvailable ingredients:");
                        ingredientService.showAllIngredientsInStock().forEach(System.out::print);

                        Sweet customSweet = sweetService.createEmptySweet();
                        System.out.print("Enter ingredients to add (ingredient1,amount1;ingredient2,amount2;...): ");
                        String ingredients = scanner.nextLine();
                        if (ingredients.matches(expressionVerification))
                            try {
                                sweetService.addAllIngredientsToSweet(customSweet, ingredients);
                                orderService.addToOrder(order, customSweet);
                                System.out.println(customSweet);
                                System.out.println("Sweet added, yummy :)");
                            } catch (ServiceException e) {
                                System.out.println(e.getMessage());
                            }
                        else
                            System.out.println("Invalid input for ingredients to add :(");
                        break;
                    case "6":
                        System.out.println(orderService.getOrderDetails(order.getId()));
                        break;
                    case "7":
                        System.out.println(orderService.getOrderDetails(order.getId()));
                        break label;
                    case "X":
                        orderService.removeOrder(order.getId());
                        System.out.println("Order deleted!\n");
                        break label;
                    default:
                        System.out.println("This is an invalid option! Try again...\n");
                        break;
                }
            }
        } catch (ServiceException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private Customer loginOption() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nAuthentication");
        System.out.print("Mail = ");
        String mail = scanner.nextLine();

        if (customerService.checkIfEmailExists(mail)) {
            System.out.print("Password = ");
            String password = scanner.nextLine();
            try {
                return customerService.login(mail, password);
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
                return null;
            }
        } else {
            System.out.print("First name = ");
            String firstName = scanner.nextLine();
            System.out.print("Last name = ");
            String lastName = scanner.nextLine();
            System.out.print("Your password = ");
            String password = scanner.nextLine();
            System.out.print("Phone number = ");
            String phone = scanner.nextLine();
            System.out.print("Address (street, number, building) = ");
            String address = scanner.nextLine();

            try {
                return customerService.createAccount(firstName, lastName, mail, password, phone,
                        new Location("Romania", "Cluj-Napoca", address));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
    }

    private void option2() {
        //  •	Print the order details
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter order number to print details: ");
        String orderNumber = scanner.nextLine();

        try {
            System.out.println(orderService.printOrderDetails(orderNumber));
            System.out.println("Order details printed\n");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }

    }

    private static final DecimalFormat df = new DecimalFormat("0.00");

    private void option3() {
        //  •	Print all the orders and the profit for the day (the profit is the total sum of the orders)
        System.out.println("\nToday's orders:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE dd.MM.yyyy")));
        orderService.getAllOrdersInADay()
                .stream()
                .map(order -> "Order no. " + order.getId() + " | "
                        + df.format(orderService.getFinalOrderPrice(order)) + "$ | Hour: " +
                        order.getOrderDateTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .collect(Collectors.toList())
                .forEach(System.out::println);
        System.out.print("Money made today: " + df.format(orderService.getMoneyMadeToday()) + "$\n" +
                "Actual profit made today: " + df.format(orderService.getProfitMadeToday()) + "$\n");
    }

    private void printShopInfo() {
        System.out.println("\n\n" + "-".repeat(100) + "\n" +
                "\t".repeat(10) + shop.getName() +
                "\n" + "-".repeat(100) + "\n");

        System.out.println("Available sweets: \n");
        for (var sweet : sweetService.getAvailableSweets()) {
            System.out.println(sweet.getId() + ". " + sweet.getSweetType() + " - " + sweet.getPrice()
                    + "$");
        }

        System.out.println("\n" + "-".repeat(100) + "\n");

        System.out.println("Available ingredients: \n");
        ingredientService.showAllIngredientsInStock().forEach(System.out::print);

        System.out.println("\n" + "-".repeat(100) + "\n");

    }

    private void printShopSweets() {
        System.out.println("\n" + "-".repeat(100) + "\n");
        System.out.print("Available sweets:");
        for (var sweet : sweetService.getAvailableSweets()) {
            System.out.print("\n\n" + sweet.getId() + ". " + sweet.getSweetType() + " - " + sweet.getPrice()
                    + "$\nRecipe: ");
            sweet.getIngredientsList()
                    .stream()
                    .map(Ingredient::getName)
                    .collect(Collectors.toList())
                    .forEach(ingredient -> System.out.print(ingredient + ", "));
        }
        System.out.println("\n" + "-".repeat(100) + "\n");
    }
}
