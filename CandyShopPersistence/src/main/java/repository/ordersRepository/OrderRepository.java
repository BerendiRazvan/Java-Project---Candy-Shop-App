package repository.ordersRepository;


import domain.order.Order;
import repository.Repository;

public interface OrderRepository extends Repository<Long, Order> {
    Order findOneOrder(Long id);
}
