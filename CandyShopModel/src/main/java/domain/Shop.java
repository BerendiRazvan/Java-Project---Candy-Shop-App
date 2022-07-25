package domain;

import domain.location.Location;

public class Shop {
    private String shopName;
    private Location shopLocation;

    public Shop(String shopName, Location shopLocation) {
        this.shopName = shopName;
        this.shopLocation = shopLocation;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Location getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(Location shopLocation) {
        this.shopLocation = shopLocation;
    }

    @Override
    public String toString() {
        return "\n\n" + "-".repeat(100) + "\n" +
                "\t".repeat(10) + shopName +
                "\n" + "-".repeat(100) + "\n" +
                "\t".repeat(5) + shopLocation +
                "\n" + "-".repeat(100) + "\n";
    }
}
