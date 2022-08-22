package service.ingredientService;

import domain.sweet.Ingredient;
import exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import repository.ingredientRepository.IngredientRepository;


import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    private static final DecimalFormat df = new DecimalFormat("0.00");

    private IngredientRepository ingredientRepository;


    @Override
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    @Override
    public Optional<Ingredient> findIngredientById(String ingredientId) throws ServiceException {
        long id;
        try {
            id = Long.parseLong(ingredientId);
        } catch (Exception e) {
            throw new ServiceException("Invalid ingredient id!");
        }
        Optional<Ingredient> ingredient = ingredientRepository.findIngredientById(id);
        if (ingredient.isPresent())
            return ingredient;
        else
            throw new ServiceException("Invalid ingredient id!");
    }

    @Override
    public List<String> showAllIngredientsInStock() {
        return ingredientRepository.findAll()
                .stream()
                .filter(ingredient -> ingredient.getAmount() > 0)
                .map(ingredient -> String.valueOf(getIngredientDetails(ingredient)))
                .collect(Collectors.toList());
    }

    private StringBuilder getIngredientDetails(Ingredient ingredient) {
        StringBuilder ingredientDetails = new StringBuilder("(Id:" + ingredient.getId() + ") " +
                ingredient.getName() +
                ",\tPrice: " + df.format(ingredient.getPrice()) + "$" +
                "\tStock: " + ingredient.getAmount());

        if (ingredient.getAmount() > 10)
            return ingredientDetails;
        else
            return ingredientDetails.append("\t(reduced quantity in shop stock)");
    }
}
