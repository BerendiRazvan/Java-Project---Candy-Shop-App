package builder;

import domain.sweet.Ingredient;
import exception.BuildException;
import lombok.NoArgsConstructor;
import validator.IngredientValidator;

@NoArgsConstructor
public class IngredientBuilder {
    public Ingredient build(long id, String name, double price, int amount) throws BuildException {
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
            throw new BuildException(validator.validateIngredient(ingredient).stream()
                    .reduce("", (result, error) -> result + error));
    }

    public Ingredient build(long id, String name, double price) throws BuildException {
        Ingredient ingredient = Ingredient.builder()
                .id(id)
                .name(name)
                .price(price)
                .amount(1)
                .build();
        IngredientValidator validator = new IngredientValidator();

        if (validator.isValidIngredient(ingredient))
            return ingredient;
        else
            throw new BuildException(validator.validateIngredient(ingredient).stream()
                    .reduce("", (result, error) -> result + error));
    }

}
