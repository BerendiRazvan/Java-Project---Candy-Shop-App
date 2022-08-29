package repository.orderRepository;


import builder.OrderBuilder;
import domain.Customer;
import domain.Shop;
import domain.order.Order;
import domain.order.OrderType;
import domain.sweet.Sweet;
import exception.ValidationException;
import exception.RepositoryException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.customerRepository.CustomerRepository;
import repository.sweetRepository.SweetRepository;

import java.util.*;

@AllArgsConstructor
public class OrderInMemoryRepository implements OrderRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderInMemoryRepository.class);

    private static final int ORDERS_TO_GENERATE = 7;
    private List<Order> orderList;

    public OrderInMemoryRepository(Shop shop, SweetRepository sweetRepository, CustomerRepository customerRepository) throws ValidationException {
        this(new ArrayList<>());
        generateOrders(shop, sweetRepository, customerRepository);
    }


    @Override
    public void add(Order order) throws RepositoryException {
        LOGGER.info("Add order - started");
        if (!orderList.contains(order)) {
            orderList.add(order);
            LOGGER.info("Add order - finished");
        } else {
            LOGGER.warn("Add order - exception occurred -> {}", "This element already exists!");
            throw new RepositoryException("This element already exists!");
        }
    }

    @Override
    public void update(Long id, Order order) throws RepositoryException {
        LOGGER.info("Update order with id = {} - started", id);
        Optional<Order> orderToUpdate = findOrderById(id);
        if (orderToUpdate.isPresent()) {
            orderList.set(orderList.indexOf(orderToUpdate.get()), order);
            LOGGER.info("Update order with id = {} - finished", id);
        } else {
            LOGGER.warn("Update order with id = {} to - exception occurred -> {}", id,
                    "This element does not exist!");
            throw new RepositoryException("This element does not exist!");
        }
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        LOGGER.info("Delete order with id = {} - started", id);
        Optional<Order> orderToRemove = findOrderById(id);
        if (orderToRemove.isPresent()) {
            orderList.remove(orderToRemove.get());
            LOGGER.info("Delete order with id = {} - finished", id);
        } else {
            LOGGER.warn("Delete order with id = {} - exception occurred -> {}", id, "This element does not exist!");
            throw new RepositoryException("This element does not exist!");
        }
    }

    @Override
    public List<Order> findAll() {
        LOGGER.info("FindAll orders - called");
        return orderList;
    }

    @Override
    public Optional<Order> findOrderById(Long id) {
        LOGGER.info("FindOrderById for order with id = {} - called", id);
        return orderList.stream()
                .filter(order -> id == order.getId())
                .findFirst();
    }

    @Override
    public Optional<Long> generateOrderId() {
        //the temporary method
        //it will no longer be needed after we add a db because the id will be automatically generated

        LOGGER.info("GenerateOrderId - started");

        long id = 1;
        while (true) {
            boolean ok = true;
            for (var o : orderList)
                if (o.getId() == id) {
                    ok = false;
                    break;
                }
            if (ok) {
                LOGGER.info("GenerateOrderId - finished");
                return Optional.of(id);
            }
            id++;
        }
    }

    private void generateOrders(Shop shop, SweetRepository sweetRepository,
                                CustomerRepository customerRepository) throws ValidationException {
        LOGGER.info("GenerateOrders - started");
        OrderBuilder orderBuilder = new OrderBuilder();
        List<Sweet> sweetList = sweetRepository.findAll();
        List<Customer> customerList = customerRepository.findAll();
        int noOfOrders = ORDERS_TO_GENERATE;
        while (noOfOrders != 0) {
            Optional<Long> id = generateOrderId();
            Optional<OrderType> orderType = randomOrderType();
            Optional<Customer> customer = randomCustomer(customerList);
            if (id.isPresent() && orderType.isPresent() && customer.isPresent()) {
                orderList.add(orderBuilder.build(id.get(), randomOrder(sweetList), orderType.get(), customer.get(), shop));
                noOfOrders--;
            } else {
                LOGGER.warn("Error: generateOrderId");
                throw new RuntimeException("Error: generateOrderId");
            }
        }
        LOGGER.info("GenerateOrders - finished");
    }

    private static Map<Sweet, Integer> randomOrder(List<Sweet> all) {
        LOGGER.info("RandomOrder - called");
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
        LOGGER.info("RandomCustomer - called");
        Random random = new Random();
        return Optional.of(all.get(random.nextInt(all.size())));
    }

    private static Optional<OrderType> randomOrderType() {
        LOGGER.info("RandomOrderType - called");
        Random random = new Random();
        if (random.nextInt(2) == 0)
            return Optional.of(OrderType.DELIVERY);
        else
            return Optional.of(OrderType.PICKUP);
    }
}
