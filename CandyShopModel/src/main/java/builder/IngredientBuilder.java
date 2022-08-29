package builder;

import domain.sweet.Ingredient;
import exception.ValidationException;
import lombok.NoArgsConstructor;
import validator.IngredientValidator;

@NoArgsConstructor
public class IngredientBuilder {
    public Ingredient build(long id, String name, double price, int amount) throws ValidationException {
        Ingredient ingredient = Ingredient.builder()
                .id(id)
                .name(name)
                .price(price)
                .amount(amount)
                .build();
        IngredientValidator validator = new IngredientValidator();

        if (validator.isValidIngredient(ingredient))
            return ingredient;
        else
            throw new ValidationException(validator.validateIngredient(ingredient).stream()
                    .reduce("", (result, error) -> result + error));
    }
}
