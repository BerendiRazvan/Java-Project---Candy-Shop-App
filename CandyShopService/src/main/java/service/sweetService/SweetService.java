package service.sweetService;

import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import exception.ValidationException;
import exception.ServiceException;
import java.util.List;
import java.util.Optional;

public interface SweetService {
    /**
     *
     * @return
     */
    List<Sweet> getAvailableSweets();

    /**
     *
     * @param sweetId
     * @return
     * @throws ServiceException
     */
    Optional<Sweet>  findSweetById(String sweetId) throws ServiceException;

    /**
     *
     * @return
     * @throws ServiceException
     * @throws ValidationException
     */
    Optional<Sweet> createNewSweetWithoutIngredients() throws ServiceException, ValidationException;

    /**
     *
     * @param customSweet
     * @param newIngredient
     * @param amount
     * @throws ServiceException
     */
    void addIngredientToSweet(Sweet customSweet, Ingredient newIngredient, int amount) throws ServiceException;

    /**
     *
     * @param customSweet
     * @param ingredients
     * @throws ServiceException
     */
    void addAllIngredientsToSweet(Sweet customSweet, String ingredients) throws ServiceException;
}
