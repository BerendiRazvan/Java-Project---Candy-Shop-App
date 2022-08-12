package domain;

import domain.location.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Shop {
    private @Getter @Setter String name;
    private @Getter @Setter Location location;

    @Override
    public String toString() {
        return "\n\n" + "-".repeat(100) + "\n" +
                "\t".repeat(10) + name +
                "\n" + "-".repeat(100) + "\n" +
                "\t".repeat(5) + location +
                "\n" + "-".repeat(100) + "\n";
    }
}
