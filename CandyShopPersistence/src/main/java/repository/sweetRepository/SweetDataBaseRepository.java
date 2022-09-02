package repository.sweetRepository;

import domain.sweet.Sweet;
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

public class SweetDataBaseRepository implements SweetRepository {
    private static EntityManagerFactory entityManagerFactory;
    private static final Logger LOGGER = LoggerFactory.getLogger(SweetDataBaseRepository.class);

    public SweetDataBaseRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void add(Sweet sweet) throws RepositoryException {
        LOGGER.info("Add sweet - started");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.persist(sweet);
            entityTransaction.commit();
            LOGGER.info("Add sweet - transaction committed");
        } catch (Exception ex) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
                LOGGER.warn("Add sweet - rolled back");
            }
            LOGGER.warn("Add sweet - exception occurred -> {}", ex.getMessage());
            throw new RepositoryException("This element already exists!");
        } finally {
            entityManager.close();
        }
        LOGGER.info("Add sweet - finished");
    }

    @Override
    public void update(Long id, Sweet sweet) throws RepositoryException {
        LOGGER.info("Update sweet with id = {} - started", id);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;
        Sweet sweetToUpdate;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            sweetToUpdate = entityManager.find(Sweet.class, id);
            sweetToUpdate.setSweetType(sweet.getSweetType());
            sweetToUpdate.setPrice(sweet.getPrice());
            sweetToUpdate.setIngredientsList(sweet.getIngredientsList());
            sweetToUpdate.setExtraIngredients(sweet.getExtraIngredients());
            entityManager.persist(sweetToUpdate);
            entityTransaction.commit();
            LOGGER.info("Update sweet with id = {} - transaction committed", id);
        } catch (Exception ex) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
                LOGGER.warn("Update sweet with id = {} - rolled back", id);
            }
            LOGGER.warn("Update sweet with id = {} to - exception occurred -> {}", id,
                    ex.getMessage());
            throw new RepositoryException("This element does not exist!");
        } finally {
            entityManager.close();
        }
        LOGGER.info("Update sweet with id = {} - finished", id);
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        LOGGER.info("Delete sweet with id = {} - started", id);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;
        Sweet sweetToDelete;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            sweetToDelete = entityManager.find(Sweet.class, id);
            entityManager.remove(sweetToDelete);
            entityTransaction.commit();
            LOGGER.info("Delete sweet with id = {} - transaction committed", id);
        } catch (Exception ex) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
                LOGGER.warn("Delete sweet with id = {} - rolled back", id);
            }
            LOGGER.warn("Delete sweet with id = {} - exception occurred -> {}", id, ex.getMessage());
            throw new RepositoryException("This element does not exist!");
        } finally {
            entityManager.close();
        }
        LOGGER.info("Delete sweet with id = {} - finished", id);
    }

    @Override
    public List<Sweet> findAll() {
        LOGGER.info("FindAll sweets - started");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Sweet> typedQuery = entityManager.createNamedQuery(Sweet.FIND_ALL, Sweet.class);
        List<Sweet> sweetList = new ArrayList<>();
        try {
            sweetList = typedQuery.getResultList();
            LOGGER.info("FindAll sweets - finished");
        } catch (NoResultException ex) {
            LOGGER.info("FindAll sweets - no result");
        } finally {
            entityManager.close();
        }
        return sweetList;
    }

    @Override
    public Optional<Sweet> findSweetById(Long id) {
        LOGGER.info("FindSweetById for sweet with id = {} - started", id);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Sweet> typedQuery = entityManager.createNamedQuery(Sweet.FIND_BY_ID, Sweet.class);
        typedQuery.setParameter("sweetId", id);
        Sweet sweet;
        try {
            sweet = typedQuery.getSingleResult();
            LOGGER.info("FindSweetById for sweet with id = {} - finished", id);
            return Optional.of(sweet);
        } catch (NoResultException ex) {
            LOGGER.info("FindSweetById for sweet with id = {} - no result", id);
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Long> generateSweetId() {
        LOGGER.info("GenerateSweetId - started");
        long id = 1;
        while (true) {
            if (findSweetById(id).isEmpty()) {
                LOGGER.info("GenerateSweetId - finished");
                return Optional.of(id);
            }
            id++;
        }
    }
}
