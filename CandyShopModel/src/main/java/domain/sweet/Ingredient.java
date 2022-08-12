package domain.sweet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Ingredient {
    private @Getter @Setter long id;
    private @Getter @Setter String name;
    private @Getter @Setter double price;
    private @Getter @Setter int amount;

    public Ingredient(long id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = 1;
    }

    @Override
    public String toString() {
        return "(Id:" + id + ") " + name + " - " + price + "$ ";
    }
}
