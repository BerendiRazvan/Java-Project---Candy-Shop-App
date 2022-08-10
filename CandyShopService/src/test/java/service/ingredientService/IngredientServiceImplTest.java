package service.ingredientService;

import domain.sweet.Ingredient;
import org.junit.jupiter.api.*;
import repository.ingredientRepository.IngredientInMemoryRepository;
import repository.ingredientRepository.IngredientRepository;
import service.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static service.UtilsConstantValues.*;


class IngredientServiceImplTest {

    private IngredientService ingredientService;
    private final Ingredient ingredient = new Ingredient(ID, INGREDIENT_NAME, INGREDIENT_PRICE, AMOUNT);

    @BeforeAll
    static void setUpAll() {
        System.out.println("Tests for IngredientServiceImpl");
    }

    @BeforeEach
    void setUp() {
        IngredientRepository ingredientRepository = new IngredientInMemoryRepository(new ArrayList<>());
        ingredientRepository.generateIngredients();
        ingredientService = new IngredientServiceImpl(ingredientRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Tests passed");
    }

    @Test
    void testGetAllIngredients() {
        List<Ingredient> list = ingredientService.getAllIngredients();
        assertEquals(list.size(), 15);
        for (int i = 1; i < list.size(); i++)
            assertEquals(list.get(i - 1).getId(), i);
    }

    @Test
    void testValidFindIngredientById() throws ServiceException {
        Ingredient ingredientById = ingredientService.findIngredientById(String.valueOf(ID));
        assertEquals(ingredientById.getId(), ID);
        assertEquals(ingredientById.getAmount(), AMOUNT);
        assertEquals(ingredientById.getPrice(), INGREDIENT_PRICE);
        assertEquals(ingredientById.getName(), INGREDIENT_NAME);
    }

    @Test
    void testInvalidFindIngredientById() throws ServiceException {
        assertThrowsExactly(ServiceException.class,
                () -> ingredientService.findIngredientById("dajdnas"),
                INGREDIENT_ID_EXCEPTION);
        assertNull(ingredientService.findIngredientById("123456"));
    }

    @Test
    void testShowAllIngredientsInStock() {
        String notificationInfo = "(reduced quantity in shop stock)";
        List<String> list = ingredientService.showAllIngredientsInStock();
        assertEquals(list.size(), 15);
        for (String s : list) {
            List<String> ingredientAsString = List.of(s.split("\t"));
            if (Integer.parseInt(ingredientAsString.get(2).replaceAll("[a-zA-z :]", "")) <= 10)
                assertEquals(ingredientAsString.get(3), notificationInfo);
        }
    }
}
