package service;

import domain.Customer;
import domain.Shop;
import domain.location.Location;
import domain.order.Order;
import domain.order.OrderType;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import repository.customersRepository.CustomerRepository;
import repository.exception.RepositoryException;
import repository.ordersRepository.OrderRepository;
import repository.sweetsRepository.SweetRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServiceImpl implements Service {
    private final Shop shop;
    private SweetRepository sweetRepository;
    private CustomerRepository customerRepository;
    private OrderRepository orderRepository;

    public ServiceImpl(Shop shop, SweetRepository sweetRepository, CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.shop = shop;
        this.sweetRepository = sweetRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }


    @Override
    public String getShopName() {
        return this.shop.getShopName();
    }

    @Override
    public Iterable<Sweet> getAvailableSweets() {
        return sweetRepository.findAll();
    }

    @Override
    public Order createOrder(Customer customer, OrderType orderType) throws ServiceException {
        int id = 1;
        while (true) {
            boolean ok = true;
            for (var o : orderRepository.findAll())
                if (o.getIdOrder() == id) {
                    ok = false;
                    break;
                }

            if (ok) break;
            id++;
        }

        try {
            Order order = new Order(id, new HashMap<>(),orderType, customer, shop);
            orderRepository.add(order);
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

        Sweet newSweet = sweetRepository.findOneSweet(id);
        if (newSweet == null)
            throw new ServiceException("Invalid sweet id!");
        else {
            Order updateOrder = orderRepository.findOneOrder(order.getIdOrder());
            updateOrder.addToSweetToOrder(newSweet);
            try {
                orderRepository.update(order.getIdOrder(), updateOrder);
            } catch (RepositoryException e) {
                throw new ServiceException(e.getMessage());
            }
        }
    }

    @Override
    public String getOrderDetails(long orderId) {
        return orderRepository.findOneOrder(orderId).toString();
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
                .filter(order -> order.getOrderDateTime().toLocalDate().isEqual(LocalDate.now()))
                .collect(Collectors.toList());
    }

    @Override
    public double getMoneyMadeToday() {
        return orderRepository.findAll()
                .stream()
                .filter(order -> order.getOrderDateTime().toLocalDate().isEqual(LocalDate.now()))
                .mapToDouble(Order::getFinalOrderPrice)
                .sum();
    }

    @Override
    public double getProfitMadeToday() {
        return orderRepository.findAll()
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
        Customer customerTry = customerRepository.findOneCustomer(mail);
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
            for (var o : orderRepository.findAll())
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
            customerRepository.add(customer);
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
        return customerRepository.findOneCustomer(mail) != null;
    }

    @Override
    public String printOrderDetails(String orderId) throws ServiceException {

        long id;
        try {
            id = Long.parseLong(orderId);
        } catch (Exception e) {
            throw new ServiceException("Invalid order number/id!");
        }

        Order yourOrder = orderRepository.findOneOrder(id);
        if (yourOrder == null)
            throw new ServiceException("Invalid order number/id!");
        else {
            return yourOrder.toString();
        }
    }
}
