package repository.ordersRepository;


import domain.order.Order;
import repository.exception.RepositoryException;

import java.util.List;

public class OrdersInMemoryRepository implements OrdersRepository {

    private List<Order> ordersMemoryList;

    public OrdersInMemoryRepository(List<Order> ordersMemoryList) {
        this.ordersMemoryList = ordersMemoryList;
    }

    @Override
    public void add(Order elem) throws RepositoryException {
        if (!ordersMemoryList.contains(elem))
            ordersMemoryList.add(elem);
        else
            throw new RepositoryException("This element already exists!");
    }

    @Override
    public void update(Long aLong, Order elem) throws RepositoryException {
        boolean exists = false;
        for (Order order : ordersMemoryList) {
            if (order.getIdOrder() == elem.getIdOrder()) {
                ordersMemoryList.set(ordersMemoryList.indexOf(order), elem);
                exists = true;
                break;
            }
        }
        if (!exists)
            throw new RepositoryException("This element does not exist!");
    }

    @Override
    public void delete(Long aLong) throws RepositoryException {
        boolean exists = false;
        for (Order order : ordersMemoryList) {
            if (order.getIdOrder() == aLong) {
                ordersMemoryList.remove(order);
                exists = true;
                break;
            }
        }
        if (!exists)
            throw new RepositoryException("This element does not exist!");
    }

    @Override
    public List<Order> findAll() {
        return ordersMemoryList;
    }

    @Override
    public Order findOneOrder(Long id) {
        for (Order order : ordersMemoryList)
            if (order.getIdOrder() == id) return order;
        return null;
    }
}
