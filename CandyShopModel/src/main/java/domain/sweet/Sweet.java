package domain.sweet;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.util.List;

@Builder
@Getter
@Setter
public class Sweet {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private long id;
    private SweetType sweetType;
    private List<Ingredient> ingredientsList;
    private List<Ingredient> extraIngredients;
    private double price;

    public double getTotalPrice() {
        return getOriginalPrice() + getExtraPrice();
    }

    public double getOriginalPrice() {
        return price;
    }

    public double getExtraPrice() {
        return getExtraIngredients()
                .stream()
                .mapToDouble(Ingredient::getPrice)
                .sum();
    }

    @Override
    public String toString() {
        return "\n\n" + sweetType.getName() +
                "\nPrice: " + df.format(price) + "$ " +
                "\nRecipe:" +
                "\nIngredients: " + ingredientsList +
                "\nExtra ingredients to add: " + extraIngredients;
    }
}
