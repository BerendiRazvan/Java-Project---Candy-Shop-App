package domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum OrderType {
    DELIVERY(60), PICKUP(30);

    private @Getter final int minimumWaitingTime;
}
