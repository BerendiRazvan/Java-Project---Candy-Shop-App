package repository.ingredientRepository;

import builder.IngredientBuilder;
import domain.sweet.Ingredient;
import exception.ValidationException;
import exception.RepositoryException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class IngredientInMemoryRepository implements IngredientRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientInMemoryRepository.class);
    private List<Ingredient> ingredientList;

    public IngredientInMemoryRepository() {
        this(new ArrayList<>());
        generateIngredients();
    }

    @Override
    public void add(Ingredient ingredient) throws RepositoryException {
        LOGGER.info("Add ingredient - started");
        if (!ingredientList.contains(ingredient)) {
            ingredientList.add(ingredient);
            LOGGER.info("Add ingredient - finished");
        } else {
            LOGGER.warn("Add ingredient - exception occurred -> {}", "This element already exists!");
            throw new RepositoryException("This element already exists!");
        }
    }

    @Override
    public void update(Long id, Ingredient ingredient) throws RepositoryException {
        LOGGER.info("Update ingredient with id = {} - started", id);
        Optional<Ingredient> ingredientToUpdate = findIngredientById(id);
        if (ingredientToUpdate.isPresent()) {
            ingredientList.set(ingredientList.indexOf(ingredientToUpdate.get()), ingredient);
            LOGGER.info("Update ingredient with id = {} - finished", id);
        } else {
            LOGGER.warn("Update ingredient with id = {} - exception occurred -> {}", id,
                    "This element does not exist!");
            throw new RepositoryException("This element does not exist!");
        }
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        LOGGER.info("Delete ingredient with id = {} - started", id);
        Optional<Ingredient> ingredientToRemove = findIngredientById(id);
        if (ingredientToRemove.isPresent()) {
            ingredientList.remove(ingredientToRemove.get());
            LOGGER.info("Delete ingredient with id = {} - finished", id);
        } else {
            LOGGER.warn("Delete ingredient with id = {} - exception occurred -> {}", id, "This element does not exist!");
            throw new RepositoryException("This element does not exist!");
        }
    }

    @Override
    public List<Ingredient> findAll() {
        LOGGER.info("FindAll ingredients - called");
        return ingredientList;
    }

    @Override
    public Optional<Ingredient> findIngredientById(Long id) {
        LOGGER.info("FindIngredientById for ingredient with id = {} - called", id);
        return ingredientList.stream()
                .filter(ingredient -> id == ingredient.getId())
                .findFirst();
    }

    @Override
    public Optional<Ingredient> findIngredientByName(String name) {
        LOGGER.info("FindIngredientByName for ingredient with name = {} - called", name);
        return ingredientList.stream()
                .filter(ingredient -> name.equalsIgnoreCase(ingredient.getName()))
                .findFirst();
    }


    private void generateIngredients() {
        LOGGER.info("GenerateIngredients - started");
        IngredientBuilder ingredientBuilder = new IngredientBuilder();
        try {
            ingredientList.addAll(Arrays.asList(
                    ingredientBuilder.build(1, "Sugar", 1.5, 23),
                    ingredientBuilder.build(2, "Milk", 0.5, 21),
                    ingredientBuilder.build(3, "Flour", 0.42, 30),
                    ingredientBuilder.build(4, "Chocolate", 1.75, 25),
                    ingredientBuilder.build(5, "Honey", 2.05, 7),
                    ingredientBuilder.build(6, "Caramel", 1.3, 17),
                    ingredientBuilder.build(7, "Whipped cream", 1, 40),
                    ingredientBuilder.build(8, "Vanilla", 0.73, 8),
                    ingredientBuilder.build(9, "Strawberries", 2.51, 14),
                    ingredientBuilder.build(10, "Cacao", 0.25, 5),
                    ingredientBuilder.build(11, "Peanut butter", 2.22, 7),
                    ingredientBuilder.build(12, "Coconut", 2.01, 20),
                    ingredientBuilder.build(13, "Cherry", 3.17, 12),
                    ingredientBuilder.build(14, "Oreo", 2.35, 32),
                    ingredientBuilder.build(15, "Ice Cream", 2.05, 10)
            ));
        } catch (ValidationException e) {
            System.out.println("Unfinished generation due to: " + e.getMessage());
            LOGGER.error("GenerateCustomers - exception occurred -> {}", e.getMessage());
        }
        LOGGER.info("GenerateIngredients - finished");
    }
}
