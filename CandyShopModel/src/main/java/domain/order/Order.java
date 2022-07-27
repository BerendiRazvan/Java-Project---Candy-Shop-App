package domain.order;

import domain.Customer;
import domain.Shop;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

public class Order {
    private long idOrder;
    private Map<Sweet, Integer> orderedSweets;
    private Customer customer;
    private Shop shop;
    private LocalDateTime orderDateTime;
    private OrderType orderType;

    private LocalDateTime waitingTime;

    public Order(long idOrder, Map<Sweet, Integer> orderedSweets, OrderType orderType, Customer customer, Shop shop) {
        this.idOrder = idOrder;
        this.orderedSweets = orderedSweets;
        this.customer = customer;
        this.shop = shop;
        this.orderDateTime = LocalDateTime.now();
        this.orderType = orderType;
        if (orderType == OrderType.PICK_UP)
            this.waitingTime = orderDateTime.plusMinutes(orderType.getMinimumWaitingTime());
        else
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

    public long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(long idOrder) {
        this.idOrder = idOrder;
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


    public double getFinalOrderPrice() {
        double totalToPay = 0;
        for (Sweet sweet : orderedSweets.keySet()) {
            totalToPay += orderedSweets.get(sweet) * sweet.getPrice();
        }
        return totalToPay;
    }


    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public String toString() {
        StringBuilder ordered = new StringBuilder();
        for (Sweet sweet : orderedSweets.keySet()) {
            ordered.append("\n").append(sweet.getSweetType()).append(" - quantity: ").append(orderedSweets.get(sweet)).append(" - price: ").append(orderedSweets.get(sweet)).append("*").append(df.format(sweet.getPrice())).append("$=").append(df.format(orderedSweets.get(sweet) * sweet.getPrice())).append("$").append("\nRecipe:").append(sweet.getSweetRecipe()
                    .getIngredientsList()
                    .stream()
                    .map(Ingredient::getName)
                    .collect(Collectors.toList())).append("\nExtra:").append(sweet.getSweetRecipe().getExtraIngredients()).append("\n");
        }
        return "\n" + "-".repeat(100) + "\n" +
                "\t".repeat(10) + "Order no." + idOrder + "\t" + orderDateTime.format(DateTimeFormatter
                .ofPattern("EEE dd.MM.yyyy HH:mm")) +
                "\n" + "-".repeat(100) + "\n" +
                "Customer: " + customer.getFirstName() + " " + customer.getLastName() + " | " +
                customer.getPhoneNumber() + " | " + customer.getEmail() +
                "\n" + customer.getCustomerLocation() +
                "\n\nCandy Shop: " + shop.getShopName() +
                "\n" + shop.getShopLocation() +
                "\n" + "-".repeat(100) + "\n" +
                "Ordered:\n" + ordered +
                "\n" + "-".repeat(100) + "\n" +
                "TOTAL TO PAY: " + df.format(getFinalOrderPrice()) + "$" +
                "\n" + "-".repeat(100) + "\n" +
                "YOUR ORDER WILL BE FINISHED AT:\t" +
                waitingTime.format(DateTimeFormatter.ofPattern("EEE dd.MM.yyyy HH:mm")) +
                "\t(" + orderType.toString() + ")" +
                "\n" + "-".repeat(100) + "\n";
    }
}
