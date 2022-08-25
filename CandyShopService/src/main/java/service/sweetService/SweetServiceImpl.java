package service.sweetService;

import builder.SweetBuilder;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import domain.sweet.SweetType;
import exception.BuildException;
import exception.RepositoryException;
import exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.ingredientRepository.IngredientRepository;
import repository.sweetRepository.SweetRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static service.utils.Converter.convertStringToInt;
import static service.utils.Converter.convertStringToLong;

@Builder
@AllArgsConstructor
public class SweetServiceImpl implements SweetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SweetServiceImpl.class);
    private static final double SWEET_DEFAULT_PRICE = 2;
    private SweetRepository sweetRepository;
    private IngredientRepository ingredientRepository;


    @Override
    public List<Sweet> getAvailableSweets() {
        LOGGER.info("GetAvailableSweets - called");
        return sweetRepository.findAll()
                .stream()
                .filter(sweet -> !sweet.getSweetType().equals(SweetType.UNIQUE) && sweet.getExtraIngredients().isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Sweet> findSweetById(String sweetId) throws ServiceException {
        LOGGER.info("FindSweetById for sweet with id = {} - started", sweetId);
        long id = convertStringToLong(sweetId);
        Optional<Sweet> sweet = sweetRepository.findSweetById(id);
        if (sweet.isPresent()) {
            LOGGER.info("FindSweetById for sweet with id = {} - finished", sweetId);
            return sweet;
        } else {
            LOGGER.warn("FindSweetById for sweet with id = {} - exception occurred -> {}", sweetId, "Invalid sweet id!");
            throw new ServiceException("Invalid sweet id!");
        }
    }

    @Override
    public Optional<Sweet> createNewSweetWithoutIngredients() throws ServiceException, BuildException {
        LOGGER.info("CreateNewSweetWithoutIngredients - started");
        Optional<Long> id = sweetRepository.generateSweetId();

        if (id.isPresent()) {
            try {
                SweetBuilder sweetBuilder = new SweetBuilder();
                Sweet sweet = sweetBuilder.build(id.get(), new ArrayList<>(), SweetType.UNIQUE, SWEET_DEFAULT_PRICE);
                sweetRepository.add(sweet);
                LOGGER.info("CreateNewSweetWithoutIngredients - finished");
                return Optional.of(sweet);
            } catch (RepositoryException e) {
                LOGGER.warn("CreateNewSweetWithoutIngredients - exception occurred -> {}", e.getMessage());
                throw new ServiceException(e.getMessage());
            }
        } else {
            LOGGER.error("CreateNewSweetWithoutIngredients - exception occurred -> {}", "Error: generateSweetId");
            throw new RuntimeException("Error: generateSweetId");
        }
    }

    @Override
    public void addIngredientToSweet(Sweet customSweet, Ingredient newIngredient, int amount) throws ServiceException {
        LOGGER.info("AddIngredientToSweet for sweet, ingredient and amount = {} - started", amount);
        if (newIngredient == null) throw new ServiceException("Invalid ingredient id!");
        else {
            Optional<Sweet> updateSweet = sweetRepository.findSweetById(customSweet.getId());
            if (updateSweet.isPresent()) {
                while (amount != 0) {
                    updateSweet.get().getIngredientsList().add(newIngredient);
                    updateSweet.get().setPrice(customSweet.getPrice() + newIngredient.getPrice());
                    amount--;
                }
                try {
                    sweetRepository.update(customSweet.getId(), updateSweet.get());
                } catch (RepositoryException e) {
                    LOGGER.warn("AddIngredientToSweet for sweet, ingredient and amount = {} - exception occurred -> {}",
                            amount, e.getMessage());
                    throw new ServiceException(e.getMessage());
                }
            } else {
                LOGGER.warn("AddIngredientToSweet for sweet, ingredient and amount = {} - exception occurred -> {}",
                        amount, "Invalid ingredient id!");
                throw new ServiceException("Invalid ingredient id!");
            }
        }
        LOGGER.info("AddIngredientToSweet for sweet, ingredient and amount = {} - finished", amount);
    }

    @Override
    public void addAllIngredientsToSweet(Sweet customSweet, String ingredients) throws ServiceException {
        LOGGER.info("AddAllIngredientsToSweet for sweet and ingredients string = {} - started", ingredients);
        List<String> ingredientList = List.of(ingredients.split(";"));
        for (String ingredientAndQuantity : ingredientList) {
            List<String> pair = List.of(ingredientAndQuantity.split(","));
            String ingredientName = pair.get(0);
            Optional<Ingredient> ingredient = ingredientRepository.findIngredientByName(ingredientName);
            if (ingredient.isPresent()) {
                int amount = convertStringToInt(pair.get(1));
                validateAmount(ingredient.get(), amount);
                addIngredientToSweet(customSweet, ingredient.get(), amount);
            } else {
                LOGGER.warn("AddAllIngredientsToSweet for sweet and ingredients string = {} - exception occurred -> {}",
                        ingredients, "Invalid ingredient name!");
                throw new ServiceException("Invalid ingredient name!");
            }
        }
        LOGGER.info("AddAllIngredientsToSweet for sweet and ingredients string = {} - finished", ingredients);
    }

    private void validateAmount(Ingredient ingredient, int amount) throws ServiceException {
        LOGGER.info("ValidateAmount for sweet and amount = {} - started", amount);
        if (amount > ingredient.getAmount())
            throw new ServiceException("Invalid amount!");
        if (amount < 1)
            throw new ServiceException("Invalid amount!");
        LOGGER.info("ValidateAmount for sweet and amount = {} - finished", amount);
    }
}
