package repository.ingredientRepository;

import domain.sweet.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import repository.exception.RepositoryException;

import java.util.Arrays;
import java.util.List;

@Builder
@AllArgsConstructor
public class IngredientInMemoryRepository implements IngredientRepository {
    private List<Ingredient> ingredientList;

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
                Ingredient.builder().id(1).name("Sugar").price(1.5).amount(23).build(),
                Ingredient.builder().id(2).name("Milk").price(0.5).amount(21).build(),
                Ingredient.builder().id(3).name("Flour").price(0.42).amount(30).build(),
                Ingredient.builder().id(4).name("Chocolate").price(1.75).amount(25).build(),
                Ingredient.builder().id(5).name("Honey").price(2.05).amount(7).build(),
                Ingredient.builder().id(6).name("Caramel").price(1.3).amount(17).build(),
                Ingredient.builder().id(7).name("Whipped cream").price(1).amount(40).build(),
                Ingredient.builder().id(8).name("Vanilla").price(0.73).amount(8).build(),
                Ingredient.builder().id(9).name("Strawberries").price(2.51).amount(14).build(),
                Ingredient.builder().id(10).name("Cacao").price(0.25).amount(5).build(),
                Ingredient.builder().id(11).name("Peanut butter").price(2.22).amount(7).build(),
                Ingredient.builder().id(12).name("Coconut").price(2.01).amount(20).build(),
                Ingredient.builder().id(13).name("Cherry").price(3.17).amount(12).build(),
                Ingredient.builder().id(14).name("Oreo").price(2.35).amount(32).build(),
                Ingredient.builder().id(15).name("Ice Cream").price(2.05).amount(10).build()
        ));
    }
}
