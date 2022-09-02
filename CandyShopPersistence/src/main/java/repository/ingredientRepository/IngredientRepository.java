package repository.ingredientRepository;

import domain.sweet.Ingredient;
import repository.Repository;

import java.util.Optional;

public interface IngredientRepository extends Repository<Long, Ingredient> {
    /**
     * The method will search for Ingredient with the given id
     *
     * @param id The id for Ingredient you are looking for
     * @return Optional.of(Ingredient) - if Ingredient is found
     * Optional.empty() - else
     */
    Optional<Ingredient> findIngredientById(Long id);

    /**
     * The method will search for Ingredient with the given name
     *
     * @param name The name for Ingredient you are looking for
     * @return Optional.of(Ingredient) - if Ingredient is found
     * Optional.empty() - else
     */
    Optional<Ingredient> findIngredientByName(String name);

    /**
     * The method that will generate an id for Ingredient
     *
     * @return an available id
     */
    Optional<Long> generateIngredientId();
}
