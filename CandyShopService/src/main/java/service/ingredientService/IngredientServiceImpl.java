package service.ingredientService;

import domain.sweet.Ingredient;
import repository.ingredientRepository.IngredientRepository;
import service.exception.ServiceException;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

public class IngredientServiceImpl implements IngredientService {

    private IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }


    @Override
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    @Override
    public Ingredient findIngredientById(String ingredientId) throws ServiceException {
        long id;
        try {
            id = Long.parseLong(ingredientId);
        } catch (Exception e) {
            throw new ServiceException("Invalid ingredient id!");
        }
        return ingredientRepository.findIngredientById(id);
    }

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public List<String> showAllIngredientsInStock() {
        return ingredientRepository.findAll()
                .stream()
                .filter(ingredient -> ingredient.getAmount() > 0)
                .map(ingredient -> {
                    if (ingredient.getAmount() > 10)
                        return ingredient.getId() +
                                ". " + ingredient.getName() +
                                ",\tPrice: " + df.format(ingredient.getPrice()) + "$" +
                                "\tStock: " + ingredient.getAmount() + "\n";
                    else
                        return ingredient.getId() +
                                ". " + ingredient.getName() +
                                ",\tPrice: " + df.format(ingredient.getPrice()) + "$" +
                                "\tStock: " + ingredient.getAmount() +
                                "\t(reduced quantity in shop stock)\n";
                }).collect(Collectors.toList());
    }
}
