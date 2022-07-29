package service.sweetService;

import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import repository.sweetsRepository.SweetRepository;
import service.exception.ServiceException;

import java.util.List;

public class SweetServiceImpl implements SweetService {

    private SweetRepository sweetRepository;

    public SweetServiceImpl(SweetRepository sweetRepository) {
        this.sweetRepository = sweetRepository;
    }

    @Override
    public List<Sweet> getAvailableSweets() {
        return sweetRepository.findAll();
    }

    @Override
    public Sweet findSweet(String sweetId) throws ServiceException {
        long id;
        try {
            id = Long.parseLong(sweetId);
        } catch (Exception e) {
            throw new ServiceException("Invalid sweet id!");
        }

        return sweetRepository.findSweetById(id);

    }

    private void addIngredientToRecipe(Sweet sweet, Ingredient newIngredient) throws Exception {
        List<Ingredient> ingredientsList = sweet.getIngredientsList();
        if (!ingredientsList.contains(newIngredient)) ingredientsList.add(newIngredient);
        else throw new Exception("This ingredient exists!");
    }

    private void addExtraIngredient(Sweet sweet, Ingredient extraIngredient) throws Exception {
        List<Ingredient> extraIngredients = sweet.getExtraIngredients();
        if (!extraIngredients.contains(extraIngredient)) extraIngredients.add(extraIngredient);
        else throw new Exception("This ingredient exists!");
    }

    private void removeIngredientFromRecipe(Sweet sweet, Ingredient ingredient) throws Exception {
        List<Ingredient> ingredientsList = sweet.getIngredientsList();
        if (ingredientsList.contains(ingredient)) ingredientsList.remove(ingredient);
        else throw new Exception("This ingredient does not exist!");
    }

    private void removeExtraIngredient(Sweet sweet, Ingredient extraIngredient) throws Exception {
        List<Ingredient> extraIngredients = sweet.getExtraIngredients();
        if (extraIngredients.contains(extraIngredient)) extraIngredients.remove(extraIngredient);
        else throw new Exception("This ingredient does not exist!");
    }


}
