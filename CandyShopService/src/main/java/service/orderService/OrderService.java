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
     * The method creates a new order without sweets ordered
     *
     * @param customer  The customer
     * @param orderType The order type
     * @param shop      The shop
     * @return The new order
     * @throws ServiceException    If the order creation fails
     * @throws ValidationException If the data entered for the new order are invalid
     */
    Optional<Order> createOrder(Customer customer, OrderType orderType, Shop shop) throws ServiceException, ValidationException;

    /**
     * The method will add a sweet to the order
     *
     * @param order    The order where the sweet is added
     * @param newSweet The sweet added
     * @throws ServiceException If the data entered for order or sweet are invalid
     */
    void addToOrder(Order order, Sweet newSweet) throws ServiceException;

    /**
     * The method will display the information about the order in the form of a tax receipt
     *
     * @param orderId The id for Order you are looking for
     * @return The information about the order
     * @throws ServiceException If the Order does not exist
     */
    StringBuilder getOrderDetails(String orderId) throws ServiceException;

    /**
     * The method will delete the order
     *
     * @param idOrder The id for the order
     * @throws ServiceException If the Order does not exist
     */
    void removeOrder(long idOrder) throws ServiceException;

    /**
     * The method will return all the orders made today
     *
     * @return List of all orders
     */
    List<Order> getAllOrdersInADay();

    /**
     * The method will return the money for the orders made today
     *
     * @return The amount of money accumulated
     */
    double getMoneyMadeToday();

    /**
     * The method will return the profit for the orders made today
     *
     * @return The amount of money accumulated as profit
     */
    double getProfitMadeToday();

    /**
     * The method will calculate the final price of the order
     *
     * @param order The order
     * @return The final price
     */
    double getFinalOrderPrice(Order order);

    /**
     * The method will add a quantity of an extra ingredient to a sweet on the order
     *
     * @param order      The order that will be modified
     * @param sweet      The sweet that will be modified
     * @param ingredient The ingredient that will be added
     * @param amount     The amount of that ingredient
     * @throws ServiceException    If the order cannot be changed
     * @throws ValidationException If there are invalid data entered
     */
    void addExtraIngredientToOrderedSweet(Order order, Sweet sweet, Ingredient ingredient, String amount)
            throws ServiceException, ValidationException;

    /**
     * The method will update the quantity of an extra ingredient from a sweet
     *
     * @param order      The order that will be modified
     * @param sweet      The sweet that will be modified
     * @param ingredient The ingredient that will be updated
     * @param amount     The amount of that ingredient
     * @throws ServiceException If the order cannot be changed
     */
    void updateExtraIngredientForOrderedSweet(Order order, Sweet sweet, Ingredient ingredient, String amount)
            throws ServiceException;

    /**
     * The method will delete an extra ingredient from a sweet
     *
     * @param order      The order that will be modified
     * @param sweet      The sweet that will be modified
     * @param ingredient The ingredient that will be deleted
     * @throws ServiceException If the order cannot be changed
     */
    void deleteExtraIngredientForOrderedSweet(Order order, Sweet sweet, Ingredient ingredient) throws ServiceException;
}
