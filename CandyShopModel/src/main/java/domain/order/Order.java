package domain.order;

import domain.Customer;
import domain.Shop;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Order {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private @Getter @Setter long id;
    private @Getter @Setter Map<Sweet, Integer> orderedSweets;
    private @Getter @Setter Customer customer;
    private @Getter @Setter Shop shop;
    private @Getter @Setter LocalDateTime orderDateTime;
    private @Getter @Setter OrderType orderType;
    private @Getter LocalDateTime waitingTime;

    public Order(long id, Map<Sweet, Integer> orderedSweets, OrderType orderType, Customer customer, Shop shop) {
        this.id = id;
        this.orderedSweets = orderedSweets;
        this.customer = customer;
        this.shop = shop;
        this.orderDateTime = LocalDateTime.now();
        this.orderType = orderType;
        this.waitingTime = orderDateTime.plusMinutes(orderType.getMinimumWaitingTime());
    }

    public void setWaitingTime(LocalDateTime preparingTime) throws Exception {
        if (preparingTime.isAfter(this.waitingTime))
            this.waitingTime = preparingTime;
        else
            throw new Exception("We are sorry. " +
                    "Minimum waiting time is 60 minutes - for delivery and 30 minutes - for pick up, " +
                    "for every order!");
    }

    @Override
    public String toString() {
        return "\n" + "-".repeat(100) + "\n" +
                "\t".repeat(10) + "Order no." + id + "\t" + orderDateTime.format(DateTimeFormatter
                .ofPattern("EEE dd.MM.yyyy HH:mm")) +
                "\n" + "-".repeat(100) + "\n" +
                "Customer: " + customer.getFirstName() + " " + customer.getLastName() + " | " +
                customer.getPhoneNumber() + " | " + customer.getEmail() +
                "\n" + customer.getLocation() +
                "\n\nCandy Shop: " + shop.getName() +
                "\n" + shop.getLocation() +
                "\n" + "-".repeat(100) + "\n" +
                "Ordered:\n" + formatForPrintingOrderedSweets() +
                "\n" + "-".repeat(100) + "\n" +
                "YOUR ORDER WILL READY FOR " + orderType.toString() + " AT: " +
                waitingTime.format(DateTimeFormatter.ofPattern("EEE dd.MM.yyyy HH:mm")) +
                "\n" + "-".repeat(100) + "\n";
    }

    private String formatForPrintingOrderedSweets() {
        StringBuilder orderedSweetsInfo = new StringBuilder();
        for (Sweet sweet : orderedSweets.keySet())
            orderedSweetsInfo.append(formatForPrintingSweet(sweet));
        return orderedSweetsInfo.toString();
    }

    private String formatForPrintingSweet(Sweet sweet) {
        return "\n(Id:" + sweet.getId() + ")" +
                " " + sweet.getSweetType().getName() +
                " - quantity: " +
                orderedSweets.get(sweet) +
                " - price: " +
                orderedSweets.get(sweet) +
                "*" +
                df.format(sweet.getPrice()) +
                "$=" +
                df.format(orderedSweets.get(sweet) * sweet.getPrice()) +
                "$" +
                "\nRecipe:" +
                formatForPrintingIngredients(sweet.getIngredientsList()) +
                "\nExtra:" +
                sweet.getExtraIngredients() +
                "\n";
    }

    private String formatForPrintingIngredients(List<Ingredient> ingredientsList) {
        return ingredientsList
                .stream()
                .map(ingredient -> "(Id:" + ingredient.getId() + ") " + ingredient.getName())
                .collect(Collectors.toList())
                .toString();
    }

}
