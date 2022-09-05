package service.ingredientService;

import domain.sweet.Ingredient;
import exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface IngredientService {
    /**
     * The method will return all the ingredients
     *
     * @return List of all ingredients
     */
    List<Ingredient> getAllIngredients();

    /**
     * The method will search for Ingredient with the given id
     *
     * @param ingredientId The id for Ingredient you are looking for
     * @return The Ingredient with teh given id
     * @throws ServiceException If the Ingredient does not exist
     */
    Optional<Ingredient> findIngredientById(String ingredientId) throws ServiceException;

    /**
     * The method will return all ingredients with stock quantity greater than 0
     *
     * @return List of all ingredients in stock
     */
    List<String> showAllIngredientsInStock();
}
