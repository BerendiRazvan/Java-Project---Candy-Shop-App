package service;

import domain.Customer;
import domain.location.Location;
import domain.order.Order;
import domain.order.OrderTypes;
import domain.sweet.Sweet;
import java.util.List;


public interface Service {
    String getShopName();

    Iterable<Sweet> getAvailableSweets();

    Order createOrder(Customer customer, OrderTypes orderType) throws ServiceException;

    void addToOrder(Order order, String sweetId) throws ServiceException;

    String getOrderDetails(long orderId);

    void removeOrder(long idOrder) throws ServiceException;

    List<Order> getAllOrdersInADay();

    double getMoneyMadeToday();

    double getProfitMadeToday();

    Customer login(String mail, String password) throws ServiceException;

    Customer createAccount(String firstName, String lastName, String email, String password,
                           String phoneNumber, Location customerLocation) throws Exception;

    boolean findMail(String mail);

    String printOrderDetails(String orderId) throws ServiceException;
}
