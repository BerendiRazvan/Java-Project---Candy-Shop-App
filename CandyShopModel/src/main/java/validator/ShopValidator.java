package validator;

import domain.Shop;
import domain.location.Location;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class ShopValidator {
    private static final String SHOP_NAME_VALIDATION_REGULAR_EXPRESSION = "[a-zA-Z ]+";

    public String validateShopName(String name) {
        if (name.equals("") || !name.matches(SHOP_NAME_VALIDATION_REGULAR_EXPRESSION))
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
        return validateShop(shop).isEmpty();
    }

    public List<String> validateShop(Shop shop) {
        if (shop == null) return List.of("Shop can not be null!");
        List<String> errors = new ArrayList<>(
                Arrays.asList(validateShopName(shop.getName()),
                        validateShopLocation(shop.getLocation())));
        errors.removeAll(Collections.singleton(""));
        return errors;
    }
}
