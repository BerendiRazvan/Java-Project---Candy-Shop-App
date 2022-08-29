package domain;

import domain.location.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Shop {
    private String name;
    private Location location;

    @Override
    public String toString() {
        return "\n\n" + "-".repeat(100) + "\n" +
                "\t".repeat(10) + name +
                "\n" + "-".repeat(100) + "\n" +
                "\t".repeat(5) + location +
                "\n" + "-".repeat(100) + "\n";
    }
}
