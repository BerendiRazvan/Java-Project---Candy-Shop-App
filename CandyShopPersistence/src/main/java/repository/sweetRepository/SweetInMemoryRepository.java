package repository.sweetRepository;


import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import domain.sweet.SweetType;
import exception.RepositoryException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import repository.ingredientRepository.IngredientRepository;

import java.util.*;

@Builder
@AllArgsConstructor
public class SweetInMemoryRepository implements SweetRepository {

    private static final int SWEETS_TO_GENERATE = 15;
    private List<Sweet> sweetList;

    @Override
    public void add(Sweet sweet) throws RepositoryException {
        if (!sweetList.contains(sweet))
            sweetList.add(sweet);
        else
            throw new RepositoryException("This element already exists!");
    }

    @Override
    public void update(Long id, Sweet sweet) throws RepositoryException {
        Optional<Sweet> sweetToUpdate = findSweetById(id);
        if (sweetToUpdate.isPresent())
            sweetList.set(sweetList.indexOf(sweetToUpdate.get()), sweet);
        else
            throw new RepositoryException("This element does not exist!");
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        Optional<Sweet> sweetToDelete = findSweetById(id);
        if (sweetToDelete.isPresent())
            sweetList.remove(sweetToDelete.get());
        else
            throw new RepositoryException("This element does not exist!");
    }

    @Override
    public List<Sweet> findAll() {
        return sweetList;
    }

    @Override
    public Optional<Sweet> findSweetById(Long id) {
        return sweetList.stream()
                .filter(sweet -> id == sweet.getId())
                .findFirst();
    }

    @Override
    public void generateSweets(IngredientRepository ingredientRepository) {
        List<Ingredient> ingredientList = ingredientRepository.findAll();
        int noOfSweets = SWEETS_TO_GENERATE;
        while (noOfSweets != 0) {
            Optional<Long> id = generateSweetId();
            Optional<SweetType> sweetType = randomSweetType();
            if (id.isPresent()&&sweetType.isPresent()) {
                sweetList.add(Sweet.builder()
                        .id(id.get())
                        .ingredientsList(randomRecipe(ingredientList))
                        .sweetType(sweetType.get())
                        .price(0)
                        .build());
                noOfSweets--;
            } else throw new RuntimeException("Error: generateSweetId");
        }
        sweetList.forEach(sweet -> sweet.setPrice(generatePrice(sweet.getIngredientsList())));
    }

    @Override
    public Optional<Long> generateSweetId() {
        long id = 1;
        while (true) {
            boolean ok = true;
            for (var s : sweetList)
                if (s.getId() == id) {
                    ok = false;
                    break;
                }

            if (ok) return Optional.of(id);
            id++;
        }
    }

    private static double generatePrice(List<Ingredient> list) {
        Random random = new Random();
        int extraPriceAddedBySeller = random.nextInt(5);
        return extraPriceAddedBySeller +
                list.stream()
                        .mapToDouble(Ingredient::getPrice)
                        .sum();
    }

    private static List<Ingredient> randomRecipe(List<Ingredient> ingredientList) {
        Random random = new Random();
        int randomNumberOfSweets = random.nextInt(7 - 1) + 2;

        List<Ingredient> recipe = new ArrayList<>();

        while (randomNumberOfSweets > 0) {
            Ingredient ingredient = ingredientList.get(random.nextInt(ingredientList.size()));
            if (!recipe.contains(ingredient)) {
                recipe.add(ingredient);
                randomNumberOfSweets--;
            }
        }
        return recipe;
    }

    private Optional<SweetType> randomSweetType() {
        Random random = new Random();
        switch (random.nextInt(5)) {
            case 1:
                return Optional.of(SweetType.CROISSANT);
            case 2:
                return Optional.of(SweetType.DONUT);
            case 3:
                return Optional.of(SweetType.WAFFLES);
            case 4:
                return Optional.of(SweetType.HOMEMADE_CHOCOLATE);
            default:
                return Optional.of(SweetType.CAKE);
        }
    }
}
