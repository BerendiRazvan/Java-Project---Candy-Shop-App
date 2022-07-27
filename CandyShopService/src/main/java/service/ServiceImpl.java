package service;

import domain.Customer;
import domain.Shop;
import domain.location.Location;
import domain.order.Delivery;
import domain.order.Order;
import domain.order.OrderType;
import domain.order.PickUp;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import repository.customersRepository.CustomersRepository;
import repository.exception.RepositoryException;
import repository.ordersRepository.OrdersRepository;
import repository.sweetsRepository.SweetsRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServiceImpl implements Service {
    private final Shop shop;
    private SweetsRepository sweetsRepository;
    private CustomersRepository customersRepository;
    private OrdersRepository ordersRepository;

    public ServiceImpl(Shop shop, SweetsRepository sweetsRepository, CustomersRepository customersRepository, OrdersRepository ordersRepository) {
        this.shop = shop;
        this.sweetsRepository = sweetsRepository;
        this.customersRepository = customersRepository;
        this.ordersRepository = ordersRepository;
    }


    @Override
    public String getShopName() {
        return this.shop.getShopName();
    }

    @Override
    public Iterable<Sweet> getAvailableSweets() {
        return sweetsRepository.findAll();
    }

    @Override
    public Order createOrder(Customer customer, OrderType orderType) throws ServiceException {
        int id = 1;
        while (true) {
            boolean ok = true;
            for (var o : ordersRepository.findAll())
                if (o.getIdOrder() == id) {
                    ok = false;
                    break;
                }

            if (ok) break;
            id++;
        }

        try {
            Order order;

            if (orderType == OrderType.PICK_UP)
                order = new PickUp(id, new HashMap<>(), customer, shop);
            else
                order = new Delivery(id, new HashMap<>(), customer, shop);

            ordersRepository.add(order);
            return order;
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void addToOrder(Order order, String sweetId) throws ServiceException {
        long id;
        try {
            id = Long.parseLong(sweetId);
        } catch (Exception e) {
            throw new ServiceException("Invalid sweet id!");
        }

        Sweet newSweet = sweetsRepository.findOneSweet(id);
        if (newSweet == null)
            throw new ServiceException("Invalid sweet id!");
        else {
            Order updateOrder = ordersRepository.findOneOrder(order.getIdOrder());
            updateOrder.addToSweetToOrder(newSweet);
            try {
                ordersRepository.update(order.getIdOrder(), updateOrder);
            } catch (RepositoryException e) {
                throw new ServiceException(e.getMessage());
            }
        }
    }

    @Override
    public String getOrderDetails(long orderId) {
        return ordersRepository.findOneOrder(orderId).toString();
    }

    @Override
    public void removeOrder(long idOrder) throws ServiceException {
        try {
            ordersRepository.delete(idOrder);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Order> getAllOrdersInADay() {
        return ordersRepository.findAll()
                .stream()
                .filter(order -> order.getOrderDateTime().toLocalDate().isEqual(LocalDate.now()))
                .collect(Collectors.toList());
    }

    @Override
    public double getMoneyMadeToday() {
        return ordersRepository.findAll()
                .stream()
                .filter(order -> order.getOrderDateTime().toLocalDate().isEqual(LocalDate.now()))
                .mapToDouble(Order::getFinalOrderPrice)
                .sum();
    }

    @Override
    public double getProfitMadeToday() {
        return ordersRepository.findAll()
                .stream()
                .filter(order -> order.getOrderDateTime().toLocalDate().isEqual(LocalDate.now()))
                .mapToDouble(order -> getProfit(order.getOrderedSweets()))
                .sum();
    }

    private double getProfit(Map<Sweet, Integer> orderedSweets) {
        double profit = 0;
        for (Sweet sweet : orderedSweets.keySet()) {
            profit += sweet.getPrice() - sweet.getExtraPrice() - sweet.getSweetRecipe().getIngredientsList()
                    .stream()
                    .mapToDouble(Ingredient::getPrice)
                    .sum();
        }

        return profit;
    }

    @Override
    public Customer login(String mail, String password) throws ServiceException {
        Customer customerTry = customersRepository.findOneCustomer(mail);
        if (customerTry != null) {
            if (password.equals(customerTry.getPassword())) {
                return customerTry;
            } else
                throw new ServiceException("Invalid password!\n");
        } else {
            throw new ServiceException("Authentication failed!");
        }
    }

    @Override
    public Customer createAccount(String firstName, String lastName, String email, String password,
                                  String phoneNumber, Location customerLocation) throws Exception {

        int id = 1;
        while (true) {
            boolean ok = true;
            for (var o : ordersRepository.findAll())
                if (o.getIdOrder() == id) {
                    ok = false;
                    break;
                }

            if (ok) break;
            id++;
        }

        String verif = verifCustomer(firstName, lastName, email, password, phoneNumber, customerLocation);
        if (!verif.matches("")) {
            throw new ServiceException(verif);
        }

        Customer customer = new Customer(id, firstName, lastName, email, password, phoneNumber, customerLocation);
        try {
            customersRepository.add(customer);
        } catch (RepositoryException e) {
            throw new Exception(e.getMessage());
        }

        return customer;
    }

    private String verifCustomer(String firstName, String lastName, String email, String password, String phoneNumber,
                                 Location location) {
        String error = "";

        if (firstName.equals("") || !firstName.matches("[a-zA-Z]+"))
            error += "Invalid first name!\n";

        if (lastName.equals("") || !lastName.matches("[a-zA-Z]+"))
            error += "Invalid last name!\n";

        if (email.equals("") || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            error += "Invalid email!\n";

        if (password.length() < 6)
            error += "Invalid password!\n";

        if (phoneNumber.length() != 10 || !phoneNumber.matches("[0-9]+"))
            error += "Invalid phone number!\n";

        if (location.getAddress().length() < 10)
            error += "Invalid address!\n";

        return error;
    }

    @Override
    public boolean findMail(String mail) {
        return customersRepository.findOneCustomer(mail) != null;
    }

    @Override
    public String printOrderDetails(String orderId) throws ServiceException {

        long id;
        try {
            id = Long.parseLong(orderId);
        } catch (Exception e) {
            throw new ServiceException("Invalid order number/id!");
        }

        Order yourOrder = ordersRepository.findOneOrder(id);
        if (yourOrder == null)
            throw new ServiceException("Invalid order number/id!");
        else {
            return yourOrder.toString();
        }
    }
}
