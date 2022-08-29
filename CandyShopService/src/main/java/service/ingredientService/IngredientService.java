package service.ingredientService;

import domain.sweet.Ingredient;
import exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface IngredientService {
    List<Ingredient> getAllIngredients();

    Optional<Ingredient> findIngredientById(String ingredientId) throws ServiceException;

    List<String> showAllIngredientsInStock();
}
