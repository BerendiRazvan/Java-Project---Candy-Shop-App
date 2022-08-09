package service.sweetService;

import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import domain.sweet.SweetType;
import repository.exception.RepositoryException;
import repository.ingredientRepository.IngredientRepository;
import repository.sweetRepository.SweetRepository;
import service.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SweetServiceImpl implements SweetService {

    private static final double SWEET_DEFAULT_PRICE = 2;
    private SweetRepository sweetRepository;
    private IngredientRepository ingredientRepository;

    public SweetServiceImpl(SweetRepository sweetRepository, IngredientRepository ingredientRepository) {
        this.sweetRepository = sweetRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public List<Sweet> getAvailableSweets() {
        return sweetRepository.findAll()
                .stream()
                .filter(sweet -> sweet.getSweetType() != SweetType.UNIQUE && sweet.getExtraIngredients().isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public Sweet findSweetById(String sweetId) throws ServiceException {
        long id;
        try {
            id = Long.parseLong(sweetId);
        } catch (Exception e) {
            throw new ServiceException("Invalid sweet id!");
        }
        return sweetRepository.findSweetById(id);
    }

    @Override
    public Sweet createEmptySweet() throws ServiceException {
        long id = sweetRepository.generateSweetId();

        try {
            Sweet sweet = new Sweet(id, new ArrayList<>(), SweetType.UNIQUE, SWEET_DEFAULT_PRICE);
            sweetRepository.add(sweet);
            return sweet;
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void addIngredientToSweet(Sweet customSweet, Ingredient newIngredient, int amount) throws ServiceException {
        if (newIngredient == null) throw new ServiceException("Invalid ingredient id!");
        else {
            Sweet updateSweet = sweetRepository.findSweetById(customSweet.getId());
            while (amount != 0) {
                updateSweet.getIngredientsList().add(newIngredient);
                updateSweet.setPrice(customSweet.getPrice() + newIngredient.getPrice());
                amount--;
            }
            try {
                sweetRepository.update(customSweet.getId(), updateSweet);
            } catch (RepositoryException e) {
                throw new ServiceException(e.getMessage());
            }
        }
    }

    @Override
    public void addAllIngredientsToSweet(Sweet customSweet, String ingredients) throws ServiceException {
        List<String> ingredientList = List.of(ingredients.split(";"));
        for (String ingredientAndQuantity : ingredientList) {
            List<String> pair = List.of(ingredientAndQuantity.split(","));
            String ingredientName = pair.get(0);
            int amount;
            try {
                amount = Integer.parseInt(pair.get(1));
            } catch (Exception e) {
                throw new ServiceException("Invalid ingredients amount!");
            }

            Ingredient ingredient = ingredientRepository.findIngredientByName(ingredientName);
            if (ingredient != null)
                addIngredientToSweet(customSweet, ingredient, amount);
            else throw new ServiceException("Invalid ingredient name!");
        }
    }
}
