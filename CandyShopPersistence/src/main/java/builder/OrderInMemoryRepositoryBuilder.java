package builder;

import domain.order.Order;
import exception.ValidationException;
import lombok.NoArgsConstructor;
import repository.orderRepository.OrderInMemoryRepository;

import java.util.List;

@NoArgsConstructor
public class OrderInMemoryRepositoryBuilder {
    public OrderInMemoryRepository build(List<Order> orderList) throws ValidationException {
        return OrderInMemoryRepository.builder()
                .orderList(orderList)
                .build();
    }
}
