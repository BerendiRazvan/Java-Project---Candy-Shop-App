package validator;

import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import domain.sweet.SweetType;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class SweetValidator {
    private static final double MINIMUM_PRICE_VALUE = 0;

    public String sweetTypeValidator(SweetType sweetType) {
        if (!(sweetType.equals(SweetType.CAKE) ||
                sweetType.equals(SweetType.CROISSANT) ||
                sweetType.equals(SweetType.DONUT) ||
                sweetType.equals(SweetType.HOMEMADE_CHOCOLATE) ||
                sweetType.equals(SweetType.WAFFLES) ||
                sweetType.equals(SweetType.UNIQUE)))
            return "Invalid sweet type!\n";
        return "";
    }

    public String sweetIngredientsListValidator(List<Ingredient> ingredientList) {
        for (Ingredient ingredient : ingredientList) {
            IngredientValidator validator = new IngredientValidator();
            if (!validator.isValidIngredient(ingredient))
                return "Invalid ingredients in recipe!\n";
        }
        return "";
    }

    public String sweetExtraIngredientsValidator(List<Ingredient> extraIngredients) {
        for (Ingredient ingredient : extraIngredients) {
            IngredientValidator validator = new IngredientValidator();
            if (!validator.isValidIngredient(ingredient))
                return "Invalid ingredients in extra ingredients list!\n";
        }
        return "";
    }

    public String sweetPriceValidator(double price) {
        if (price < MINIMUM_PRICE_VALUE)
            return "Invalid price!\n";
        return "";
    }

    public boolean isValidSweet(Sweet sweet) {
        return sweetValidation(sweet).equals("");
    }

    public String sweetValidation(Sweet sweet) {
        return sweetTypeValidator(sweet.getSweetType()) +
                sweetIngredientsListValidator(sweet.getIngredientsList()) +
                sweetExtraIngredientsValidator(sweet.getExtraIngredients()) +
                sweetPriceValidator(sweet.getPrice());
    }
}
