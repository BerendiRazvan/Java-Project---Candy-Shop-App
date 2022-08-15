package repository.orderRepository;


import domain.Customer;
import domain.Shop;
import domain.order.Order;
import domain.order.OrderType;
import domain.sweet.Sweet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import repository.customerRepository.CustomerRepository;
import repository.exception.RepositoryException;
import repository.sweetRepository.SweetRepository;

import java.util.*;

@Builder
@AllArgsConstructor
public class OrderInMemoryRepository implements OrderRepository {

    private static final int ORDERS_TO_GENERATE = 7;
    private List<Order> orderList;


    @Override
    public void add(Order order) throws RepositoryException {
        if (!orderList.contains(order))
            orderList.add(order);
        else
            throw new RepositoryException("This element already exists!");
    }

    @Override
    public void update(Long id, Order order) throws RepositoryException {
        Order orderToUpdate = findOrderById(id);
        if (orderToUpdate == null)
            throw new RepositoryException("This element does not exist!");
        else
            orderList.set(orderList.indexOf(orderToUpdate), order);
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        Order orderToRemove = findOrderById(id);
        if (orderToRemove == null)
            throw new RepositoryException("This element does not exist!");
        else
            orderList.remove(orderToRemove);
    }

    @Override
    public List<Order> findAll() {
        return orderList;
    }

    @Override
    public Order findOrderById(Long id) {
        for (Order order : orderList)
            if (order.getId() == id) return order;
        return null;
    }

    @Override
    public void generateOrders(Shop shop, SweetRepository sweetRepository,
                               CustomerRepository customerRepository) {
        List<Sweet> sweetList = sweetRepository.findAll();
        List<Customer> customerList = customerRepository.findAll();
        int noOfOrders = ORDERS_TO_GENERATE;
        while (noOfOrders != 0) {
            orderList.add(Order.builder()
                    .id(generateOrderId())
                    .orderedSweets(randomOrder(sweetList))
                    .orderType(randomOrderType())
                    .customer(randomCustomer(customerList))
                    .shop(shop)
                    .build());
            noOfOrders--;
        }

    }

    @Override
    public int generateOrderId() {
        //the temporary method
        //it will no longer be needed after we add a db because the id will be automatically generated
        int id = 1;
        while (true) {
            boolean ok = true;
            for (var o : orderList)
                if (o.getId() == id) {
                    ok = false;
                    break;
                }
            if (ok) return id;
            id++;
        }
    }

    private static Map<Sweet, Integer> randomOrder(List<Sweet> all) {
        Random random = new Random();
        int randomNumberOfSweets = random.nextInt(10 - 1) + 1;

        Map<Sweet, Integer> sweetIntegerMap = new HashMap<>();

        while (randomNumberOfSweets > 0) {
            Sweet sweet = all.get(random.nextInt(all.size()));
            sweetIntegerMap.merge(sweet, 1, Integer::sum);

            randomNumberOfSweets--;
        }

        return sweetIntegerMap;
    }

    private static Customer randomCustomer(List<Customer> all) {
        Random random = new Random();
        return all.get(random.nextInt(all.size()));
    }

    private static OrderType randomOrderType() {
        Random random = new Random();
        if (random.nextInt(2) == 0)
            return OrderType.DELIVERY;
        else return OrderType.PICKUP;
    }
}
