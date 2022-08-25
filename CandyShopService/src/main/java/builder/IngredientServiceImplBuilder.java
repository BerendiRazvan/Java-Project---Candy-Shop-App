package builder;

import exception.ValidationException;
import lombok.NoArgsConstructor;
import repository.ingredientRepository.IngredientRepository;
import service.ingredientService.IngredientServiceImpl;

@NoArgsConstructor
public class IngredientServiceImplBuilder {
    public IngredientServiceImpl build(IngredientRepository ingredientRepository) throws ValidationException {
        return IngredientServiceImpl.builder()
                .ingredientRepository(ingredientRepository)
                .build();
    }
}
