package domain.order;

import domain.Customer;
import domain.Shop;
import domain.sweet.Sweet;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class PickUp extends Order {
    private static final int MINIMUM_PICKUP_TIME_MINUTES = 30;
    private LocalDateTime preparingTime;

    public PickUp(long idOrder, Map<Sweet, Integer> orderedSweets, Customer customer, Shop shop) {
        super(idOrder, orderedSweets, customer, shop);
        this.preparingTime = super.getOrderDateTime().plusMinutes(MINIMUM_PICKUP_TIME_MINUTES);
    }

    public int getMINIMUM_PICKUP_TIME_MINUTES() {
        return MINIMUM_PICKUP_TIME_MINUTES;
    }

    public LocalDateTime getDeliveryTime() {
        return preparingTime;
    }

    public void setDeliveryTime(LocalDateTime preparingTime) throws Exception {
        if (preparingTime.isAfter(this.preparingTime))
            this.preparingTime = preparingTime;
        else
            throw new Exception("We are sorry. Minimum preparing time is 30 minutes for every order!");
    }

    @Override
    public String toString() {
        return super.toString() +
                "ORDER EXPECTED TO PEAK-UP: "
                + preparingTime.format(DateTimeFormatter.ofPattern("EEE dd.MM.yyyy HH:mm")) +
                "\n" + "-".repeat(100) + "\n";
    }
}
