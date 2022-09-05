package service.sweetService;

import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import exception.ValidationException;
import exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface SweetService {
    /**
     * The method will return all the sweets available
     *
     * @return List of all sweets available
     */
    List<Sweet> getAvailableSweets();

    /**
     * The method will search for Sweet with the given id
     *
     * @param sweetId The id for Sweet you are looking for
     * @return The Sweet with teh given id
     * @throws ServiceException If the Sweet does not exist
     */
    Optional<Sweet> findSweetById(String sweetId) throws ServiceException;

    /**
     * The method creates a new sweet without ingredients
     *
     * @return The new sweet
     * @throws ServiceException    If the sweet creation fails
     * @throws ValidationException If the data entered for the new sweet are invalid
     */
    Optional<Sweet> createNewSweetWithoutIngredients() throws ServiceException, ValidationException;

    /**
     * The method will add an amount of ingredients to the sweet recipe
     *
     * @param customSweet   The sweet that needs to be modified
     * @param newIngredient The ingredient to be added
     * @param amount        The amount of ingredients
     * @throws ServiceException If the data entered for sweet, ingredient or amount are invalid
     */
    void addIngredientToSweet(Sweet customSweet, Ingredient newIngredient, int amount) throws ServiceException;

    /**
     * The method will create a new sweet with the specified string of ingredients
     *
     * @param customSweet The sweet that needs to be modified
     * @param ingredients The ingredient to be added
     * @throws ServiceException If the data entered for sweet or ingredient are invalid
     */
    void addAllIngredientsToSweet(Sweet customSweet, String ingredients) throws ServiceException;
}
