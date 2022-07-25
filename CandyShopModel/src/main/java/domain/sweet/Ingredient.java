package domain.sweet;

public class Ingredient {
    private long idIngredient;
    private String name;
    private double price;

    public Ingredient(long idIngredient, String name, double price) {
        this.idIngredient = idIngredient;
        this.name = name;
        this.price = price;
    }

    public long getIdIngredient() {
        return idIngredient;
    }

    public void setIdIngredient(long idIngredient) {
        this.idIngredient = idIngredient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    @Override
    public String toString() {
        return name + " - " + price + "$ ";
    }
}
