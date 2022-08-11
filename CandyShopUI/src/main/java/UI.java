import domain.Shop;
import domain.sweet.Ingredient;
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
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final Scanner SCANNER = new Scanner(System.in);
    private final Shop shop;
    private final String menu;
    private CustomerService customerService;
    private SweetService sweetService;
    private OrderService orderService;
    private IngredientService ingredientService;

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
    }


    public void show() {
        label:
        while (true) {
            printShopInfo();
            System.out.println(menu);

            System.out.print("Choose an option: ");
            String option = SCANNER.nextLine().toUpperCase();

            switch (option) {
                case "1":
                    optionOrderSweets();
                    break;
                case "2":
                    optionPrintOrderDetails();
                    break;
                case "3":
                    optionViewOrdersAndProfitForADay();
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

    private void optionOrderSweets() {
        printShopSweets();
        OrderSweetUI orderSweetUI = new OrderSweetUI(shop, customerService, sweetService, orderService, ingredientService);
        orderSweetUI.show();
    }

    private void optionPrintOrderDetails() {
        System.out.print("Enter order number to print details: ");
        String orderNumber = SCANNER.nextLine();

        try {
            System.out.println(orderService.getOrderDetails(orderNumber));
            System.out.println("Order details printed\n");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private void optionViewOrdersAndProfitForADay() {
        System.out.println("\nToday's orders: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy")));
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
            System.out.println("(Id:" + sweet.getId() + ") " + sweet.getSweetType() + " - " + sweet.getPrice()
                    + "$");
        }

        System.out.println("\n" + "-".repeat(100) + "\n");

        System.out.println("Available ingredients: \n");
        ingredientService.showAllIngredientsInStock().forEach(System.out::println);

        System.out.println("\n" + "-".repeat(100) + "\n");
    }

    private void printShopSweets() {
        System.out.println("\n" + "-".repeat(100) + "\n");
        System.out.print("Available sweets:");
        for (var sweet : sweetService.getAvailableSweets()) {
            System.out.print("\n\n(Id:" + sweet.getId() + ") " + sweet.getSweetType() + " - " + sweet.getPrice()
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
