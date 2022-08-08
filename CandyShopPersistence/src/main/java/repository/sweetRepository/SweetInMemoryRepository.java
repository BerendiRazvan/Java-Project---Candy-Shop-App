package repository.sweetRepository;

import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import domain.sweet.SweetType;
import repository.exception.RepositoryException;
import repository.ingredientRepository.IngredientRepository;

import java.util.*;

public class SweetInMemoryRepository implements SweetRepository {

    private List<Sweet> sweetList;

    public SweetInMemoryRepository(List<Sweet> sweetList) {
        this.sweetList = sweetList;
    }

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
        sweetList.addAll(Arrays.asList(
                new Sweet(1, randomRecipe(ingredientRepository.findAll()), SweetType.DONUT, 5),
                new Sweet(2, randomRecipe(ingredientRepository.findAll()), SweetType.DONUT, 5.5),
                new Sweet(3, randomRecipe(ingredientRepository.findAll()), SweetType.CAKE, 23.55),
                new Sweet(4, randomRecipe(ingredientRepository.findAll()), SweetType.CROISSANT, 3.99),
                new Sweet(5, randomRecipe(ingredientRepository.findAll()), SweetType.WAFFLES, 4.99),
                new Sweet(6, randomRecipe(ingredientRepository.findAll()), SweetType.CROISSANT, 3.39),
                new Sweet(7, randomRecipe(ingredientRepository.findAll()), SweetType.HOMEMADE_CHOCOLATE, 13.39),
                new Sweet(8, randomRecipe(ingredientRepository.findAll()), SweetType.DONUT, 3.25),
                new Sweet(9, randomRecipe(ingredientRepository.findAll()), SweetType.CAKE, 49.99),
                new Sweet(10, randomRecipe(ingredientRepository.findAll()), SweetType.HOMEMADE_CHOCOLATE, 3.99),
                new Sweet(11, randomRecipe(ingredientRepository.findAll()), SweetType.CROISSANT, 1.99),
                new Sweet(12, randomRecipe(ingredientRepository.findAll()), SweetType.DONUT, 2.11),
                new Sweet(13, randomRecipe(ingredientRepository.findAll()), SweetType.WAFFLES, 3.75),
                new Sweet(14, randomRecipe(ingredientRepository.findAll()), SweetType.WAFFLES, 1.99),
                new Sweet(15, randomRecipe(ingredientRepository.findAll()), SweetType.CROISSANT, 0.99)
        ));
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

}
