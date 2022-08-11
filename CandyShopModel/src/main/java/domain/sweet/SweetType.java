package domain.sweet;

public enum SweetType {
    CAKE("Cake"), WAFFLES("Waffles"), CROISSANT("Croissant"), DONUT("Donut"), HOMEMADE_CHOCOLATE("Homemade Chocolate"),
    UNIQUE("Custom sweet");

    private final String name;

    SweetType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
