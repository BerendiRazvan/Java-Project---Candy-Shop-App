package repository.ordersRepository;


import domain.order.Order;
import repository.exception.RepositoryException;

import java.util.List;

public class OrderInMemoryRepository implements OrderRepository {

    private List<Order> orderList;

    public OrderInMemoryRepository(List<Order> orderList) {
        this.orderList = orderList;
    }

    @Override
    public void add(Order elem) throws RepositoryException {
        if (!orderList.contains(elem))
            orderList.add(elem);
        else
            throw new RepositoryException("This element already exists!");
    }

    @Override
    public void update(Long aLong, Order elem) throws RepositoryException {
        boolean exists = false;
        for (Order order : orderList) {
            if (order.getIdOrder() == elem.getIdOrder()) {
                orderList.set(orderList.indexOf(order), elem);
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
        for (Order order : orderList) {
            if (order.getIdOrder() == aLong) {
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
    public Order findOneOrder(Long id) {
        for (Order order : orderList)
            if (order.getIdOrder() == id) return order;
        return null;
    }
}
