package builder;

import domain.order.Order;
import exception.BuildException;
import lombok.NoArgsConstructor;
import repository.orderRepository.OrderInMemoryRepository;

import java.util.List;

@NoArgsConstructor
public class OrderInMemoryRepositoryBuilder {
    public OrderInMemoryRepository build(List<Order> orderList) throws BuildException {
        return OrderInMemoryRepository.builder()
                .orderList(orderList)
                .build();
    }
}
