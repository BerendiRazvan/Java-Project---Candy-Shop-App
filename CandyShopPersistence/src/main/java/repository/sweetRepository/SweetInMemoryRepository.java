package repository.sweetRepository;

import domain.order.Order;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import domain.sweet.SweetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import repository.exception.RepositoryException;
import repository.ingredientRepository.IngredientRepository;

import java.util.*;

@Builder
@AllArgsConstructor
public class SweetInMemoryRepository implements SweetRepository {

    private static final int SWEETS_TO_GENERATE = 15;
    private List<Sweet> sweetList;

    @Override
    public void add(Sweet sweet) throws RepositoryException {
        if (!sweetList.contains(sweet))
            sweetList.add(sweet);
        else
            throw new RepositoryException("This element already exists!");
    }

    @Override
    public void update(Long id, Sweet sweet) throws RepositoryException {
        Sweet sweetToUpdate = findSweetById(id);
        if (sweetToUpdate == null)
            throw new RepositoryException("This element does not exist!");
        else
            sweetList.set(sweetList.indexOf(sweetToUpdate), sweet);
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        Sweet sweetToDelete = findSweetById(id);
        if (sweetToDelete == null)
            throw new RepositoryException("This element does not exist!");
        else
            sweetList.remove(sweetToDelete);
    }

    @Override
    public List<Sweet> findAll() {
        return sweetList;
    }

    @Override
    public Sweet findSweetById(Long id) {
        for (Sweet sweet : sweetList)
            if (id == sweet.getId()) return sweet;
        return null;
    }

    @Override
    public void generateSweets(IngredientRepository ingredientRepository) {
        List<Ingredient> ingredientList = ingredientRepository.findAll();
        int noOfSweets = SWEETS_TO_GENERATE;
        while (noOfSweets != 0) {
            sweetList.add(Sweet.builder()
                    .id(generateSweetId())
                    .ingredientsList(randomRecipe(ingredientList))
                    .sweetType(randomSweetType())
                    .price(0)
                    .build());
            noOfSweets--;
        }
        sweetList.forEach(sweet -> sweet.setPrice(generatePrice(sweet.getIngredientsList())));
    }

    @Override
    public long generateSweetId() {
        int id = 1;
        while (true) {
            boolean ok = true;
            for (var s : sweetList)
                if (s.getId() == id) {
                    ok = false;
                    break;
                }

            if (ok) return id;
            id++;
        }
    }

    private static double generatePrice(List<Ingredient> list) {
        Random random = new Random();
        int extraPriceAddedBySeller = random.nextInt(5);
        return extraPriceAddedBySeller +
                list.stream()
                        .mapToDouble(Ingredient::getPrice)
                        .sum();
    }

    private static List<Ingredient> randomRecipe(List<Ingredient> ingredientList) {
        Random random = new Random();
        int randomNumberOfSweets = random.nextInt(7 - 1) + 2;

        List<Ingredient> recipe = new ArrayList<>();

        while (randomNumberOfSweets > 0) {
            Ingredient ingredient = ingredientList.get(random.nextInt(ingredientList.size()));
            if (!recipe.contains(ingredient)) {
                recipe.add(ingredient);
                randomNumberOfSweets--;
            }
        }
        return recipe;
    }

    private SweetType randomSweetType() {
        Random random = new Random();
        switch (random.nextInt(5)){
            case 1:
                return SweetType.CROISSANT;
            case 2:
                return SweetType.DONUT;
            case 3:
                return SweetType.WAFFLES;
            case 4:
                return SweetType.HOMEMADE_CHOCOLATE;
            default:
                return SweetType.CAKE;
        }
    }
}
