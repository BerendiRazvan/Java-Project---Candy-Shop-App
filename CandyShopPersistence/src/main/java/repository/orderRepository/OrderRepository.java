package repository.orderRepository;


import domain.Shop;
import domain.order.Order;
import repository.Repository;
import repository.customerRepository.CustomerRepository;
import repository.sweetRepository.SweetRepository;

public interface OrderRepository extends Repository<Long, Order> {
    Order findOrderById(Long id);

    void generateOrders(Shop shop, SweetRepository sweetRepository,
                        CustomerRepository customerRepository);

    int generateOrderId();
}
