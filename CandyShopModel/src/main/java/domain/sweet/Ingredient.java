package domain.sweet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
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

    @Override
    public String toString() {
        return "(Id:" + id + ") " + name + " - " + price + "$ ";
    }
}
