package validator;

import domain.sweet.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientValidator {
    private static final String WORD_VALIDATION_REGULAR_EXPRESSION = "[a-zA-Z ]+";
    private static final double MINIMUM_PRICE_VALUE = 0;
    private static final int MINIMUM_AMOUNT_VALUE = 0;

    public boolean isValidIngredient(Ingredient ingredient) {
        return validateIngredient(ingredient).isEmpty();
    }

    public List<String> validateIngredient(Ingredient ingredient) {
        if (ingredient == null) return List.of("Ingredient can not be null!");
        List<String> errors = new ArrayList<>();

        String error = validateIngredientName(ingredient.getName());
        if (!error.matches(""))
            errors.add(error);

        error = validateIngredientPrice(ingredient.getPrice());
        if (!error.matches(""))
            errors.add(error);

        error = validateIngredientAmount(ingredient.getAmount());
        if (!error.matches(""))
            errors.add(error);


        return errors;
    }

    private String validateIngredientName(String name) {
        if (name.equals("") || !name.matches(WORD_VALIDATION_REGULAR_EXPRESSION))
            return "Invalid name!\n";
        return "";
    }

    private String validateIngredientPrice(double price) {
        if (price < MINIMUM_PRICE_VALUE)
            return "Invalid price!\n";
        return "";
    }

    private String validateIngredientAmount(int amount) {
        if (amount < MINIMUM_AMOUNT_VALUE)
            return "Invalid amount!\n";
        return "";
    }
}
