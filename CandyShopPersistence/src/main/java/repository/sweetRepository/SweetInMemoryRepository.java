package repository.sweetRepository;


import builder.SweetBuilder;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import domain.sweet.SweetType;
import exception.ValidationException;
import exception.RepositoryException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.ingredientRepository.IngredientRepository;

import java.util.*;

@Builder
@AllArgsConstructor
public class SweetInMemoryRepository implements SweetRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(SweetInMemoryRepository.class);

    private static final int SWEETS_TO_GENERATE = 15;
    private List<Sweet> sweetList;

    @Override
    public void add(Sweet sweet) throws RepositoryException {
        LOGGER.info("Add sweet - started");
        if (!sweetList.contains(sweet)) {
            sweetList.add(sweet);
            LOGGER.info("Add sweet - finished");
        } else {
            LOGGER.warn("Add sweet - exception occurred -> {}", "This element already exists!");
            throw new RepositoryException("This element already exists!");
        }
    }

    @Override
    public void update(Long id, Sweet sweet) throws RepositoryException {
        LOGGER.info("Update sweet with id = {} - started", id);
        Optional<Sweet> sweetToUpdate = findSweetById(id);
        if (sweetToUpdate.isPresent()) {
            sweetList.set(sweetList.indexOf(sweetToUpdate.get()), sweet);
            LOGGER.info("Update sweet with id = {} - finished", id);
        } else {
            LOGGER.warn("Update sweet with id = {} - exception occurred -> {}", id,
                    "This element does not exist!");
            throw new RepositoryException("This element does not exist!");
        }
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        LOGGER.info("Delete sweet with id = {} - started", id);
        Optional<Sweet> sweetToDelete = findSweetById(id);
        if (sweetToDelete.isPresent()) {
            sweetList.remove(sweetToDelete.get());
            LOGGER.info("Delete sweet with id = {} - finished", id);
        } else {
            LOGGER.warn("Delete sweet with id = {} - exception occurred -> {}", id, "This element does not exist!");
            throw new RepositoryException("This element does not exist!");
        }
    }

    @Override
    public List<Sweet> findAll() {
        LOGGER.info("FindAll sweets - called");
        return sweetList;
    }

    @Override
    public Optional<Sweet> findSweetById(Long id) {
        LOGGER.info("FindSweetById for sweet with id = {} - called", id);
        return sweetList.stream()
                .filter(sweet -> id == sweet.getId())
                .findFirst();
    }

    @Override
    public Optional<Long> generateSweetId() {
        LOGGER.info("GenerateSweetId - started");

        long id = 1;
        while (true) {
            boolean ok = true;
            for (var s : sweetList)
                if (s.getId() == id) {
                    ok = false;
                    break;
                }

            if (ok) {
                LOGGER.info("GenerateSweetId - finished");
                return Optional.of(id);
            }
            id++;
        }
    }

    @Override
    public void generateSweets(IngredientRepository ingredientRepository) throws ValidationException {
        LOGGER.info("GenerateSweets - started");
        SweetBuilder sweetBuilder = new SweetBuilder();
        List<Ingredient> ingredientList = ingredientRepository.findAll();
        int noOfSweets = SWEETS_TO_GENERATE;
        while (noOfSweets != 0) {
            Optional<Long> id = generateSweetId();
            Optional<SweetType> sweetType = randomSweetType();
            if (id.isPresent() && sweetType.isPresent()) {
                sweetList.add(sweetBuilder.build(id.get(), randomRecipe(ingredientList), sweetType.get(), 0));
                noOfSweets--;
            } else {
                LOGGER.warn("Error: generateSweetId");
                throw new RuntimeException("Error: generateSweetId");
            }
        }
        sweetList.forEach(sweet -> sweet.setPrice(generatePrice(sweet.getIngredientsList())));
        LOGGER.info("GenerateSweets - finished");
    }


    private static double generatePrice(List<Ingredient> list) {
        LOGGER.info("GeneratePrice - called");
        Random random = new Random();
        int extraPriceAddedBySeller = random.nextInt(5);
        return extraPriceAddedBySeller +
                list.stream()
                        .mapToDouble(Ingredient::getPrice)
                        .sum();
    }

    private static List<Ingredient> randomRecipe(List<Ingredient> ingredientList) {
        LOGGER.info("RandomRecipe - called");
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
        LOGGER.info("RandomSweetType - called");
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
