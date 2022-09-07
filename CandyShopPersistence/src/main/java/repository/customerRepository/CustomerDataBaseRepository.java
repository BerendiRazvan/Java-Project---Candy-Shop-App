package repository.customerRepository;

import domain.Customer;
import exception.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDataBaseRepository implements CustomerRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDataBaseRepository.class);
    private EntityManagerFactory entityManagerFactory;

    public CustomerDataBaseRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void add(Customer customer) throws RepositoryException {
        LOGGER.info("Add customer - started");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.persist(customer.getLocation());
            entityManager.persist(customer);
            entityTransaction.commit();
            LOGGER.info("Add customer - transaction committed");
        } catch (Exception ex) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
                LOGGER.warn("Add customer - rolled back");
            }
            LOGGER.warn("Add customer - exception occurred -> {}", ex.getMessage());
            throw new RepositoryException("This element already exists!");
        } finally {
            entityManager.close();
        }
        LOGGER.info("Add customer - finished");
    }

    @Override
    public void update(Long id, Customer customer) throws RepositoryException {
        LOGGER.info("Update customer with id = {} - started", id);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;
        Customer customerToUpdate;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            customerToUpdate = entityManager.find(Customer.class, id);
            setUpCustomer(customerToUpdate, customer);
            entityManager.merge(customerToUpdate);
            entityTransaction.commit();
            LOGGER.info("Update customer with id = {} - transaction committed", id);
        } catch (Exception ex) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
                LOGGER.warn("Update customer with id = {} - rolled back", id);
            }
            LOGGER.warn("Update customer with id = {} to - exception occurred -> {}", id,
                    ex.getMessage());
            throw new RepositoryException("This element does not exist!");
        } finally {
            entityManager.close();
        }
        LOGGER.info("Update customer with id = {} - finished", id);
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        LOGGER.info("Delete customer with id = {} - started", id);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;
        Customer customerToDelete;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            customerToDelete = entityManager.find(Customer.class, id);
            entityManager.remove(customerToDelete);
            entityTransaction.commit();
            LOGGER.info("Delete customer with id = {} - transaction committed", id);
        } catch (Exception ex) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
                LOGGER.warn("Delete customer with id = {} - rolled back", id);
            }
            LOGGER.warn("Delete customer with id = {} - exception occurred -> {}", id, ex.getMessage());
            throw new RepositoryException("This element does not exist!");
        } finally {
            entityManager.close();
        }
        LOGGER.info("Delete customer with id = {} - finished", id);
    }

    @Override
    public List<Customer> findAll() {
        LOGGER.info("FindAll customers - started");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Customer> typedQuery = entityManager.createNamedQuery(Customer.FIND_ALL, Customer.class);
        List<Customer> customerList = new ArrayList<>();
        try {
            customerList = typedQuery.getResultList();
            LOGGER.info("FindAll customers - finished");
        } catch (NoResultException ex) {
            LOGGER.info("FindAll customers - no result");
        } finally {
            entityManager.close();
        }
        return customerList;
    }

    @Override
    public Optional<Customer> findCustomerByEmail(String email) {
        LOGGER.info("FindCustomerByEmail for customer with email = {} - started", email);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Customer> typedQuery = entityManager.createNamedQuery(Customer.FIND_BY_EMAIL, Customer.class);
        typedQuery.setParameter("customerEmail", email);
        Customer customer;
        try {
            customer = typedQuery.getSingleResult();
            LOGGER.info("FindCustomerByEmail for customer with email = {} - finished", email);
            return Optional.of(customer);
        } catch (NoResultException ex) {
            LOGGER.info("FindCustomerByEmail for customer with email = {} - no result", email);
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Customer> findCustomerById(Long id) {
        LOGGER.info("FindCustomerById for customer with id = {} - started", id);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Customer> typedQuery = entityManager.createNamedQuery(Customer.FIND_BY_ID, Customer.class);
        typedQuery.setParameter("customerId", id);
        Customer customer;
        try {
            customer = typedQuery.getSingleResult();
            LOGGER.info("FindCustomerById for customer with id = {} - finished", id);
            return Optional.of(customer);
        } catch (NoResultException ex) {
            LOGGER.info("FindCustomerById for customer with id = {} - no result", id);
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Long> generateCustomerId() {
        LOGGER.info("GenerateCustomerId - started");
        long id = 1;
        while (true) {
            if (findCustomerById(id).isEmpty()) {
                LOGGER.info("GenerateCustomerId - finished");
                return Optional.of(id);
            }
            id++;
        }
    }

    private void setUpCustomer(Customer customerToUpdate, Customer customer) {
        customerToUpdate.setFirstName(customer.getFirstName());
        customerToUpdate.setLastName(customer.getLastName());
        customerToUpdate.setEmail(customer.getEmail());
        customerToUpdate.setPassword(customer.getPassword());
        customerToUpdate.setPhoneNumber(customer.getPhoneNumber());
    }
}
