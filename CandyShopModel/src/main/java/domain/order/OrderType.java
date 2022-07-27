package domain.order;

public enum OrderType {
    DELIVERY(60), PICKUP(30);

    private int minimumWaitingTime;

    OrderType(int minimumWaitingTime) {
        this.minimumWaitingTime = minimumWaitingTime;
    }

    public int getMinimumWaitingTime() {
        return minimumWaitingTime;
    }
}
