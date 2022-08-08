package repository.ingredientRepository;

import domain.sweet.Ingredient;
import repository.Repository;

public interface IngredientRepository extends Repository<Long, Ingredient> {

    Ingredient findIngredientById(Long id);

    Ingredient findIngredientByName(String name);

    void generateIngredients();

}
