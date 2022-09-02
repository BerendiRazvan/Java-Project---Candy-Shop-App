package repository.orderRepository;

import domain.order.Order;
import repository.Repository;

import java.util.Optional;

public interface OrderRepository extends Repository<Long, Order> {
    Optional<Order> findOrderById(Long id);
    Optional<Long> generateOrderId();
}
