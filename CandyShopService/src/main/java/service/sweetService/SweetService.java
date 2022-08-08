package service.sweetService;

import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import service.exception.ServiceException;

import java.util.List;

public interface SweetService {
    List<Sweet> getAvailableSweets();

    Sweet findSweetById(String sweetId) throws ServiceException;

    Sweet createEmptySweet() throws ServiceException;

    void addIngredientToSweet(Sweet customSweet, Ingredient newIngredient, int amount) throws ServiceException;

    void addAllIngredientsToSweet(Sweet customSweet, String ingredients) throws ServiceException;
}
