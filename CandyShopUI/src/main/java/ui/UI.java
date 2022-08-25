package ui;

import builder.OrderSweetUIBuilder;
import domain.Shop;
import domain.sweet.Ingredient;
import exception.ValidationException;
import exception.CandyShopException;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.customerService.CustomerService;
import service.ingredientService.IngredientService;
import service.orderService.OrderService;
import service.sweetService.SweetService;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.stream.Collectors;


public class UI {
    private static final Logger LOGGER = LoggerFactory.getLogger(UI.class);
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final Scanner SCANNER = new Scanner(System.in);
    private final Shop shop;
    private final String menu;
    private CustomerService customerService;
    private SweetService sweetService;
    private OrderService orderService;
    private IngredientService ingredientService;

    @Builder
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


    public void show() throws ValidationException {
        LOGGER.info("Show - started");
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
        LOGGER.info("Show - finished");
    }

    private void optionOrderSweets() throws ValidationException {
        LOGGER.info("OptionOrderSweets - started");
        printShopSweets();
        OrderSweetUIBuilder orderSweetUIBuilder = new OrderSweetUIBuilder();
        OrderSweetUI orderSweetUI = orderSweetUIBuilder.build(shop, customerService, sweetService, orderService,
                ingredientService);
        orderSweetUI.show();
        LOGGER.info("OptionOrderSweets - finished");
    }

    private void optionPrintOrderDetails() {
        LOGGER.info("OptionPrintOrderDetails - started");
        System.out.print("Enter order number to print details: ");
        String orderNumber = SCANNER.nextLine();

        try {
            System.out.println(orderService.getOrderDetails(orderNumber));
            System.out.println("Order details printed\n");
        } catch (CandyShopException e) {
            System.out.println(e.getMessage());
        }
        LOGGER.info("OptionPrintOrderDetails - finished");
    }

    private void optionViewOrdersAndProfitForADay() {
        LOGGER.info("OptionViewOrdersAndProfitForADay - started");
        System.out.println("\nToday's orders: " + LocalDateTime.now().format(DateTimeFormatter
                .ofPattern("EEEE, dd.MM.yyyy")));
        orderService.getAllOrdersInADay()
                .stream()
                .map(order -> "Order no. " + order.getId() + " | "
                        + df.format(orderService.getFinalOrderPrice(order)) + "$ | Hour: " +
                        order.getOrderDateTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .collect(Collectors.toList())
                .forEach(System.out::println);
        System.out.print("Money made today: " + df.format(orderService.getMoneyMadeToday()) + "$\n" +
                "Actual profit made today: " + df.format(orderService.getProfitMadeToday()) + "$\n");
        LOGGER.info("OptionViewOrdersAndProfitForADay - finished");
    }

    private void printShopInfo() {
        LOGGER.info("PrintShopInfo - started");
        System.out.println("\n\n" + "-".repeat(100) + "\n" +
                "\t".repeat(10) + shop.getName() +
                "\n" + "-".repeat(100) + "\n");

        System.out.println("Available sweets: \n");
        for (var sweet : sweetService.getAvailableSweets())
            System.out.println("(Id:" + sweet.getId() + ") " + sweet.getSweetType() + " - " +
                    df.format(sweet.getPrice()) + "$");

        System.out.println("\n" + "-".repeat(100) + "\n");

        System.out.println("Available ingredients: \n");
        ingredientService.showAllIngredientsInStock().forEach(System.out::println);

        System.out.println("\n" + "-".repeat(100) + "\n");
        LOGGER.info("PrintShopInfo - finished");
    }

    private void printShopSweets() {
        LOGGER.info("PrintShopSweets - started");
        System.out.println("\n" + "-".repeat(100) + "\n");
        System.out.print("Available sweets:");
        for (var sweet : sweetService.getAvailableSweets()) {
            System.out.print("\n\n(Id:" + sweet.getId() + ") " + sweet.getSweetType() + " - " +
                    df.format(sweet.getPrice()) + "$\nRecipe: ");
            sweet.getIngredientsList()
                    .stream()
                    .map(Ingredient::getName)
                    .collect(Collectors.toList())
                    .forEach(ingredient -> System.out.print(ingredient + ", "));
        }
        System.out.println("\n" + "-".repeat(100) + "\n");
        LOGGER.info("PrintShopSweets - finished");
    }
}
