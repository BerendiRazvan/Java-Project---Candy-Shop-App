package validator;

import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import domain.sweet.SweetType;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class SweetValidator {
    private static final double MINIMUM_PRICE_VALUE = 0;

    public String validateSweetType(SweetType sweetType) {
        if (!(sweetType.equals(SweetType.CAKE) ||
                sweetType.equals(SweetType.CROISSANT) ||
                sweetType.equals(SweetType.DONUT) ||
                sweetType.equals(SweetType.HOMEMADE_CHOCOLATE) ||
                sweetType.equals(SweetType.WAFFLES) ||
                sweetType.equals(SweetType.UNIQUE)))
            return "Invalid sweet type!\n";
        return "";
    }

    public String validateSweetIngredientsList(List<Ingredient> ingredientList) {
        for (Ingredient ingredient : ingredientList) {
            IngredientValidator validator = new IngredientValidator();
            if (!validator.isValidIngredient(ingredient))
                return "Invalid ingredients in recipe!\n";
        }
        return "";
    }

    public String validateSweetExtraIngredients(List<Ingredient> extraIngredients) {
        for (Ingredient ingredient : extraIngredients) {
            IngredientValidator validator = new IngredientValidator();
            if (!validator.isValidIngredient(ingredient))
                return "Invalid ingredients in extra ingredients list!\n";
        }
        return "";
    }

    public String validateSweetPrice(double price) {
        if (price < MINIMUM_PRICE_VALUE)
            return "Invalid price!\n";
        return "";
    }

    public boolean isValidSweet(Sweet sweet) {
        return validateSweet(sweet).isEmpty();
    }

    public List<String> validateSweet(Sweet sweet) {
        if (sweet == null) return List.of("Sweet can not be null!");
        List<String> errors = new ArrayList<>(
                Arrays.asList(validateSweetType(sweet.getSweetType()),
                        validateSweetIngredientsList(sweet.getIngredientsList()),
                        validateSweetExtraIngredients(sweet.getExtraIngredients()),
                        validateSweetPrice(sweet.getPrice())));
        errors.removeAll(Collections.singleton(""));
        return errors;
    }
}
