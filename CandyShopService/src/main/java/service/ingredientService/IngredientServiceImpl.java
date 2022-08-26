package service.ingredientService;

import domain.sweet.Ingredient;
import exception.ServiceException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.ingredientRepository.IngredientRepository;


import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static service.utils.Converter.convertStringToLong;

@AllArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientServiceImpl.class);

    private static final DecimalFormat df = new DecimalFormat("0.00");

    private IngredientRepository ingredientRepository;

    @Override
    public List<Ingredient> getAllIngredients() {
        LOGGER.info("GetAllIngredients - called");
        return ingredientRepository.findAll();
    }

    @Override
    public Optional<Ingredient> findIngredientById(String ingredientId) throws ServiceException {
        LOGGER.info("FindIngredientById for ingredient with id = {} - started", ingredientId);
        long id = convertStringToLong(ingredientId);
        Optional<Ingredient> ingredient = ingredientRepository.findIngredientById(id);
        if (ingredient.isPresent()) {
            LOGGER.info("FindIngredientById for ingredient with id = {} - finished", ingredientId);
            return ingredient;
        } else {
            LOGGER.warn("FindIngredientById for ingredient with id = {} - exception occurred -> {} ", ingredientId,
                    "Invalid ingredient id!");
            throw new ServiceException("Invalid ingredient id!");
        }
    }

    @Override
    public List<String> showAllIngredientsInStock() {
        LOGGER.info("ShowAllIngredientsInStock - called");
        return ingredientRepository.findAll()
                .stream()
                .filter(ingredient -> ingredient.getAmount() > 0)
                .map(ingredient -> String.valueOf(getIngredientDetails(ingredient)))
                .collect(Collectors.toList());
    }

    private StringBuilder getIngredientDetails(Ingredient ingredient) {
        LOGGER.info("GetIngredientDetails for ingredient - started");
        StringBuilder ingredientDetails = new StringBuilder("(Id:" + ingredient.getId() + ") " +
                ingredient.getName() +
                ",\tPrice: " + df.format(ingredient.getPrice()) + "$" +
                "\tStock: " + ingredient.getAmount());

        if (ingredient.getAmount() > 10) {
            LOGGER.info("GetIngredientDetails for ingredient - started");
            return ingredientDetails;
        }
        else {
            LOGGER.info("GetIngredientDetails for ingredient - started");
            return ingredientDetails.append("\t(reduced quantity in shop stock)");
        }
    }
}
