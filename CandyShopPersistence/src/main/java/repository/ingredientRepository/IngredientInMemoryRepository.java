package repository.ingredientRepository;

import domain.sweet.Ingredient;
import repository.exception.RepositoryException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IngredientInMemoryRepository implements IngredientRepository {
    List<Ingredient> ingredientList;

    public IngredientInMemoryRepository(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @Override
    public void add(Ingredient ingredient) throws RepositoryException {
        if (!ingredientList.contains(ingredient))
            ingredientList.add(ingredient);
        else
            throw new RepositoryException("This element already exists!");
    }

    @Override
    public void update(Long id, Ingredient ingredient) throws RepositoryException {
        Ingredient ingredientToUpdate = findIngredientById(id);
        if (ingredientToUpdate == null)
            throw new RepositoryException("This element does not exist!");
        else
            ingredientList.set(ingredientList.indexOf(ingredientToUpdate), ingredient);
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        Ingredient ingredientToRemove = findIngredientById(id);
        if (ingredientToRemove == null)
            throw new RepositoryException("This element does not exist!");
        else
            ingredientList.remove(ingredientToRemove);
    }

    @Override
    public List<Ingredient> findAll() {
        return ingredientList;
    }

    @Override
    public Ingredient findIngredientById(Long id) {
        for (Ingredient ingredient : ingredientList)
            if (ingredient.getId() == id) return ingredient;
        return null;
    }

    @Override
    public Ingredient findIngredientByName(String name) {
        for (Ingredient ingredient : ingredientList)
            if (ingredient.getName().equalsIgnoreCase(name)) return ingredient;
        return null;
    }

    @Override
    public void generateIngredients() {
        ingredientList.addAll(Arrays.asList(
                new Ingredient(1, "Sugar", 1.5, 23),
                new Ingredient(2, "Milk", 0.5, 20),
                new Ingredient(3, "Flour", 0.42, 30),
                new Ingredient(4, "Chocolate", 1.75, 15),
                new Ingredient(5, "Honey", 2.05, 7),
                new Ingredient(6, "Caramel", 1.3, 17),
                new Ingredient(7, "Whipped cream", 1, 40),
                new Ingredient(8, "Vanilla", 0.73, 8),
                new Ingredient(9, "Strawberries", 2.51, 14),
                new Ingredient(10, "Cacao", 0.25, 5),
                new Ingredient(11, "Peanut butter", 2.22, 7),
                new Ingredient(12, "Coconut", 2.01, 20),
                new Ingredient(13, "Cherry", 3.17, 12),
                new Ingredient(14, "Oreo", 2.35, 32),
                new Ingredient(15, "Ice Cream", 2.05, 10)
        ));
    }
}
