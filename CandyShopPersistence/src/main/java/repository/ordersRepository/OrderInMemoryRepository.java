package repository.ordersRepository;


import domain.Customer;
import domain.Shop;
import domain.order.Order;
import domain.order.OrderType;
import domain.sweet.Sweet;
import repository.customersRepository.CustomerRepository;
import repository.exception.RepositoryException;
import repository.sweetsRepository.SweetRepository;

import java.util.*;

public class OrderInMemoryRepository implements OrderRepository {

    private List<Order> orderList;

    public OrderInMemoryRepository(List<Order> orderList) {
        this.orderList = orderList;
    }

    @Override
    public void add(Order order) throws RepositoryException {
        if (!orderList.contains(order))
            orderList.add(order);
        else
            throw new RepositoryException("This element already exists!");
    }

    @Override
    public void update(Long id, Order order) throws RepositoryException {
        boolean exists = false;
        for (Order o : orderList) {
            if (o.getIdOrder() == order.getIdOrder()) {
                orderList.set(orderList.indexOf(o), order);
                exists = true;
                break;
            }
        }
        if (!exists)
            throw new RepositoryException("This element does not exist!");
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        boolean exists = false;
        for (Order order : orderList) {
            if (order.getIdOrder() == id) {
                orderList.remove(order);
                exists = true;
                break;
            }
        }
        if (!exists)
            throw new RepositoryException("This element does not exist!");
    }

    @Override
    public List<Order> findAll() {
        return orderList;
    }

    @Override
    public Order findOrderById(Long id) {
        for (Order order : orderList)
            if (order.getIdOrder() == id) return order;
        return null;
    }

    public static List<Order> generateOrders(Shop shop, SweetRepository sweetRepository,
                                             CustomerRepository customerRepository) {
        List<Order> orderList = new ArrayList<>();
        orderList.add(new Order(1,
                randomOrder(sweetRepository.findAll()), OrderType.PICKUP,
                randomCustomer(customerRepository.findAll()), shop));
        orderList.add(new Order(2,
                randomOrder(sweetRepository.findAll()), OrderType.PICKUP,
                randomCustomer(customerRepository.findAll()), shop));
        orderList.add(new Order(3,
                randomOrder(sweetRepository.findAll()), OrderType.DELIVERY,
                randomCustomer(customerRepository.findAll()), shop));
        orderList.add(new Order(4,
                randomOrder(sweetRepository.findAll()), OrderType.DELIVERY,
                randomCustomer(customerRepository.findAll()), shop));
        orderList.add(new Order(5,
                randomOrder(sweetRepository.findAll()), OrderType.PICKUP,
                randomCustomer(customerRepository.findAll()), shop));
        orderList.add(new Order(6,
                randomOrder(sweetRepository.findAll()), OrderType.DELIVERY,
                randomCustomer(customerRepository.findAll()), shop));
        orderList.add(new Order(7,
                randomOrder(sweetRepository.findAll()), OrderType.PICKUP,
                randomCustomer(customerRepository.findAll()), shop));
        return orderList;

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
}
