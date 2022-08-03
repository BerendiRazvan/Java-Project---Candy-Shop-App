package domain;

import domain.location.Location;

public class Shop {
    private String name;
    private Location location;

    public Shop(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "\n\n" + "-".repeat(100) + "\n" +
                "\t".repeat(10) + name +
                "\n" + "-".repeat(100) + "\n" +
                "\t".repeat(5) + location +
                "\n" + "-".repeat(100) + "\n";
    }
}
