package validator;

import domain.sweet.Ingredient;

public class IngredientValidator {
    private static final String ONLY_LETTERS_VALIDATION = "[a-zA-Z]+";
    private static final double MINIMUM_PRICE_VALUE = 0;
    private static final int MINIMUM_AMOUNT_VALUE = 0;

    public String ingredientNameValidator(String name) {
        if (name.equals("") || !name.matches(ONLY_LETTERS_VALIDATION))
            return "Invalid name!\n";
        return "";
    }

    public String ingredientPriceValidator(double price) {
        if (price < MINIMUM_PRICE_VALUE)
            return "Invalid price!\n";
        return "";
    }

    public String ingredientAmountValidator(int amount) {
        if (amount < MINIMUM_AMOUNT_VALUE)
            return "Invalid amount!\n";
        return "";
    }

    public boolean isValidIngredient(Ingredient ingredient) {
        return ingredientValidation(ingredient).equals("");
    }

    public String ingredientValidation(Ingredient ingredient) {
        if (ingredient == null) return "Ingredient can not be null!";
        return ingredientNameValidator(ingredient.getName()) +
                ingredientPriceValidator(ingredient.getPrice()) +
                ingredientAmountValidator(ingredient.getAmount());
    }
}
