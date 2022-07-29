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

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Override
    public Order createOrder(Customer customer, OrderType orderType, Shop shop) throws ServiceException {
        int id = generateOrderId();

        try {
            Order order = new Order(id, new HashMap<>(), orderType, customer, shop);
            orderRepository.add(order);
            return order;
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private int generateOrderId() {
        //the temporary method
        //it will no longer be needed after we add a db because the id will be automatically generated
        int id = 1;
        while (true) {
            boolean ok = true;
            for (var o : orderRepository.findAll())
                if (o.getIdOrder() == id) {
                    ok = false;
                    break;
                }
            if (ok) return id;
            ;
            id++;
        }
    }

    @Override
    public void addToOrder(Order order, Sweet newSweet) throws ServiceException {
        if (newSweet == null) throw new ServiceException("Invalid sweet id!");
        else {
            Order updateOrder = orderRepository.findOrderById(order.getIdOrder());
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
        return orderRepository.findOrderById(orderId).toString() +
                "TOTAL TO PAY: " + df.format(getFinalOrderPrice(orderRepository.findOrderById(orderId))) + "$" +
                "\n" + "-".repeat(100) + "\n";
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
        return orderRepository.findAll().stream().filter(order -> order.getOrderDateTime().toLocalDate().isEqual(LocalDate.now())).collect(Collectors.toList());
    }


    @Override
    public double getMoneyMadeToday() {
        return orderRepository.findAll().stream().filter(order -> order.getOrderDateTime().toLocalDate().isEqual(LocalDate.now())).mapToDouble(this::getFinalOrderPrice).sum();
    }


    @Override
    public double getProfitMadeToday() {
        return orderRepository.findAll().stream().filter(order -> order.getOrderDateTime().toLocalDate().isEqual(LocalDate.now())).mapToDouble(order -> getProfit(order.getOrderedSweets())).sum();
    }


    private double getProfit(Map<Sweet, Integer> orderedSweets) {
        double profit = 0;
        for (Sweet sweet : orderedSweets.keySet()) {
            profit += sweet.getPrice() - sweet.getExtraPrice() - sweet.getIngredientsList()
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

        Order yourOrder = orderRepository.findOrderById(id);
        if (yourOrder == null) throw new ServiceException("Invalid order number/id!");
        else {
            return yourOrder.toString() +
                    "TOTAL TO PAY: " + df.format(getFinalOrderPrice(yourOrder)) + "$" +
                    "\n" + "-".repeat(100) + "\n";
        }
    }

    @Override
    public double getFinalOrderPrice(Order order) {
        Map<Sweet, Integer> orderedSweets = order.getOrderedSweets();
        double totalToPay = 0;
        for (Sweet sweet : orderedSweets.keySet()) {
            totalToPay += orderedSweets.get(sweet) * sweet.getPrice();
        }
        return totalToPay;
    }


    private void addSweetToOrder(Order order, Sweet sweet) {
        order.getOrderedSweets().merge(sweet, 1, Integer::sum);
    }


    private void addSweetToOrder(Order order, Sweet sweet, int quantity) {
        order.getOrderedSweets().merge(sweet, quantity, Integer::sum);
    }


    private void removeSweetToOrder(Order order, Sweet sweet) {
        order.getOrderedSweets().merge(sweet, -1, Integer::sum);
        if (order.getOrderedSweets().get(sweet) == 0) order.getOrderedSweets().remove(sweet);
    }


}
