import domain.Customer;
import domain.location.Location;
import domain.order.Order;
import domain.order.OrderTypes;
import domain.sweet.Ingredient;
import service.ServiceImpl;
import service.ServiceException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UI {
    private ServiceImpl service;
    private final String menu;
    private final String menuOpt1;

    public UI(ServiceImpl service) {
        this.service = service;

        menu = "\nOptions:\n" +
                "1 - Order sweets\n" +
                "2 - Print order details\n" +
                "3 - View orders and profit for a day\n" +
                "X - Exit";

        menuOpt1 = "\nOptions:\n" +
                "1 - Add sweet\n" +
                "2 - Finish order\n" +
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

        OrderTypes orderType = OrderTypes.Delivery;
        System.out.println("\nYour order will be delivered to you, do you want delivery with pickup?\nAnswer (Yes/No):");

        String deliveryOpt = scanner.nextLine().toUpperCase();

        if (deliveryOpt.matches("YES"))
            orderType = OrderTypes.PickUp;


        try {
            //init order
            Order order = service.createOrder(customer, orderType);
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
                            service.addToOrder(order, sweetId);
                            System.out.println("Sweet added, yummy :)");
                        } catch (ServiceException e) {
                            System.out.println("Oh no, we failed to add your sweet :(");
                        }
                        break;
                    case "2":
                        System.out.println(service.getOrderDetails(order.getIdOrder()));
                        break label;
                    case "X":
                        service.removeOrder(order.getIdOrder());
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

        if (service.findMail(mail)) {
            System.out.print("Password = ");
            String password = scanner.nextLine();
            try {
                return service.login(mail, password);
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
                return service.createAccount(firstName, lastName, mail, password, phone,
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
            System.out.println(service.printOrderDetails(orderNumber));
            System.out.println("Order details printed\n");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }

    }

    private static final DecimalFormat df = new DecimalFormat("0.00");

    private void option3() {
        //  •	Print all the orders and the profit for the day (the profit is the total sum of the orders)
        System.out.println("\nToday's orders:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE dd.MM.yyyy")));
        service.getAllOrdersInADay()
                .stream()
                .map(order -> new String("Order no. " + order.getIdOrder() + " | "
                        + df.format(order.getFinalOrderPrice()) + "$ | Hour: " +
                        order.getOrderDateTime().format(DateTimeFormatter.ofPattern("HH:mm"))))
                .collect(Collectors.toList())
                .forEach(System.out::println);
        System.out.print("Money made today: " + df.format(service.getMoneyMadeToday()) + "$\n" +
                "Actual profit made today: " + df.format(service.getProfitMadeToday()) + "$\n");
    }

    private void printShopInfo() {
        System.out.println("\n\n" + "-".repeat(100) + "\n" +
                "\t".repeat(10) + service.getShopName() +
                "\n" + "-".repeat(100) + "\n");

        System.out.println("Available sweets: \n");
        for (var sweet : service.getAvailableSweets()) {
            System.out.println(sweet.getIdSweet() + ". " + sweet.getSweetType() + " - " + sweet.getPrice()
                    + "$");
//            System.out.print("\n" + sweet.getIdSweet() + ". " + sweet.getSweetType() + " - " + sweet.getPrice()
//                    + "$\nRecipe: " + sweet.getSweetRecipe().getIngredientsList() + "\n");
        }

        System.out.println("\n" + "-".repeat(100) + "\n");

    }

    private void printShopSweets() {
        System.out.println("\n" + "-".repeat(100) + "\n");
        System.out.print("Available sweets:");
        for (var sweet : service.getAvailableSweets()) {
            System.out.print("\n\n" + sweet.getIdSweet() + ". " + sweet.getSweetType() + " - " + sweet.getPrice()
                    + "$\nRecipe: ");
            sweet.getSweetRecipe().getIngredientsList().stream()
                    .map(Ingredient::getName)
                    .collect(Collectors.toList())
                    .forEach(System.out::print);
        }
        System.out.println("\n" + "-".repeat(100) + "\n");
    }
}
