package service.orderService;

import domain.Customer;
import domain.Shop;
import domain.order.Order;
import domain.order.OrderType;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import repository.exception.RepositoryException;
import repository.ordersRepository.OrderRepository;
import service.exception.ServiceException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Override
    public Order createOrder(Customer customer, OrderType orderType, Shop shop) throws ServiceException {
        int id = 1;
        while (true) {
            boolean ok = true;
            for (var o : orderRepository.findAll())
                if (o.getIdOrder() == id) {
                    ok = false;
                    break;
                }

            if (ok) break;
            id++;
        }

        try {
            Order order = new Order(id, new HashMap<>(), orderType, customer, shop);
            orderRepository.add(order);
            return order;
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void addToOrder(Order order, Sweet newSweet) throws ServiceException {
        if (newSweet == null)
            throw new ServiceException("Invalid sweet id!");
        else {
            Order updateOrder = orderRepository.findOneOrder(order.getIdOrder());
            addSweetToOrder(updateOrder, newSweet);
            try {
                orderRepository.update(order.getIdOrder(), updateOrder);
            } catch (RepositoryException e) {
                throw new ServiceException(e.getMessage());
            }
        }
    }


    @Override
    public String getOrderDetails(long orderId) {
        return orderRepository.findOneOrder(orderId).toString();
    }


    @Override
    public void removeOrder(long idOrder) throws ServiceException {
        try {
            orderRepository.delete(idOrder);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }


    @Override
    public List<Order> getAllOrdersInADay() {
        return orderRepository.findAll()
                .stream()
                .filter(order -> order.getOrderDateTime().toLocalDate().isEqual(LocalDate.now()))
                .collect(Collectors.toList());
    }


    @Override
    public double getMoneyMadeToday() {
        return orderRepository.findAll()
                .stream()
                .filter(order -> order.getOrderDateTime().toLocalDate().isEqual(LocalDate.now()))
                .mapToDouble(Order::getFinalOrderPrice)
                .sum();
    }


    @Override
    public double getProfitMadeToday() {
        return orderRepository.findAll()
                .stream()
                .filter(order -> order.getOrderDateTime().toLocalDate().isEqual(LocalDate.now()))
                .mapToDouble(order -> getProfit(order.getOrderedSweets()))
                .sum();
    }


    private double getProfit(Map<Sweet, Integer> orderedSweets) {
        double profit = 0;
        for (Sweet sweet : orderedSweets.keySet()) {
            profit += sweet.getPrice() - sweet.getExtraPrice() - sweet.getSweetRecipe().getIngredientsList()
                    .stream()
                    .mapToDouble(Ingredient::getPrice)
                    .sum();
        }

        return profit;
    }

    @Override
    public String printOrderDetails(String orderId) throws ServiceException {

        long id;
        try {
            id = Long.parseLong(orderId);
        } catch (Exception e) {
            throw new ServiceException("Invalid order number/id!");
        }

        Order yourOrder = orderRepository.findOneOrder(id);
        if (yourOrder == null)
            throw new ServiceException("Invalid order number/id!");
        else {
            return yourOrder.toString();
        }
    }



    private void addSweetToOrder(Order order, Sweet sweet) {
        order.getOrderedSweets().merge(sweet, 1, Integer::sum);
    }



    private void addSweetToOrder(Order order, Sweet sweet, int quantity) {
        order.getOrderedSweets().merge(sweet, quantity, Integer::sum);
    }


    private void removeSweetToOrder(Order order, Sweet sweet) {
        order.getOrderedSweets().merge(sweet, -1, Integer::sum);
        if (order.getOrderedSweets().get(sweet) == 0)
            order.getOrderedSweets().remove(sweet);
    }


}
