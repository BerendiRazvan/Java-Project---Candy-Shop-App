package domain.order;

import domain.Customer;
import domain.Shop;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Order {
    private long id;
    private Map<Sweet, Integer> orderedSweets;
    private Customer customer;
    private Shop shop;
    private LocalDateTime orderDateTime;
    private OrderType orderType;

    private LocalDateTime waitingTime;

    public Order(long id, Map<Sweet, Integer> orderedSweets, OrderType orderType, Customer customer, Shop shop) {
        this.id = id;
        this.orderedSweets = orderedSweets;
        this.customer = customer;
        this.shop = shop;
        this.orderDateTime = LocalDateTime.now();
        this.orderType = orderType;
        this.waitingTime = orderDateTime.plusMinutes(orderType.getMinimumWaitingTime());
    }

    public LocalDateTime getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(LocalDateTime preparingTime) throws Exception {
        if (preparingTime.isAfter(this.waitingTime))
            this.waitingTime = preparingTime;
        else
            throw new Exception("We are sorry. " +
                    "Minimum waiting time is 60 minutes - for delivery and 30 minutes - for pick up, " +
                    "for every order!");
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<Sweet, Integer> getOrderedSweets() {
        return orderedSweets;
    }

    public void setOrderedSweets(Map<Sweet, Integer> orderedSweets) {
        this.orderedSweets = orderedSweets;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    private static final DecimalFormat df = new DecimalFormat("0.00");

    private String formatForPrintingOrderedSweets() {
        StringBuilder orderedSweetsInfo = new StringBuilder();
        for (Sweet sweet : orderedSweets.keySet())
            orderedSweetsInfo.append(formatForPrintingSweet(sweet));
        return orderedSweetsInfo.toString();
    }

    private String formatForPrintingSweet(Sweet sweet) {
        return "\n" +
                sweet.getSweetType() +
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
                .map(Ingredient::getName)
                .collect(Collectors.toList())
                .toString();
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
}
