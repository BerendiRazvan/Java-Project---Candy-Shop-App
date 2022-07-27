package domain.order;

public enum OrderType {
    DELIVERY(60), PICK_UP(30);

    private int minimumWaitingTime;

    OrderType(int minimumWaitingTime) {
        this.minimumWaitingTime = minimumWaitingTime;
    }

    public int getMinimumWaitingTime() {
        return minimumWaitingTime;
    }
}
