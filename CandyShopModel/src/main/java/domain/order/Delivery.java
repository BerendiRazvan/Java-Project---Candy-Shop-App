package domain.order;

import domain.Customer;
import domain.Shop;
import domain.sweet.Sweet;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Delivery extends Order {

    private static final int MINIMUM_DELIVERY_TIME_MINUTES = 60;
    private LocalDateTime deliveryTime;

    public Delivery(long idOrder, Map<Sweet, Integer> orderedSweets, Customer customer, Shop shop) {
        super(idOrder, orderedSweets, customer, shop);
        this.deliveryTime = super.getOrderDateTime().plusMinutes(MINIMUM_DELIVERY_TIME_MINUTES);
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) throws Exception {
        if (deliveryTime.isAfter(this.deliveryTime))
            this.deliveryTime = deliveryTime;
        else
            throw new Exception("We are sorry. Minimum waiting time is 1 hour for every delivery!");
    }

    @Override
    public String toString() {
        return super.toString() +
                "ORDER EXPECTED TO ARRIVE: " + deliveryTime.format(DateTimeFormatter.ofPattern("EEE dd.MM.yyyy HH:mm")) +
                "\n" + "-".repeat(100) + "\n";
    }
}
