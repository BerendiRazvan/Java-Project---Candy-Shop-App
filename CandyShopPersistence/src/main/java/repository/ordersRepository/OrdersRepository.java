package repository.ordersRepository;


import domain.order.Order;
import repository.Repository;

public interface OrdersRepository extends Repository<Long, Order> {
    Order findOneOrder(Long id);
}
