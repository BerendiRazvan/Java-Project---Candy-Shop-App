package service.orderService;

import builder.OrderBuilder;
import builder.SweetBuilder;
import domain.Customer;
import domain.Shop;
import domain.order.Order;
import domain.order.OrderType;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import exception.ValidationException;
import exception.RepositoryException;
import exception.ServiceException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.ingredientRepository.IngredientRepository;
import repository.orderRepository.OrderRepository;
import repository.sweetRepository.SweetRepository;


import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static service.utils.Converter.convertStringToInt;
import static service.utils.Converter.convertStringToLong;

@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private OrderRepository orderRepository;
    private SweetRepository sweetRepository;
    private IngredientRepository ingredientRepository;


    @Override
    public Optional<Order> createOrder(Customer customer, OrderType orderType, Shop shop) throws ServiceException,
            ValidationException {
        LOGGER.info("CreateOrder order with customer, order type = {} and shop - started", orderType);
        Optional<Long> id = orderRepository.generateOrderId();

        if (id.isPresent()) {
            try {
                OrderBuilder orderBuilder = new OrderBuilder();
                Order order = orderBuilder.build(id.get(), new HashMap<>(), orderType, customer, shop);
                orderRepository.add(order);
                LOGGER.info("CreateOrder order with customer, order type = {} and shop - finished", orderType);
                return Optional.of(order);
            } catch (RepositoryException e) {
                LOGGER.warn("CreateOrder order with customer, order type = {} and shop - exception occurred -> {}",
                        orderType, e.getMessage());
                throw new ServiceException(e.getMessage());
            }
        } else {
            LOGGER.error("CreateOrder order with customer, order type = {} sand shop - exception occurred -> {}",
                    orderType, "Error: generateOrderId");
            throw new ServiceException("Error: generateOrderId");
        }
    }


    @Override
    public void addToOrder(Order order, Sweet newSweet) throws ServiceException {
        LOGGER.info("AddToOrder for order and sweet - started");
        if (newSweet == null) throw new ServiceException("Invalid sweet id!");
        else {
            Optional<Order> updateOrder = orderRepository.findOrderById(order.getId());
            if (updateOrder.isPresent()) {
                try {
                    updateShopStockAfterAddToOrder(newSweet.getIngredientsList());
                    updateShopStockAfterAddToOrder(newSweet.getExtraIngredients());
                    addSweetToOrder(updateOrder.get(), newSweet);
                    orderRepository.update(order.getId(), updateOrder.get());
                } catch (RepositoryException e) {
                    LOGGER.warn("AddToOrder for order and sweet - exception occurred -> {}", e.getMessage());
                    throw new ServiceException(e.getMessage());
                }
            } else {
                LOGGER.warn("AddToOrder for order and sweet - exception occurred -> {}", "Invalid sweet id!");
                throw new ServiceException("Invalid sweet id!");
            }
        }
        LOGGER.info("AddToOrder for order and sweet - finished");
    }


    @Override
    public StringBuilder getOrderDetails(String orderId) throws ServiceException {
        LOGGER.info("GetOrderDetails for order with id = {} - started", orderId);
        long id = convertStringToLong(orderId);

        Optional<Order> orderById = orderRepository.findOrderById(id);
        if (orderById.isPresent()) {
            LOGGER.info("GetOrderDetails for order with id = {} - finished", orderId);
            return new StringBuilder(orderById.get() +
                    "TOTAL TO PAY: " + df.format(getFinalOrderPrice(orderById.get())) + "$" +
                    "\n" + "-".repeat(100) + "\n");
        } else {
            LOGGER.warn("GetOrderDetails for order with id = {} - exception occurred -> {}", orderId,
                    "Invalid order number/id!");
            throw new ServiceException("Invalid order number/id!");
        }
    }

    @Override
    public void removeOrder(long idOrder) throws ServiceException {
        LOGGER.info("RemoveOrder for order with id = {} - started", idOrder);
        try {
            orderRepository.delete(idOrder);
        } catch (RepositoryException e) {
            LOGGER.warn("RemoveOrder for order with id = {} - exception occurred -> {}", idOrder, e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        LOGGER.info("RemoveOrder for order with id = {} - finished", idOrder);
    }


    @Override
    public List<Order> getAllOrdersInADay() {
        LOGGER.info("GetAllOrdersInADay - called");
        return orderRepository.findAll()
                .stream()
                .filter(order -> order.getOrderDateTime()
                        .toLocalDate()
                        .isEqual(LocalDate.now()))
                .collect(Collectors.toList());
    }


    @Override
    public double getMoneyMadeToday() {
        LOGGER.info("GetMoneyMadeToday - called");
        return orderRepository.findAll()
                .stream()
                .filter(order -> order.getOrderDateTime()
                        .toLocalDate()
                        .isEqual(LocalDate.now()))
                .mapToDouble(this::getFinalOrderPrice).sum();
    }


    @Override
    public double getProfitMadeToday() {
        LOGGER.info("GetProfitMadeToday - called");
        return orderRepository.findAll()
                .stream()
                .filter(order -> order.getOrderDateTime()
                        .toLocalDate().isEqual(LocalDate.now()))
                .mapToDouble(order -> getProfit(order.getOrderedSweets()))
                .sum();
    }

    @Override
    public double getFinalOrderPrice(Order order) {
        LOGGER.info("GetFinalOrderPrice - started");
        Map<Sweet, Integer> orderedSweets = order.getOrderedSweets();
        double totalToPay = 0;
        for (Sweet sweet : orderedSweets.keySet()) {
            totalToPay += orderedSweets.get(sweet) * sweet.getTotalPrice();
        }
        LOGGER.info("GetFinalOrderPrice - finished");
        return totalToPay;
    }

    @Override
    public void addExtraIngredientToOrderedSweet(Order order, Sweet sweet, Ingredient ingredient, String amount)
            throws ServiceException, ValidationException {
        LOGGER.info("AddExtraIngredientToOrderedSweet for order, sweet, ingredient and amount = {} - started", amount);
        int ingredientAmount = convertStringToInt(amount);

        extraIngredientValidationWithAmount(order, sweet, ingredient, ingredientAmount);

        Optional<Long> id = sweetRepository.generateSweetId();
        if (id.isPresent()) {
            SweetBuilder sweetBuilder = new SweetBuilder();
            Sweet customSweet = sweetBuilder.build(id.get(), sweet.getIngredientsList(), sweet.getSweetType(), sweet.getTotalPrice());
            customSweet.setExtraIngredients(new ArrayList<>(sweet.getExtraIngredients()));

            updateExtraIngredientFromSweet(customSweet, ingredient, ingredientAmount);

            removeSweetFromOrder(order, sweet);
            addSweetToOrder(order, customSweet);

            try {
                sweetRepository.add(customSweet);
            } catch (RepositoryException e) {
                LOGGER.warn("AddExtraIngredientToOrderedSweet for order, sweet, ingredient and amount = {} - exception " +
                        "occurred -> {}", amount, e.getMessage());
                throw new ServiceException(e.getMessage());
            }
        } else {
            LOGGER.error("AddExtraIngredientToOrderedSweet for order, sweet, ingredient and amount = {} - exception " +
                    "occurred -> {}", amount, "Error: generateSweetId");
            throw new ServiceException("Error: generateSweetId");
        }
        LOGGER.info("AddExtraIngredientToOrderedSweet for order, sweet, ingredient and amount = {} - finished", amount);
    }

    @Override
    public void updateExtraIngredientForOrderedSweet(Order order, Sweet sweet, Ingredient ingredient, String amount)
            throws ServiceException {
        LOGGER.info("UpdateExtraIngredientForOrderedSweet for order, sweet, ingredient and amount = {} - started", amount);
        int ingredientAmount = convertStringToInt(amount);

        extraIngredientValidationWithAmount(order, sweet, ingredient, ingredientAmount);
        updateExtraIngredientFromSweet(sweet, ingredient, ingredientAmount);

        try {
            sweetRepository.update(sweet.getId(), sweet);
        } catch (RepositoryException e) {
            LOGGER.warn("UpdateExtraIngredientForOrderedSweet for order, sweet, ingredient and amount = {} - exception " +
                    "occurred -> {}", amount, e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        LOGGER.info("UpdateExtraIngredientForOrderedSweet for order, sweet, ingredient and amount = {} - finished", amount);
    }

    @Override
    public void deleteExtraIngredientForOrderedSweet(Order order, Sweet sweet, Ingredient ingredient)
            throws ServiceException {
        LOGGER.info("DeleteExtraIngredientForOrderedSweet for order, sweet and ingredient - started");

        extraIngredientValidation(order, sweet, ingredient);

        updateExtraIngredientFromSweet(sweet, ingredient, 0);

        try {
            sweetRepository.update(sweet.getId(), sweet);
        } catch (RepositoryException e) {
            LOGGER.warn("DeleteExtraIngredientForOrderedSweet for order, sweet and ingredient - exception occurred -> {}",
                    e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        LOGGER.info("DeleteExtraIngredientForOrderedSweet for order, sweet and ingredient - finished");
    }

    private void updateExtraIngredientFromSweet(Sweet sweet, Ingredient ingredient, int ingredientAmount)
            throws ServiceException {
        LOGGER.info("UpdateExtraIngredientFromSweet for sweet, ingredient and amount = {} - started", ingredientAmount);
        int actualAmount = getExtraIngredientAmountFromSweet(sweet, ingredient);
        while (actualAmount - ingredientAmount != 0) {
            if (actualAmount < ingredientAmount) {
                sweet.getExtraIngredients().add(ingredient);
                decreasesIngredientsStock(ingredient);
                actualAmount++;
            } else {
                sweet.getExtraIngredients().remove(ingredient);
                increaseIngredientsStock(ingredient);
                actualAmount--;
            }
        }
        LOGGER.info("UpdateExtraIngredientFromSweet for sweet, ingredient and amount = {} - finished", ingredientAmount);
    }

    private void extraIngredientValidationWithAmount(Order order, Sweet sweet, Ingredient ingredient, int amount)
            throws ServiceException {
        LOGGER.info("ExtraIngredientValidationWithAmount for order, sweet, ingredient and amount = {} - started", amount);
        extraIngredientValidation(order, sweet, ingredient);
        if (amount > ingredient.getAmount())
            throw new ServiceException("Invalid amount!");
        if (amount < 1)
            throw new ServiceException("Invalid amount!");
        LOGGER.info("ExtraIngredientValidationWithAmount for order, sweet, ingredient and amount = {} - finished", amount);
    }

    private void extraIngredientValidation(Order order, Sweet sweet, Ingredient ingredient)
            throws ServiceException {
        LOGGER.info("ExtraIngredientValidation for order, sweet and ingredient - started");
        if (sweet == null) throw new ServiceException("Invalid sweet!");
        if (ingredient == null) throw new ServiceException("Invalid ingredient!");
        if (order.getOrderedSweets().get(sweet) == null)
            throw new ServiceException("The sweet was not ordered!");
        LOGGER.info("ExtraIngredientValidation for order, sweet and ingredient - finished");
    }


    private int getExtraIngredientAmountFromSweet(Sweet sweet, Ingredient ingredient) {
        LOGGER.info("GetExtraIngredientAmountFromSweet for sweet and ingredient - called");
        return (int) sweet.getExtraIngredients()
                .stream()
                .filter(i -> i == ingredient)
                .count();
    }

    private void updateShopStockAfterAddToOrder(List<Ingredient> ingredientList) throws ServiceException {
        LOGGER.info("UpdateShopStockAfterAddToOrder for ingredientList - started");
        for (Ingredient ingredient : ingredientList) {
            try {
                decreasesIngredientsStock(ingredient);
            } catch (ServiceException e) {
                LOGGER.warn("UpdateShopStockAfterAddToOrder for ingredientList - exception occurred -> {}", e.getMessage());
                throw new ServiceException(e.getMessage());
            }
        }
        LOGGER.info("UpdateShopStockAfterAddToOrder for ingredientList - finished");
    }

    private void decreasesIngredientsStock(Ingredient ingredient) throws ServiceException {
        LOGGER.info("DecreasesIngredientsStock for ingredient - started");
        int updatedAmount = ingredient.getAmount() - 1;
        if (updatedAmount < 0) {
            LOGGER.warn("DecreasesIngredientsStock for ingredient - exception occurred -> {}",
                    "Invalid amount for this ingredient");
            throw new ServiceException("Invalid amount for this ingredient");
        }
        ingredient.setAmount(updatedAmount);
        try {
            ingredientRepository.update(ingredient.getId(), ingredient);
        } catch (RepositoryException e) {
            LOGGER.warn("DecreasesIngredientsStock for ingredient - exception occurred -> {}", e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        LOGGER.info("DecreasesIngredientsStock for ingredient - finished");
    }

    private void increaseIngredientsStock(Ingredient ingredient) throws ServiceException {
        LOGGER.info("IncreaseIngredientsStock for ingredient - started");
        int updatedAmount = ingredient.getAmount() + 1;
        ingredient.setAmount(updatedAmount);
        try {
            ingredientRepository.update(ingredient.getId(), ingredient);
        } catch (RepositoryException e) {
            LOGGER.warn("IncreaseIngredientsStock for ingredient - exception occurred -> {}", e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        LOGGER.info("IncreaseIngredientsStock for ingredient - finished");
    }

    private void addSweetToOrder(Order order, Sweet sweet) {
        LOGGER.info("AddSweetToOrder for order and sweet - started");
        order.getOrderedSweets().merge(sweet, 1, Integer::sum);
        LOGGER.info("AddSweetToOrder for order and sweet - finished");
    }


    private void addSweetToOrder(Order order, Sweet sweet, int quantity) {
        LOGGER.info("AddSweetToOrder for order, sweet and quantity = {} - started", quantity);
        order.getOrderedSweets().merge(sweet, quantity, Integer::sum);
        LOGGER.info("AddSweetToOrder for order, sweet and quantity = {} - finished", quantity);
    }


    private void removeSweetFromOrder(Order order, Sweet sweet) {
        LOGGER.info("RemoveSweetFromOrder for order and sweet - started");
        order.getOrderedSweets().merge(sweet, -1, Integer::sum);
        if (order.getOrderedSweets().get(sweet) == 0)
            order.getOrderedSweets().remove(sweet);
        LOGGER.info("RemoveSweetFromOrder for order and sweet - finished");
    }

    private double getProfit(Map<Sweet, Integer> orderedSweets) {
        LOGGER.info("GetProfit for orderedSweets - started");
        double profit = 0;
        for (Sweet sweet : orderedSweets.keySet()) {
            profit += sweet.getTotalPrice() - sweet.getExtraPrice() - priceForIngredients(sweet.getIngredientsList());
        }
        LOGGER.info("GetProfit for orderedSweets - finished");
        return profit;
    }

    private double priceForIngredients(List<Ingredient> ingredientsList) {
        LOGGER.info("PriceForIngredients for ingredientsList - called");
        return ingredientsList
                .stream()
                .mapToDouble(Ingredient::getPrice)
                .sum();
    }

}
