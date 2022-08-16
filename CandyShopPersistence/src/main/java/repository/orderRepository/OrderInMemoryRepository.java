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
        Optional<Order> orderToUpdate = findOrderById(id);
        if (orderToUpdate.isPresent())
            orderList.set(orderList.indexOf(orderToUpdate.get()), order);
        else
            throw new RepositoryException("This element does not exist!");
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        Optional<Order> orderToRemove = findOrderById(id);
        if (orderToRemove.isPresent())
            orderList.remove(orderToRemove.get());
        else
            throw new RepositoryException("This element does not exist!");
    }

    @Override
    public List<Order> findAll() {
        return orderList;
    }

    @Override
    public Optional<Order> findOrderById(Long id) {
        return orderList.stream()
                .filter(order -> id == order.getId())
                .findFirst();
    }

    @Override
    public void generateOrders(Shop shop, SweetRepository sweetRepository,
                               CustomerRepository customerRepository) {
        List<Sweet> sweetList = sweetRepository.findAll();
        List<Customer> customerList = customerRepository.findAll();
        int noOfOrders = ORDERS_TO_GENERATE;
        while (noOfOrders != 0) {
            Optional<Long> id = generateOrderId();
            Optional<OrderType> orderType = randomOrderType();
            Optional<Customer> customer = randomCustomer(customerList);
            if (id.isPresent() && orderType.isPresent() && customer.isPresent()) {
                orderList.add(Order.builder()
                        .id(id.get())
                        .orderedSweets(randomOrder(sweetList))
                        .orderType(orderType.get())
                        .customer(customer.get())
                        .shop(shop)
                        .build());
                noOfOrders--;
            } else throw new RuntimeException("Error: generateOrderId");
        }

    }

    @Override
    public Optional<Long> generateOrderId() {
        //the temporary method
        //it will no longer be needed after we add a db because the id will be automatically generated
        long id = 1;
        while (true) {
            boolean ok = true;
            for (var o : orderList)
                if (o.getId() == id) {
                    ok = false;
                    break;
                }
            if (ok) return Optional.of(id);
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

    private static Optional<Customer> randomCustomer(List<Customer> all) {
        Random random = new Random();
        return Optional.of(all.get(random.nextInt(all.size())));
    }

    private static Optional<OrderType> randomOrderType() {
        Random random = new Random();
        if (random.nextInt(2) == 0)
            return Optional.of(OrderType.DELIVERY);
        else
            return Optional.of(OrderType.PICKUP);
    }
}
