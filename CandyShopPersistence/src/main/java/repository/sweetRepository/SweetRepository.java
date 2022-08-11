package repository.sweetRepository;


import domain.sweet.Sweet;
import repository.Repository;
import repository.ingredientRepository.IngredientRepository;

public interface SweetRepository extends Repository<Long, Sweet> {
    Sweet findSweetById(Long id);

    void generateSweets(IngredientRepository ingredientRepository);

    long generateSweetId();
}
