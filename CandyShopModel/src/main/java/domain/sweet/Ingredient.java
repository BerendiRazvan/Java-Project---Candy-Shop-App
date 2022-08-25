package domain.sweet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class Ingredient {
    private long id;
    private String name;
    private double price;
    private int amount;

    @Override
    public String toString() {
        return "(Id:" + id + ") " + name + " - " + price + "$ ";
    }
}
