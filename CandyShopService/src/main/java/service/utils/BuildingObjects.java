package service.utils;

import domain.Customer;
import domain.Shop;
import domain.location.Location;
import domain.order.Order;
import domain.order.OrderType;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import domain.sweet.SweetType;
import exception.ServiceException;
import validator.CustomerValidator;
import validator.OrderValidator;
import validator.SweetValidator;

import java.util.HashMap;
import java.util.List;

public final class BuildingObjects {
    public static Order newOrder(long id, OrderType orderType, Customer customer, Shop shop) throws ServiceException {
        Order order = Order.builder()
                .id(id)
                .orderedSweets(new HashMap<>())
                .orderType(orderType)
                .customer(customer)
                .shop(shop)
                .build();
        OrderValidator validator = new OrderValidator();

        if (validator.isValidOrder(order))
            return order;
        else
            throw new ServiceException(validator.validateOrder(order)
                    .stream()
                    .reduce("", (result, error) -> result + error));
    }

    public static Sweet newSweet(long id, List<Ingredient> ingredientList, SweetType sweetType, double price)
            throws ServiceException {
        Sweet sweet = Sweet.builder()
                .id(id)
                .ingredientsList(ingredientList)
                .sweetType(sweetType)
                .price(price)
                .build();
        SweetValidator validator = new SweetValidator();

        if (validator.isValidSweet(sweet))
            return sweet;
        else
            throw new ServiceException(validator.validateSweet(sweet)
                    .stream()
                    .reduce("", (result, error) -> result + error));
    }

    public static Customer newCustomer(long id, String firstName, String lastName, String email, String password,
                                       String phoneNumber, Location customerLocation) throws ServiceException {
        Customer customer = Customer.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .location(customerLocation)
                .build();
        CustomerValidator validator = new CustomerValidator();

        if (validator.isValidCustomer(customer))
            return customer;
        else
            throw new ServiceException(validator.validateCustomer(customer)
                    .stream()
                    .reduce("", (result, error) -> result + error));
    }
}
