package repository.orderRepository;

import domain.order.Order;
import exception.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDataBaseRepository implements OrderRepository {

    private static EntityManagerFactory entityManagerFactory;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDataBaseRepository.class);

    public OrderDataBaseRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void add(Order order) throws RepositoryException {
        LOGGER.info("Add order - started");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.persist(order);
            entityTransaction.commit();
            LOGGER.info("Add order - transaction committed");
        } catch (Exception ex) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
                LOGGER.warn("Add order - rolled back");
            }
            LOGGER.warn("Add order - exception occurred -> {}", ex.getMessage());
            throw new RepositoryException("This element already exists!");
        } finally {
            entityManager.close();
        }
        LOGGER.info("Add order - finished");
    }

    @Override
    public void update(Long id, Order order) throws RepositoryException {
        LOGGER.info("Update order with id = {} - started", id);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;
        Order orderToUpdate;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            orderToUpdate = entityManager.find(Order.class, id);
            orderToUpdate.setOrderedSweets(order.getOrderedSweets());
            orderToUpdate.setOrderType(order.getOrderType());
            orderToUpdate.setWaitingTime(order.getWaitingTime());
            orderToUpdate.setCustomer(order.getCustomer());
            orderToUpdate.setShop(order.getShop());
            entityManager.persist(orderToUpdate);
            entityTransaction.commit();
            LOGGER.info("Update order with id = {} - transaction committed", id);
        } catch (Exception ex) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
                LOGGER.warn("Update order with id = {} - rolled back", id);
            }
            LOGGER.warn("Update order with id = {} to - exception occurred -> {}", id,
                    ex.getMessage());
            throw new RepositoryException("This element does not exist!");
        } finally {
            entityManager.close();
        }
        LOGGER.info("Update order with id = {} - finished", id);
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        LOGGER.info("Delete order with id = {} - started", id);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;
        Order orderToDelete;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            orderToDelete = entityManager.find(Order.class, id);
            entityManager.remove(orderToDelete);
            entityTransaction.commit();
            LOGGER.info("Delete order with id = {} - transaction committed", id);
        } catch (Exception ex) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
                LOGGER.warn("Delete order with id = {} - rolled back", id);
            }
            LOGGER.warn("Delete order with id = {} - exception occurred -> {}", id, ex.getMessage());
            throw new RepositoryException("This element does not exist!");
        } finally {
            entityManager.close();
        }
        LOGGER.info("Delete order with id = {} - finished", id);
    }

    @Override
    public List<Order> findAll() {
        LOGGER.info("FindAll orders - started");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Order> typedQuery = entityManager.createNamedQuery(Order.FIND_ALL, Order.class);
        List<Order> orderList = new ArrayList<>();
        try {
            orderList = typedQuery.getResultList();
            LOGGER.info("FindAll orders - finished");
        } catch (NoResultException ex) {
            LOGGER.info("FindAll orders - no result");
        } finally {
            entityManager.close();
        }
        return orderList;
    }

    @Override
    public Optional<Order> findOrderById(Long id) {
        LOGGER.info("FindOrderById for order with id = {} - started", id);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Order> typedQuery = entityManager.createNamedQuery(Order.FIND_BY_ID, Order.class);
        typedQuery.setParameter("orderId", id);
        Order order;
        try {
            order = typedQuery.getSingleResult();
            LOGGER.info("FindOrderById for order with id = {} - finished", id);
            return Optional.of(order);
        } catch (NoResultException ex) {
            LOGGER.info("FindOrderById for order with id = {} - no result", id);
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Long> generateOrderId() {
        LOGGER.info("GenerateOrderId - started");
        long id = 1;
        while (true) {
            if (findOrderById(id).isEmpty()) {
                LOGGER.info("GenerateOrderId - finished");
                return Optional.of(id);
            }
            id++;
        }
    }
}
