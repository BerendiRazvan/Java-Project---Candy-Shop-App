package domain.sweet;


import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Sweet {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private @Getter @Setter long id;
    private @Getter @Setter SweetType sweetType;
    private @Getter @Setter List<Ingredient> ingredientsList;
    private @Getter @Setter List<Ingredient> extraIngredients;
    private @Setter double price;

    public Sweet(long id, List<Ingredient> ingredientsList, SweetType sweetType, double price) {
        this.id = id;
        this.sweetType = sweetType;
        this.ingredientsList = ingredientsList;
        this.extraIngredients = new ArrayList<>();
        this.price = price;
    }

    public double getPrice() {
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
