package validator;

import domain.sweet.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IngredientValidator {
    private static final String WORD_VALIDATION_REGULAR_EXPRESSION = "[a-zA-Z]+";
    private static final double MINIMUM_PRICE_VALUE = 0;
    private static final int MINIMUM_AMOUNT_VALUE = 0;

    public String validateIngredientName(String name) {
        if (name.equals("") || !name.matches(WORD_VALIDATION_REGULAR_EXPRESSION))
            return "Invalid name!\n";
        return "";
    }

    public String validateIngredientPrice(double price) {
        if (price < MINIMUM_PRICE_VALUE)
            return "Invalid price!\n";
        return "";
    }

    public String validateIngredientAmount(int amount) {
        if (amount < MINIMUM_AMOUNT_VALUE)
            return "Invalid amount!\n";
        return "";
    }

    public boolean isValidIngredient(Ingredient ingredient) {
        return validateIngredient(ingredient).isEmpty();
    }

    public List<String> validateIngredient(Ingredient ingredient) {
        if (ingredient == null) return List.of("Ingredient can not be null!");
        List<String> errors = new ArrayList<>(
                Arrays.asList(validateIngredientName(ingredient.getName()),
                        validateIngredientPrice(ingredient.getPrice()),
                        validateIngredientAmount(ingredient.getAmount())));
        errors.removeAll(Collections.singleton(""));
        return errors;
    }
}
