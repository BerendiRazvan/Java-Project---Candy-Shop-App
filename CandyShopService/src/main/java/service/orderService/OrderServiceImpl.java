package service.orderService;

import builder.OrderBuilder;
import builder.SweetBuilder;
import domain.Customer;
import domain.Shop;
import domain.order.Order;
import domain.order.OrderType;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import exception.BuildException;
import exception.RepositoryException;
import exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import repository.ingredientRepository.IngredientRepository;
import repository.orderRepository.OrderRepository;
import repository.sweetRepository.SweetRepository;


import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static service.utils.Converter.convertStringToInt;
import static service.utils.Converter.convertStringToLong;

@Builder
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private OrderRepository orderRepository;
    private SweetRepository sweetRepository;
    private IngredientRepository ingredientRepository;


    @Override
    public Optional<Order> createOrder(Customer customer, OrderType orderType, Shop shop) throws ServiceException,
            BuildException {
        Optional<Long> id = orderRepository.generateOrderId();

        if (id.isPresent()) {
            try {
                OrderBuilder orderBuilder = new OrderBuilder();
                Order order = orderBuilder.build(id.get(), new HashMap<>(), orderType, customer, shop);
                orderRepository.add(order);
                return Optional.of(order);
            } catch (RepositoryException e) {
                throw new ServiceException(e.getMessage());
            }
        } else throw new RuntimeException("Error: generateOrderId");
    }


    @Override
    public void addToOrder(Order order, Sweet newSweet) throws ServiceException {
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
                    throw new ServiceException(e.getMessage());
                }
            } else throw new ServiceException("Invalid sweet id!");
        }
    }


    @Override
    public StringBuilder getOrderDetails(String orderId) throws ServiceException {
        long id = convertStringToLong(orderId);

        Optional<Order> orderById = orderRepository.findOrderById(id);
        if (orderById.isPresent()) {
            return new StringBuilder(orderById.get() +
                    "TOTAL TO PAY: " + df.format(getFinalOrderPrice(orderById.get())) + "$" +
                    "\n" + "-".repeat(100) + "\n");
        } else throw new ServiceException("Invalid order number/id!");
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
                .filter(order -> order.getOrderDateTime()
                        .toLocalDate()
                        .isEqual(LocalDate.now()))
                .collect(Collectors.toList());
    }


    @Override
    public double getMoneyMadeToday() {
        return orderRepository.findAll()
                .stream()
                .filter(order -> order.getOrderDateTime()
                        .toLocalDate()
                        .isEqual(LocalDate.now()))
                .mapToDouble(this::getFinalOrderPrice).sum();
    }


    @Override
    public double getProfitMadeToday() {
        return orderRepository.findAll()
                .stream()
                .filter(order -> order.getOrderDateTime()
                        .toLocalDate().isEqual(LocalDate.now()))
                .mapToDouble(order -> getProfit(order.getOrderedSweets()))
                .sum();
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

    @Override
    public void addExtraIngredientToOrderedSweet(Order order, Sweet sweet, Ingredient ingredient, String amount)
            throws ServiceException, BuildException {
        int ingredientAmount = convertStringToInt(amount);

        extraIngredientValidationWithAmount(order, sweet, ingredient, ingredientAmount);

        Optional<Long> id = sweetRepository.generateSweetId();
        if (id.isPresent()) {
            SweetBuilder sweetBuilder = new SweetBuilder();
            Sweet customSweet = sweetBuilder.build(id.get(), sweet.getIngredientsList(), sweet.getSweetType(), sweet.getPrice());
            customSweet.setExtraIngredients(new ArrayList<>(sweet.getExtraIngredients()));

            updateExtraIngredientFromSweet(customSweet, ingredient, ingredientAmount);

            removeSweetFromOrder(order, sweet);
            addSweetToOrder(order, customSweet);

            try {
                sweetRepository.add(customSweet);
            } catch (RepositoryException e) {
                throw new ServiceException(e.getMessage());
            }
        } else throw new RuntimeException("Error: generateSweetId");
    }

    @Override
    public void updateExtraIngredientForOrderedSweet(Order order, Sweet sweet, Ingredient ingredient, String amount)
            throws ServiceException {
        int ingredientAmount = convertStringToInt(amount);

        extraIngredientValidationWithAmount(order, sweet, ingredient, ingredientAmount);
        updateExtraIngredientFromSweet(sweet, ingredient, ingredientAmount);

        try {
            sweetRepository.update(sweet.getId(), sweet);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteExtraIngredientForOrderedSweet(Order order, Sweet sweet, Ingredient ingredient)
            throws ServiceException {
        extraIngredientValidation(order, sweet, ingredient);

        updateExtraIngredientFromSweet(sweet, ingredient, 0);

        try {
            sweetRepository.update(sweet.getId(), sweet);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private void updateExtraIngredientFromSweet(Sweet sweet, Ingredient ingredient, int ingredientAmount)
            throws ServiceException {
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
    }

    private void extraIngredientValidationWithAmount(Order order, Sweet sweet, Ingredient ingredient, int amount)
            throws ServiceException {
        extraIngredientValidation(order, sweet, ingredient);
        if (amount > ingredient.getAmount())
            throw new ServiceException("Invalid amount!");
        if (amount < 1)
            throw new ServiceException("Invalid amount!");
    }

    private void extraIngredientValidation(Order order, Sweet sweet, Ingredient ingredient)
            throws ServiceException {
        if (sweet == null) throw new ServiceException("Invalid sweet!");
        if (ingredient == null) throw new ServiceException("Invalid ingredient!");
        if (order.getOrderedSweets().get(sweet) == null)
            throw new ServiceException("The sweet was not ordered!");
    }


    private int getExtraIngredientAmountFromSweet(Sweet sweet, Ingredient ingredient) {
        return (int) sweet.getExtraIngredients()
                .stream()
                .filter(i -> i == ingredient)
                .count();
    }

    private void updateShopStockAfterAddToOrder(List<Ingredient> ingredientList) throws ServiceException {
        for (Ingredient ingredient : ingredientList) {
            try {
                decreasesIngredientsStock(ingredient);
            } catch (ServiceException e) {
                throw new ServiceException(e.getMessage());
            }
        }
    }

    private void decreasesIngredientsStock(Ingredient ingredient) throws ServiceException {
        int updatedAmount = ingredient.getAmount() - 1;
        if (updatedAmount < 0)
            throw new ServiceException("Invalid amount for this ingredient");
        ingredient.setAmount(updatedAmount);
        try {
            ingredientRepository.update(ingredient.getId(), ingredient);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private void increaseIngredientsStock(Ingredient ingredient) throws ServiceException {
        int updatedAmount = ingredient.getAmount() + 1;
        ingredient.setAmount(updatedAmount);
        try {
            ingredientRepository.update(ingredient.getId(), ingredient);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private void addSweetToOrder(Order order, Sweet sweet) {
        order.getOrderedSweets().merge(sweet, 1, Integer::sum);
    }


    private void addSweetToOrder(Order order, Sweet sweet, int quantity) {
        order.getOrderedSweets().merge(sweet, quantity, Integer::sum);
    }


    private void removeSweetFromOrder(Order order, Sweet sweet) {
        order.getOrderedSweets().merge(sweet, -1, Integer::sum);
        if (order.getOrderedSweets().get(sweet) == 0) order.getOrderedSweets().remove(sweet);
    }

    private double getProfit(Map<Sweet, Integer> orderedSweets) {
        double profit = 0;
        for (Sweet sweet : orderedSweets.keySet()) {
            profit += sweet.getPrice() - sweet.getExtraPrice() - priceForIngredients(sweet.getIngredientsList());
        }

        return profit;
    }

    private double priceForIngredients(List<Ingredient> ingredientsList) {
        return ingredientsList
                .stream()
                .mapToDouble(Ingredient::getPrice)
                .sum();
    }

}
