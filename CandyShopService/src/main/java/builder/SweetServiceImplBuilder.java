package builder;

import exception.BuildException;
import lombok.NoArgsConstructor;
import repository.ingredientRepository.IngredientRepository;
import repository.sweetRepository.SweetRepository;
import service.sweetService.SweetServiceImpl;

@NoArgsConstructor
public class SweetServiceImplBuilder {
    public SweetServiceImpl build(SweetRepository sweetRepository, IngredientRepository ingredientRepository)
            throws BuildException {
        return SweetServiceImpl.builder()
                .sweetRepository(sweetRepository)
                .ingredientRepository(ingredientRepository)
                .build();
    }
}
