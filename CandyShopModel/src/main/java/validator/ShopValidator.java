package validator;

import domain.Shop;
import domain.location.Location;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ShopValidator {
    private static final String WORD_VALIDATION_REGULAR_EXPRESSION = "[a-zA-Z]+";

    public String validateShopName(String name) {
        if (name.equals("") || !name.matches(WORD_VALIDATION_REGULAR_EXPRESSION))
            return "Invalid name!\n";
        return "";
    }

    public String validateShopLocation(Location location) {
        LocationValidator validator = new LocationValidator();
        if (!validator.isValidLocation(location))
            return "Invalid location!\n";
        return "";
    }

    public boolean isValidShop(Shop shop) {
        return validateShop(shop).equals("");
    }

    public String validateShop(Shop shop) {
        if (shop == null) return "Shop can not be null!";
        return validateShopName(shop.getName()) +
                validateShopLocation(shop.getLocation());
    }
}
