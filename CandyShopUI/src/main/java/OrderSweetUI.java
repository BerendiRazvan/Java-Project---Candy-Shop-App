import domain.Customer;
import domain.Shop;
import domain.location.Location;
import domain.order.Order;
import domain.order.OrderType;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import lombok.Builder;
import service.customerService.CustomerService;
import service.exception.ServiceException;
import service.ingredientService.IngredientService;
import service.orderService.OrderService;
import service.sweetService.SweetService;

import java.util.Optional;
import java.util.Scanner;

public class OrderSweetUI {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String expressionVerification = "^(([a-zA-Z ]*)(,)(\\d*)(;))*$";
    private final Shop shop;
    private final String menu;

    private CustomerService customerService;
    private SweetService sweetService;
    private OrderService orderService;
    private IngredientService ingredientService;

    @Builder
    public OrderSweetUI(Shop shop, CustomerService customerService, SweetService sweetService, OrderService orderService,
                        IngredientService ingredientService) {
        this.shop = shop;
        this.customerService = customerService;
        this.sweetService = sweetService;
        this.orderService = orderService;
        this.ingredientService = ingredientService;

        menu = "\nOptions:\n" +
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
        Customer customer = null;
        while (customer == null) {
            Optional<Customer> customerOptional = loginOption();
            if (customerOptional.isPresent())
                customer = customerOptional.get();
        }

        System.out.println("\nYour account is:\n");
        System.out.println(customer);

        OrderType orderType = OrderType.DELIVERY;
        System.out.print("\nYour order will be delivered to you, do you want delivery with pickup?\nAnswer (Yes/No):");

        String deliveryOpt = SCANNER.nextLine().toUpperCase();

        if (deliveryOpt.matches("YES"))
            orderType = OrderType.PICKUP;


        try {
            Optional<Order> optionalOrder = orderService.createOrder(customer, orderType, shop);
            Order order;
            if (optionalOrder.isPresent())
                order = optionalOrder.get();
            else throw new RuntimeException("Error: createOrder");
            label:
            while (true) {
                System.out.println(menu);

                System.out.print("Choose an option: ");
                String option = SCANNER.nextLine().toUpperCase();

                switch (option) {
                    case "1":
                        option1(order);
                        break;
                    case "2":
                        option2(order);
                        break;
                    case "3":
                        option3(order);
                        break;
                    case "4":
                        option4(order);
                        break;
                    case "5":
                        option5(order);
                        break;
                    case "6":
                        System.out.println(orderService.getOrderDetails(String.valueOf(order.getId())));
                        break;
                    case "7":
                        System.out.println(orderService.getOrderDetails(String.valueOf(order.getId())));
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

    private void option5(Order order) {
        System.out.println("\nAvailable ingredients:");
        ingredientService.showAllIngredientsInStock().forEach(System.out::println);
        try {
            Optional<Sweet> customSweet = sweetService.createNewSweetWithoutIngredients();
            System.out.print("Enter ingredients to add (ingredient1,amount1;ingredient2,amount2;...): ");
            String ingredients = SCANNER.nextLine();
            if (ingredients.matches(expressionVerification) && customSweet.isPresent())
                try {
                    sweetService.addAllIngredientsToSweet(customSweet.get(), ingredients);
                    orderService.addToOrder(order, customSweet.get());
                    System.out.println(customSweet);
                    System.out.println("Sweet added, yummy :)");
                } catch (ServiceException e) {
                    System.out.println(e.getMessage());
                }
            else
                System.out.println("Invalid input for ingredients to add :(");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private void option1(Order order) {
        System.out.print("Choose a sweet (enter sweet id): ");
        String sweetId = SCANNER.nextLine().toUpperCase();
        try {
            Optional<Sweet> sweetOptional = sweetService.findSweetById(sweetId);
            if (sweetOptional.isPresent()) {
                orderService.addToOrder(order, sweetOptional.get());
                System.out.println("Sweet added, yummy :)");
            } else throw new ServiceException("Invalid sweet id!");
        } catch (ServiceException e) {
            System.out.println("Oh no, we failed to add your sweet :(");
            System.out.println(e.getMessage());
        }
    }

    private void option2(Order order) {
        System.out.println("Available ingredients:");
        ingredientService.showAllIngredientsInStock().forEach(System.out::println);
        try {
            System.out.println(orderService.getOrderDetails(String.valueOf(order.getId())));
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
        System.out.print("Enter the ID for the ordered sweet you want to change:");
        String orderedSweetIdForAdd = SCANNER.nextLine();
        System.out.print("Enter the ID for the extra ingredient you want to add:");
        String ingredientIdForAdd = SCANNER.nextLine();
        System.out.print("Enter the amount you want to add:");
        String amountForAdd = SCANNER.nextLine();
        try {
            Optional<Ingredient> ingredientOptional = ingredientService.findIngredientById(ingredientIdForAdd);
            Optional<Sweet> sweetOptional = sweetService.findSweetById(orderedSweetIdForAdd);
            if (ingredientOptional.isPresent() && sweetOptional.isPresent()) {
                orderService.addExtraIngredientToOrderedSweet(order,
                        sweetOptional.get(),
                        ingredientOptional.get(), amountForAdd);
                System.out.println("Sweet modified :)");
            } else throw new ServiceException("Invalid sweet/ingredient id!");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private void option3(Order order) {
        System.out.println("Available ingredients:");
        ingredientService.showAllIngredientsInStock().forEach(System.out::println);
        try {
            System.out.println(orderService.getOrderDetails(String.valueOf(order.getId())));
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
        System.out.print("Enter the ID for the ordered sweet you want to change:");
        String orderedSweetIdForUpdate = SCANNER.nextLine();
        System.out.print("Enter the ID for the extra ingredient you want to update:");
        String ingredientIdForUpdate = SCANNER.nextLine();
        System.out.print("Enter the amount you want:");
        String amountForUpdate = SCANNER.nextLine();
        try {
            Optional<Ingredient> ingredientOptional = ingredientService.findIngredientById(ingredientIdForUpdate);
            Optional<Sweet> sweetOptional = sweetService.findSweetById(orderedSweetIdForUpdate);
            if (ingredientOptional.isPresent() && sweetOptional.isPresent()) {
                orderService.updateExtraIngredientForOrderedSweet(order,
                        sweetOptional.get(),
                        ingredientOptional.get(), amountForUpdate);
                System.out.println("Sweet modified :)");
            } else throw new ServiceException("Invalid sweet/ingredient id!");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private void option4(Order order) {
        System.out.println("Available ingredients:");
        ingredientService.showAllIngredientsInStock().forEach(System.out::println);
        try {
            System.out.println(orderService.getOrderDetails(String.valueOf(order.getId())));
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
        System.out.print("Enter the ID for the ordered sweet you want to change:");
        String orderedSweetIdForDelete = SCANNER.nextLine();
        System.out.print("Enter the ID for the extra ingredient you want to delete:");
        String ingredientIdForDelete = SCANNER.nextLine();
        try {
            Optional<Ingredient> ingredientOptional = ingredientService.findIngredientById(ingredientIdForDelete);
            Optional<Sweet> sweetOptional = sweetService.findSweetById(orderedSweetIdForDelete);
            if (ingredientOptional.isPresent() && sweetOptional.isPresent()) {
                orderService.deleteExtraIngredientForOrderedSweet(order,
                        sweetOptional.get(),
                        ingredientOptional.get());
                System.out.println("Sweet modified :)");
            } else throw new ServiceException("Invalid sweet/ingredient id!");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }


    private Optional<Customer> loginOption() {
        System.out.println("\nAuthentication");
        System.out.print("Mail = ");
        String mail = SCANNER.nextLine();

        if (customerService.checkIfEmailExists(mail)) {
            System.out.print("Password = ");
            String password = SCANNER.nextLine();
            try {
                return customerService.login(mail, password);
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
                return Optional.empty();
            }
        } else {
            System.out.print("First name = ");
            String firstName = SCANNER.nextLine();
            System.out.print("Last name = ");
            String lastName = SCANNER.nextLine();
            System.out.print("Your password = ");
            String password = SCANNER.nextLine();
            System.out.print("Phone number = ");
            String phone = SCANNER.nextLine();
            System.out.print("Address (street, number, building) = ");
            String address = SCANNER.nextLine();

            try {
                return customerService.createAccount(firstName, lastName, mail, password, phone,
                        new Location("Romania", "Cluj-Napoca", address));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return Optional.empty();
            }
        }
    }
}
