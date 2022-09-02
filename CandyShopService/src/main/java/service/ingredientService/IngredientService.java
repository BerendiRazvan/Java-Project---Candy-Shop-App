package service.ingredientService;

import domain.sweet.Ingredient;
import exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface IngredientService {
    /**
     *
     * @return
     */
    List<Ingredient> getAllIngredients();

    /**
     *
     * @param ingredientId
     * @return
     * @throws ServiceException
     */
    Optional<Ingredient> findIngredientById(String ingredientId) throws ServiceException;

    /**
     *
     * @return
     */
    List<String> showAllIngredientsInStock();
}
