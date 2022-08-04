package repository.ordersRepository;


import domain.Shop;
import domain.order.Order;
import repository.Repository;
import repository.customersRepository.CustomerRepository;
import repository.sweetsRepository.SweetRepository;

public interface OrderRepository extends Repository<Long, Order> {
    Order findOrderById(Long id);

    void generateOrders(Shop shop, SweetRepository sweetRepository,
                        CustomerRepository customerRepository);
}
