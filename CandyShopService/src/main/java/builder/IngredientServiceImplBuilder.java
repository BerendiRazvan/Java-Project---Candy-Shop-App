package builder;

import exception.BuildException;
import lombok.NoArgsConstructor;
import repository.ingredientRepository.IngredientRepository;
import service.ingredientService.IngredientServiceImpl;

@NoArgsConstructor
public class IngredientServiceImplBuilder {
    public IngredientServiceImpl build(IngredientRepository ingredientRepository) throws BuildException {
        return IngredientServiceImpl.builder()
                .ingredientRepository(ingredientRepository)
                .build();
    }
}
