package repository.ingredientRepository;

import domain.sweet.Ingredient;
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

public class IngredientDataBaseRepository implements IngredientRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientDataBaseRepository.class);
    private EntityManagerFactory entityManagerFactory;

    public IngredientDataBaseRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void add(Ingredient ingredient) throws RepositoryException {
        LOGGER.info("Add ingredient - started");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.persist(ingredient);
            entityTransaction.commit();
            LOGGER.info("Add ingredient - transaction committed");
        } catch (Exception ex) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
                LOGGER.warn("Add ingredient - rolled back");
            }
            LOGGER.warn("Add ingredient - exception occurred -> {}", ex.getMessage());
            throw new RepositoryException("This element already exists!");
        } finally {
            entityManager.close();
        }
        LOGGER.info("Add ingredient - finished");
    }

    @Override
    public void update(Long id, Ingredient ingredient) throws RepositoryException {
        LOGGER.info("Update ingredient with id = {} - started", id);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;
        Ingredient ingredientToUpdate;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            ingredientToUpdate = entityManager.find(Ingredient.class, id);
            setUpIngredient(ingredientToUpdate, ingredient);
            entityManager.merge(ingredientToUpdate);
            entityTransaction.commit();
            LOGGER.info("Update ingredient with id = {} - transaction committed", id);
        } catch (Exception ex) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
                LOGGER.warn("Update ingredient with id = {} - rolled back", id);
            }
            LOGGER.warn("Update ingredient with id = {} to - exception occurred -> {}", id,
                    ex.getMessage());
            throw new RepositoryException("This element does not exist!");
        } finally {
            entityManager.close();
        }
        LOGGER.info("Update ingredient with id = {} - finished", id);
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        LOGGER.info("Delete ingredient with id = {} - started", id);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;
        Ingredient ingredientToDelete = null;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            ingredientToDelete = entityManager.find(Ingredient.class, id);
            entityManager.remove(ingredientToDelete);
            entityTransaction.commit();
            LOGGER.info("Delete ingredient with id = {} - transaction committed", id);
        } catch (Exception ex) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
                LOGGER.warn("Delete ingredient with id = {} - rolled back", id);
            }
            LOGGER.warn("Delete ingredient with id = {} - exception occurred -> {}", id, ex.getMessage());
            throw new RepositoryException("This element does not exist!");
        } finally {
            entityManager.close();
        }
        LOGGER.info("Delete ingredient with id = {} - finished", id);
    }

    @Override
    public List<Ingredient> findAll() {
        LOGGER.info("FindAll Ingredient - started");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Ingredient> typedQuery = entityManager.createNamedQuery(Ingredient.FIND_ALL, Ingredient.class);
        List<Ingredient> ingredientList = new ArrayList<>();
        try {
            ingredientList = typedQuery.getResultList();
            LOGGER.info("FindAll Ingredient - finished");
        } catch (NoResultException ex) {
            LOGGER.info("FindAll Ingredient - no result");
        } finally {
            entityManager.close();
        }
        return ingredientList;
    }

    @Override
    public Optional<Ingredient> findIngredientById(Long id) {
        LOGGER.info("FindIngredientById for ingredient with id = {} - started", id);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Ingredient> typedQuery = entityManager.createNamedQuery(Ingredient.FIND_BY_ID, Ingredient.class);
        typedQuery.setParameter("ingredientId", id);
        Ingredient ingredient;
        try {
            ingredient = typedQuery.getSingleResult();
            LOGGER.info("FindIngredientById for ingredient with id = {} - finished", id);
            return Optional.of(ingredient);
        } catch (NoResultException ex) {
            LOGGER.info("FindIngredientById for ingredient with id = {} - no result", id);
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Ingredient> findIngredientByName(String name) {
        LOGGER.info("FindIngredientByName for ingredient with name = {} - started", name);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Ingredient> typedQuery = entityManager.createNamedQuery(Ingredient.FIND_BY_NAME, Ingredient.class);
        typedQuery.setParameter("ingredientName", name);
        Ingredient ingredient;
        try {
            ingredient = typedQuery.getSingleResult();
            LOGGER.info("FindIngredientByName for ingredient with name = {} - finished", name);
            return Optional.of(ingredient);
        } catch (NoResultException ex) {
            LOGGER.info("FindIngredientByName for ingredient with name = {} - no result", name);
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Long> generateIngredientId() {
        LOGGER.info("GenerateIngredientId - started");
        long id = 1;
        while (true) {
            if (findIngredientById(id).isEmpty()) {
                LOGGER.info("GenerateIngredientId - finished");
                return Optional.of(id);
            }
            id++;
        }
    }

    private void setUpIngredient(Ingredient ingredientToUpdate, Ingredient ingredient) {
        ingredientToUpdate.setName(ingredient.getName());
        ingredientToUpdate.setPrice(ingredient.getPrice());
        ingredientToUpdate.setAmount(ingredient.getAmount());
    }
}
