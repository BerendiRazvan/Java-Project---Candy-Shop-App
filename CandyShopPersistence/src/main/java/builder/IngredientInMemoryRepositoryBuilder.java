package builder;

import domain.sweet.Ingredient;
import exception.ValidationException;
import lombok.NoArgsConstructor;
import repository.ingredientRepository.IngredientInMemoryRepository;

import java.util.List;

@NoArgsConstructor
public class IngredientInMemoryRepositoryBuilder {
    public IngredientInMemoryRepository build(List<Ingredient> ingredientList) throws ValidationException {
        return IngredientInMemoryRepository.builder()
                .ingredientList(ingredientList)
                .build();
    }
}
