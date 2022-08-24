package service.orderService;

import domain.Customer;
import domain.Shop;
import domain.order.Order;
import domain.order.OrderType;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import exception.BuildException;
import exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Optional<Order> createOrder(Customer customer, OrderType orderType, Shop shop) throws ServiceException, BuildException;

    void addToOrder(Order order, Sweet newSweet) throws ServiceException;

    StringBuilder getOrderDetails(String orderId) throws ServiceException;

    void removeOrder(long idOrder) throws ServiceException;

    List<Order> getAllOrdersInADay();

    double getMoneyMadeToday();

    double getProfitMadeToday();

    double getFinalOrderPrice(Order order);

    void addExtraIngredientToOrderedSweet(Order order, Sweet sweet, Ingredient ingredient, String amount)
            throws ServiceException, BuildException;

    void updateExtraIngredientForOrderedSweet(Order order, Sweet sweet, Ingredient ingredient, String amount)
            throws ServiceException;

    void deleteExtraIngredientForOrderedSweet(Order order, Sweet sweet, Ingredient ingredient) throws ServiceException;
}
