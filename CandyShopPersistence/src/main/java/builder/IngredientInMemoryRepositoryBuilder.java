package builder;

import domain.sweet.Ingredient;
import exception.BuildException;
import lombok.NoArgsConstructor;
import repository.ingredientRepository.IngredientInMemoryRepository;

import java.util.List;

@NoArgsConstructor
public class IngredientInMemoryRepositoryBuilder {
    public IngredientInMemoryRepository build(List<Ingredient> ingredientList) throws BuildException {
        return IngredientInMemoryRepository.builder()
                .ingredientList(ingredientList)
                .build();
    }
}
