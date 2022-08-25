package builder;

import domain.Shop;
import exception.ValidationException;
import lombok.NoArgsConstructor;
import service.customerService.CustomerService;
import service.ingredientService.IngredientService;
import service.orderService.OrderService;
import service.sweetService.SweetService;
import ui.UI;

@NoArgsConstructor
public class UIBuilder {
    public UI build(Shop shop, CustomerService customerService, SweetService sweetService, OrderService
            orderService, IngredientService ingredientService) throws ValidationException {
        return UI.builder()
                .shop(shop)
                .customerService(customerService)
                .sweetService(sweetService)
                .orderService(orderService)
                .ingredientService(ingredientService)
                .build();
    }
}
