package domain.sweet;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Sweet {
    private long id;
    private SweetType sweetType;
    private List<Ingredient> ingredientsList;
    private List<Ingredient> extraIngredients;
    private double price;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public Sweet(long id, List<Ingredient> ingredientsList, SweetType sweetType, double price) {
        this.id = id;
        this.sweetType = sweetType;
        this.ingredientsList = ingredientsList;
        this.extraIngredients = new ArrayList<>();
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Ingredient> getIngredientsList() {
        return ingredientsList;
    }

    public List<Ingredient> getExtraIngredients() {
        return extraIngredients;
    }

    public void setIngredientsList(List<Ingredient> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public void setExtraIngredients(List<Ingredient> extraIngredients) {
        this.extraIngredients = extraIngredients;
    }

    public SweetType getSweetType() {
        return sweetType;
    }

    public void setSweetType(SweetType sweetType) {
        this.sweetType = sweetType;
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

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "\n\n" + sweetType +
                "\nPrice: " + df.format(price) + "$ " +
                "\nRecipe:" +
                "\nIngredients: " + ingredientsList +
                "\nExtra ingredients to add: " + extraIngredients;
    }
}
