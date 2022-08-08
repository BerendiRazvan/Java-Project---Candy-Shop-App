package service.ingredientService;

import domain.sweet.Ingredient;
import service.exception.ServiceException;

import java.util.List;

public interface IngredientService {
    List<Ingredient> getAllIngredients();

    Ingredient findIngredientById(String ingredientId) throws ServiceException;

    List<String> showAllIngredientsInStock();
}
