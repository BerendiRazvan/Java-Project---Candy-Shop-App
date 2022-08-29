package repository.ingredientRepository;

import domain.sweet.Ingredient;
import exception.ValidationException;
import repository.Repository;

import java.util.Optional;

public interface IngredientRepository extends Repository<Long, Ingredient> {

    Optional<Ingredient> findIngredientById(Long id);

    Optional<Ingredient> findIngredientByName(String name);

}
