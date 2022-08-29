package builder;

import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import domain.sweet.SweetType;
import exception.ValidationException;
import lombok.NoArgsConstructor;
import validator.SweetValidator;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class SweetBuilder {
    public Sweet build(long id, List<Ingredient> ingredientList, SweetType sweetType, double price)
            throws ValidationException {
        Sweet sweet = Sweet.builder()
                .id(id)
                .ingredientsList(ingredientList)
                .extraIngredients(new ArrayList<>())
                .sweetType(sweetType)
                .price(price)
                .build();
        SweetValidator validator = new SweetValidator();

        if (validator.isValidSweet(sweet))
            return sweet;
        else
            throw new ValidationException(validator.validateSweet(sweet).stream()
                    .reduce("", (result, error) -> result + error));
    }
}
