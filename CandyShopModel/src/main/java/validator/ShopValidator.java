package validator;

import domain.Shop;
import domain.location.Location;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ShopValidator {
    private static final String ONLY_LETTERS_VALIDATION = "[a-zA-Z]+";

    public String shopNameValidator(String name) {
        if (name.equals("") || !name.matches(ONLY_LETTERS_VALIDATION))
            return "Invalid name!\n";
        return "";
    }

    public String shopLocationValidator(Location location) {
        LocationValidator validator = new LocationValidator();
        if (!validator.isValidLocation(location))
            return "Invalid location!\n";
        return "";
    }

    public boolean isValidShop(Shop shop) {
        return shopValidation(shop).equals("");
    }

    public String shopValidation(Shop shop) {
        return shopNameValidator(shop.getName()) +
                shopLocationValidator(shop.getLocation());
    }
}
