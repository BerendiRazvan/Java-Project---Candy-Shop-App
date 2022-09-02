package service.orderService;

import domain.Customer;
import domain.Shop;
import domain.order.Order;
import domain.order.OrderType;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import exception.ValidationException;
import exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    /**
     *
     * @param customer
     * @param orderType
     * @param shop
     * @return
     * @throws ServiceException
     * @throws ValidationException
     */
    Optional<Order> createOrder(Customer customer, OrderType orderType, Shop shop) throws ServiceException, ValidationException;

    /**
     *
     * @param order
     * @param newSweet
     * @throws ServiceException
     */
    void addToOrder(Order order, Sweet newSweet) throws ServiceException;

    /**
     *
     * @param orderId
     * @return
     * @throws ServiceException
     */
    StringBuilder getOrderDetails(String orderId) throws ServiceException;

    /**
     *
     * @param idOrder
     * @throws ServiceException
     */
    void removeOrder(long idOrder) throws ServiceException;

    /**
     *
     * @return
     */
    List<Order> getAllOrdersInADay();

    /**
     *
     * @return
     */
    double getMoneyMadeToday();

    /**
     *
     * @return
     */
    double getProfitMadeToday();

    /**
     *
     * @param order
     * @return
     */
    double getFinalOrderPrice(Order order);

    /**
     *
     * @param order
     * @param sweet
     * @param ingredient
     * @param amount
     * @throws ServiceException
     * @throws ValidationException
     */
    void addExtraIngredientToOrderedSweet(Order order, Sweet sweet, Ingredient ingredient, String amount)
            throws ServiceException, ValidationException;

    /**
     *
     * @param order
     * @param sweet
     * @param ingredient
     * @param amount
     * @throws ServiceException
     */
    void updateExtraIngredientForOrderedSweet(Order order, Sweet sweet, Ingredient ingredient, String amount)
            throws ServiceException;

    /**
     *
     * @param order
     * @param sweet
     * @param ingredient
     * @throws ServiceException
     */
    void deleteExtraIngredientForOrderedSweet(Order order, Sweet sweet, Ingredient ingredient) throws ServiceException;
}
