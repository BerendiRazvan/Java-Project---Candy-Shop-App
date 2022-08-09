package domain.order;

public enum OrderType {
    DELIVERY(60), PICKUP(30);

    private final int minimumWaitingTime;

    OrderType(int minimumWaitingTime) {
        this.minimumWaitingTime = minimumWaitingTime;
    }

    public int getMinimumWaitingTime() {
        return minimumWaitingTime;
    }
}
