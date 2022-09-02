package builder;

import domain.Shop;
import domain.location.Location;
import exception.ValidationException;
import lombok.NoArgsConstructor;
import validator.ShopValidator;


@NoArgsConstructor
public class ShopBuilder {
    public Shop build(long id, String name, Location location) throws ValidationException {
        Shop shop = Shop.builder()
                .id(id)
                .name(name)
                .location(location)
                .build();
        ShopValidator validator = new ShopValidator();

        if (validator.isValidShop(shop))
            return shop;
        else
            throw new ValidationException(validator.validateShop(shop).stream()
                    .reduce("", (result, error) -> result + error));
    }
}
