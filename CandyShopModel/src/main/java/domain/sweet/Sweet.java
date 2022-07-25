package domain.sweet;


public class Sweet {
    private long idSweet;
    private Recipe sweetRecipe;
    private final SweetTypes sweetType;
    private double price;

    public Sweet(long idSweet, Recipe sweetRecipe, SweetTypes sweetType, double price) {
        this.idSweet = idSweet;
        this.sweetRecipe = sweetRecipe;
        this.sweetType = sweetType;
        this.price = price;
    }

    public long getIdSweet() {
        return idSweet;
    }

    public void setIdSweet(long idSweet) {
        this.idSweet = idSweet;
    }

    public Recipe getSweetRecipe() {
        return sweetRecipe;
    }

    public void setSweetRecipe(Recipe sweetRecipe) {
        this.sweetRecipe = sweetRecipe;
    }

    public SweetTypes getSweetType() {
        return sweetType;
    }

    public double getPrice() {
        return getOriginalPrice() + getExtraPrice();
    }

    public double getOriginalPrice() {
        return price;
    }

    public double getExtraPrice() {
        return sweetRecipe.getExtraIngredients()
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
                "\nPrice: " + price + "$ " +
                "\n" + sweetRecipe;
    }
}
