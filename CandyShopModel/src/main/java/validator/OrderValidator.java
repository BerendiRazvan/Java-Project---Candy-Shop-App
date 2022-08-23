package validator;

import domain.Customer;
import domain.Shop;
import domain.order.Order;
import domain.order.OrderType;
import domain.sweet.Sweet;

import java.time.LocalDateTime;
import java.util.Map;

public class OrderValidator {
    private static final int MINIMUM_AMOUNT_OF_ORDERED_SWEETS_VALUE = 1;

    public String validateOrderOrderedSweet(Map<Sweet, Integer> orderedSweets) {
        for (Sweet sweet : orderedSweets.keySet()) {
            SweetValidator sweetValidator = new SweetValidator();
            if (!sweetValidator.isValidSweet(sweet))
                return "Invalid ordered sweets!\n";
            if (orderedSweets.get(sweet) < MINIMUM_AMOUNT_OF_ORDERED_SWEETS_VALUE)
                return "Invalid amount of ordered sweets!\n";
        }
        return "";
    }

    public String validateOrderCustomer(Customer customer) {
        CustomerValidator validator = new CustomerValidator();
        if (!validator.isValidCustomer(customer))
            return "Invalid customer!\n";
        return "";
    }

    public String validateOrderShop(Shop shop) {
        ShopValidator validator = new ShopValidator();
        if (!validator.isValidShop(shop))
            return "Invalid shop!\n";
        return "";
    }

    public String validateOrderDateTime(LocalDateTime orderDateTime) {
        if (orderDateTime.isBefore(LocalDateTime.of(2020, 1, 1, 0, 0)))
            return "Invalid order date time!\n";
        return "";
    }

    public String validateOrderType(OrderType orderType) {
        if (!(orderType.equals(OrderType.DELIVERY) || orderType.equals(OrderType.PICKUP)))
            return "Invalid order type!\n";
        return "";
    }

    public boolean isValidOrder(Order order) {
        return validateOrder(order).equals("");
    }

    public String validateOrder(Order order) {
        if (order == null) return "Order can not be null!";
        return validateOrderOrderedSweet(order.getOrderedSweets()) +
                validateOrderCustomer(order.getCustomer()) +
                validateOrderShop(order.getShop()) +
                validateOrderDateTime(order.getOrderDateTime()) +
                validateOrderType(order.getOrderType());
    }
}
