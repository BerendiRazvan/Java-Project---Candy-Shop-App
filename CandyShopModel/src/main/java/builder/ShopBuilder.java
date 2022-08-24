package builder;

import domain.Shop;
import domain.location.Location;
import exception.BuildException;
import lombok.NoArgsConstructor;
import validator.ShopValidator;


@NoArgsConstructor
public class ShopBuilder {
    public Shop build(String name, Location location) throws BuildException {
        Shop shop = Shop.builder()
                .name(name)
                .location(location)
                .build();
        ShopValidator validator = new ShopValidator();

        if (validator.isValidShop(shop))
            return shop;
        else
            throw new BuildException(validator.validateShop(shop).stream()
                    .reduce("", (result, error) -> result + error));
    }
}
