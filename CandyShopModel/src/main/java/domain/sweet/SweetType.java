package domain.sweet;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SweetType {
    CAKE("Cake"), WAFFLES("Waffles"), CROISSANT("Croissant"),
    DONUT("Donut"), HOMEMADE_CHOCOLATE("Homemade Chocolate"),
    UNIQUE("Custom sweet");

    private @Getter final String name;
}
