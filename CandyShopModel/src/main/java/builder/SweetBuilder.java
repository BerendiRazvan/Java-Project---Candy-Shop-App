package builder;

import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import domain.sweet.SweetType;
import exception.BuildException;
import lombok.NoArgsConstructor;
import validator.SweetValidator;

import java.util.List;

@NoArgsConstructor
public class SweetBuilder {
    public Sweet build(long id, List<Ingredient> ingredientList, SweetType sweetType, double price)
            throws BuildException {
        Sweet sweet = Sweet.builder()
                .id(id)
                .ingredientsList(ingredientList)
                .sweetType(sweetType)
                .price(price)
                .build();
        SweetValidator validator = new SweetValidator();

        if (validator.isValidSweet(sweet))
            return sweet;
        else
            throw new BuildException(validator.validateSweet(sweet).stream()
                    .reduce("", (result, error) -> result + error));
    }
}
