package service.orderService;

import domain.Customer;
import domain.Shop;
import domain.order.Order;
import domain.order.OrderType;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import domain.sweet.SweetType;
import repository.exception.RepositoryException;
import repository.ingredientRepository.IngredientRepository;
import repository.orderRepository.OrderRepository;
import repository.sweetRepository.SweetRepository;
import service.exception.ServiceException;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private SweetRepository sweetRepository;
    private IngredientRepository ingredientRepository;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public OrderServiceImpl(OrderRepository orderRepository, SweetRepository sweetRepository, IngredientRepository ingredientRepository) {
        this.orderRepository = orderRepository;
        this.ingredientRepository = ingredientRepository;
        this.sweetRepository = sweetRepository;
    }

    @Override
    public Order createOrder(Customer customer, OrderType orderType, Shop shop) throws ServiceException {
        int id = orderRepository.generateOrderId();

        try {
            Order order = new Order(id, new HashMap<>(), orderType, customer, shop);
            orderRepository.add(order);
            return order;
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }


    @Override
    public void addToOrder(Order order, Sweet newSweet) throws ServiceException {
        if (newSweet == null) throw new ServiceException("Invalid sweet id!");
        else {
            Order updateOrder = orderRepository.findOrderById(order.getId());
            try {
                updateShopStockAfterAddToOrder(newSweet.getIngredientsList());
                updateShopStockAfterAddToOrder(newSweet.getExtraIngredients());
                addSweetToOrder(updateOrder, newSweet);
                orderRepository.update(order.getId(), updateOrder);
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
            return yourOrder +
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

    @Override
    public void addExtraIngredientToOrderedSweet(Order order, Sweet sweet, Ingredient ingredient, String amount)
            throws ServiceException {
        if (sweet == null) throw new ServiceException("Invalid sweet!");
        if (ingredient == null) throw new ServiceException("Invalid ingredient!");
        int ingredientAmount;
        try {
            ingredientAmount = Integer.parseInt(amount);
        } catch (Exception e) {
            throw new ServiceException("Invalid amount!");
        }
        if (ingredientAmount > ingredient.getAmount())
            throw new ServiceException("Invalid amount!");
        if (ingredientAmount < 1)
            throw new ServiceException("Invalid amount!");
        if (order.getOrderedSweets().get(sweet) == null)
            throw new ServiceException("The sweet was not ordered!");
        else {
            Sweet customSweet = new Sweet(sweetRepository.generateSweetId(), sweet.getIngredientsList(),
                    sweet.getSweetType(), sweet.getPrice());
            customSweet.setExtraIngredients(new ArrayList<>(sweet.getExtraIngredients()));
            removeSweetFromOrder(order, sweet);
            while (ingredientAmount != 0) {
                customSweet.getExtraIngredients().add(ingredient);
                customSweet.setPrice(customSweet.getPrice() + ingredient.getPrice());
                decreasesIngredientsStock(ingredient);
                ingredientAmount--;
            }
            addSweetToOrder(order, customSweet);
            try {
                sweetRepository.add(customSweet);
            } catch (RepositoryException e) {
                throw new ServiceException(e.getMessage());
            }
        }
    }

    @Override
    public void updateExtraIngredientForOrderedSweet(Order order, Sweet sweet, Ingredient ingredient, String amount)
            throws ServiceException {
        if (sweet == null) throw new ServiceException("Invalid sweet!");
        if (ingredient == null) throw new ServiceException("Invalid ingredient!");
        int ingredientAmount;
        try {
            ingredientAmount = Integer.parseInt(amount);
        } catch (Exception e) {
            throw new ServiceException("Invalid amount!");
        }
        if (ingredientAmount > ingredient.getAmount())
            throw new ServiceException("Invalid amount!");
        if (ingredientAmount < 1)
            throw new ServiceException("Invalid amount!");
        if (order.getOrderedSweets().get(sweet) == null)
            throw new ServiceException("The sweet was not ordered!");
        else {
            int actualAmount = (int) sweet.getExtraIngredients()
                    .stream()
                    .filter(i -> i == ingredient)
                    .count();
            while (actualAmount - ingredientAmount != 0) {
                if (actualAmount < ingredientAmount) {
                    sweet.getExtraIngredients().add(ingredient);
                    sweet.setPrice(sweet.getPrice() + ingredient.getPrice());
                    decreasesIngredientsStock(ingredient);
                    actualAmount++;
                } else {
                    sweet.getExtraIngredients().remove(ingredient);
                    sweet.setPrice(sweet.getPrice() - ingredient.getPrice());
                    increaseIngredientsStock(ingredient);
                    actualAmount--;
                }
            }
            try {
                sweetRepository.update(sweet.getId(), sweet);
            } catch (RepositoryException e) {
                throw new ServiceException(e.getMessage());
            }
        }
    }

    @Override
    public void deleteExtraIngredientForOrderedSweet(Order order, Sweet sweet, Ingredient ingredient) throws ServiceException {
        if (sweet == null) throw new ServiceException("Invalid sweet!");
        if (ingredient == null) throw new ServiceException("Invalid ingredient!");
        if (order.getOrderedSweets().get(sweet) == null)
            throw new ServiceException("The sweet was not ordered!");
        else {
            int actualAmount = (int) sweet.getExtraIngredients()
                    .stream()
                    .filter(i -> i == ingredient)
                    .count();
            while (actualAmount != 0) {
                sweet.getExtraIngredients().remove(ingredient);
                sweet.setPrice(sweet.getPrice() - ingredient.getPrice());
                increaseIngredientsStock(ingredient);
                actualAmount--;
            }
            try {
                sweetRepository.update(sweet.getId(), sweet);
            } catch (RepositoryException e) {
                throw new ServiceException(e.getMessage());
            }
        }
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
