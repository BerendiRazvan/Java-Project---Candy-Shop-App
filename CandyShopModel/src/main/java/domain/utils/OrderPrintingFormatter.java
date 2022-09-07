package domain.utils;

import domain.sweet.Ingredient;
import domain.sweet.Sweet;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderPrintingFormatter {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static String formatForPrintingOrderedSweets(Map<Sweet, Integer> orderedSweets) {
        StringBuilder orderedSweetsInfo = new StringBuilder();
        for (Sweet sweet : orderedSweets.keySet())
            orderedSweetsInfo.append(formatForPrintingSweet(sweet, orderedSweets));
        return orderedSweetsInfo.toString();
    }

    private static String formatForPrintingSweet(Sweet sweet, Map<Sweet, Integer> orderedSweets) {
        return "\n(Id:" + sweet.getId() + ")" +
                " " + sweet.getSweetType().getName() +
                " - quantity: " +
                orderedSweets.get(sweet) +
                " - price: " +
                orderedSweets.get(sweet) +
                "*" +
                df.format(sweet.getTotalPrice()) +
                "$=" +
                df.format(orderedSweets.get(sweet) * sweet.getTotalPrice()) +
                "$" +
                "\nRecipe:" +
                formatForPrintingIngredients(sweet.getIngredientsList()) +
                "\nExtra:" +
                sweet.getExtraIngredients() +
                "\n";
    }

    private static String formatForPrintingIngredients(List<Ingredient> ingredientsList) {
        return ingredientsList
                .stream()
                .map(ingredient -> "(Id:" + ingredient.getId() + ") " + ingredient.getName())
                .collect(Collectors.toList())
                .toString();
    }
}
