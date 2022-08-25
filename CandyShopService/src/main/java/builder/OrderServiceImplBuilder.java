package builder;

import exception.ValidationException;
import lombok.NoArgsConstructor;
import repository.ingredientRepository.IngredientRepository;
import repository.orderRepository.OrderRepository;
import repository.sweetRepository.SweetRepository;
import service.orderService.OrderServiceImpl;

@NoArgsConstructor
public class OrderServiceImplBuilder {
    public OrderServiceImpl build(OrderRepository orderRepository, SweetRepository sweetRepository,
                                  IngredientRepository ingredientRepository) throws ValidationException {
        return OrderServiceImpl.builder()
                .orderRepository(orderRepository)
                .sweetRepository(sweetRepository)
                .ingredientRepository(ingredientRepository)
                .build();
    }
}
