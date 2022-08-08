package service.orderService;

import domain.Customer;
import domain.Shop;
import domain.order.Order;
import domain.order.OrderType;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import service.exception.ServiceException;

import java.util.List;

public interface OrderService {

    Order createOrder(Customer customer, OrderType orderType, Shop shop) throws ServiceException;

    void addToOrder(Order order, Sweet newSweet) throws ServiceException;

    String getOrderDetails(long orderId);

    void removeOrder(long idOrder) throws ServiceException;

    List<Order> getAllOrdersInADay();

    double getMoneyMadeToday();

    double getProfitMadeToday();

    String printOrderDetails(String orderId) throws ServiceException;

    double getFinalOrderPrice(Order order);

    void addExtraIngredientToOrderedSweet(Order order, Sweet sweet, Ingredient ingredient, String amount)
            throws ServiceException;

    void updateExtraIngredientForOrderedSweet(Order order, Sweet sweet, Ingredient ingredient, String amount)
            throws ServiceException;

    void deleteExtraIngredientForOrderedSweet(Order order, Sweet sweet, Ingredient ingredient) throws ServiceException;
}
