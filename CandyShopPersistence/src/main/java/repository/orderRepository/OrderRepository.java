package repository.orderRepository;


import domain.Shop;
import domain.order.Order;
import exception.ValidationException;
import repository.Repository;
import repository.customerRepository.CustomerRepository;
import repository.sweetRepository.SweetRepository;

import java.util.Optional;

public interface OrderRepository extends Repository<Long, Order> {
    Optional<Order> findOrderById(Long id);

    Optional<Long> generateOrderId();
}
