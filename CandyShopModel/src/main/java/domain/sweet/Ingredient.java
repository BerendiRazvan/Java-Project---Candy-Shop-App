package domain.sweet;

public class Ingredient {
    private long id;
    private String name;
    private double price;

    private int amount;

    public Ingredient(long id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = 1;
    }

    public Ingredient(long id, String name, double price, int amount) {
        this(id, name, price);
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return name + " - " + price + "$ ";
    }
}
