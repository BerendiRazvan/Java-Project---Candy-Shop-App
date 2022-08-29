package builder;

import domain.Customer;
import domain.Shop;
import domain.order.Order;
import domain.order.OrderType;
import domain.sweet.Sweet;
import exception.ValidationException;
import lombok.NoArgsConstructor;
import validator.OrderValidator;

import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
public class OrderBuilder {
    public Order build(long id, Map<Sweet, Integer> orderedSweets, OrderType orderType, Customer customer, Shop shop)
            throws ValidationException {
        Order order = Order.builder()
                .id(id)
                .orderedSweets(orderedSweets)
                .orderType(orderType)
                .orderDateTime(LocalDateTime.now())
                .waitingTime(LocalDateTime.now().plusMinutes(orderType.getMinimumWaitingTime()))
                .customer(customer)
                .shop(shop)
                .build();
        OrderValidator validator = new OrderValidator();

        if (validator.isValidOrder(order))
            return order;
        else
            throw new ValidationException(validator.validateOrder(order).stream()
                    .reduce("", (result, error) -> result + error));
    }
}
