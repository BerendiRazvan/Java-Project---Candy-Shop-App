package repository.orderRepository;

import domain.order.Order;
import repository.Repository;

import java.util.Optional;

public interface OrderRepository extends Repository<Long, Order> {
    /**
     * The method will search for Order with the given id
     *
     * @param id The id for Order you are looking for
     * @return Optional.of(Order) - if Order is found
     * Optional.empty() - else
     */
    Optional<Order> findOrderById(Long id);

    /**
     * The method that will generate an id for Order
     *
     * @return an available id
     */
    Optional<Long> generateOrderId();
}
