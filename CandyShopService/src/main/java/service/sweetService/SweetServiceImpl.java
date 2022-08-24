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
import repository.ingredientRepository.IngredientRepository;
import repository.sweetRepository.SweetRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static service.utils.Converter.convertStringToInt;

@Builder
@AllArgsConstructor
public class SweetServiceImpl implements SweetService {

    private static final double SWEET_DEFAULT_PRICE = 2;
    private SweetRepository sweetRepository;
    private IngredientRepository ingredientRepository;


    @Override
    public List<Sweet> getAvailableSweets() {
        return sweetRepository.findAll()
                .stream()
                .filter(sweet -> !sweet.getSweetType().equals(SweetType.UNIQUE) && sweet.getExtraIngredients().isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Sweet> findSweetById(String sweetId) throws ServiceException {
        long id;
        try {
            id = Long.parseLong(sweetId);
        } catch (Exception e) {
            throw new ServiceException("Invalid sweet id!");
        }
        Optional<Sweet> sweet = sweetRepository.findSweetById(id);
        if (sweet.isPresent())
            return sweet;
        else throw new ServiceException("Invalid sweet id!");
    }

    @Override
    public Optional<Sweet> createNewSweetWithoutIngredients() throws ServiceException, BuildException {

        Optional<Long> id = sweetRepository.generateSweetId();

        if (id.isPresent()) {
            try {
                SweetBuilder sweetBuilder = new SweetBuilder();
                Sweet sweet = sweetBuilder.build(id.get(), new ArrayList<>(), SweetType.UNIQUE, SWEET_DEFAULT_PRICE);
                sweetRepository.add(sweet);
                return Optional.of(sweet);
            } catch (RepositoryException e) {
                throw new ServiceException(e.getMessage());
            }
        } else throw new RuntimeException("Error: generateSweetId");
    }

    @Override
    public void addIngredientToSweet(Sweet customSweet, Ingredient newIngredient, int amount) throws ServiceException {
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
                    throw new ServiceException(e.getMessage());
                }
            } else throw new ServiceException("Invalid ingredient id!");
        }
    }

    @Override
    public void addAllIngredientsToSweet(Sweet customSweet, String ingredients) throws ServiceException {
        List<String> ingredientList = List.of(ingredients.split(";"));
        for (String ingredientAndQuantity : ingredientList) {
            List<String> pair = List.of(ingredientAndQuantity.split(","));
            String ingredientName = pair.get(0);
            Optional<Ingredient> ingredient = ingredientRepository.findIngredientByName(ingredientName);
            if (ingredient.isPresent()) {
                int amount = convertStringToInt(pair.get(1));
                validateAmount(ingredient.get(), amount);
                addIngredientToSweet(customSweet, ingredient.get(), amount);
            } else throw new ServiceException("Invalid ingredient name!");
        }
    }

    private void validateAmount(Ingredient ingredient, int amount) throws ServiceException {
        if (amount > ingredient.getAmount())
            throw new ServiceException("Invalid amount!");
        if (amount < 1)
            throw new ServiceException("Invalid amount!");
    }
}
