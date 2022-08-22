package service.ingredientService;

import domain.sweet.Ingredient;
import exception.ServiceException;
import org.junit.jupiter.api.*;
import repository.ingredientRepository.IngredientInMemoryRepository;
import repository.ingredientRepository.IngredientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static service.ConstantValues.*;


class IngredientServiceImplTest {

    private static final String CHARACTERS_TO_DELETE = "[a-zA-z :]";
    private IngredientService ingredientService;

    @BeforeAll
    static void setUpAll() {
        System.out.println("Tests for IngredientServiceImpl");
    }

    @BeforeEach
    void setUp() {
        IngredientRepository ingredientRepository = IngredientInMemoryRepository.builder()
                .ingredientList(new ArrayList<>())
                .build();
        ingredientRepository.generateIngredients();

        ingredientService = IngredientServiceImpl.builder()
                .ingredientRepository(ingredientRepository)
                .build();
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
        Optional<Ingredient> ingredientById = ingredientService.findIngredientById(String.valueOf(ID));
        if(ingredientById.isPresent()) {
            assertEquals(ingredientById.get().getId(), ID);
            assertEquals(ingredientById.get().getAmount(), AMOUNT);
            assertEquals(ingredientById.get().getPrice(), INGREDIENT_PRICE);
            assertEquals(ingredientById.get().getName(), INGREDIENT_NAME);
        }else fail("Ingredient findIngredientById failed");
    }

    @Test
    void testInvalidFindIngredientById() throws ServiceException {
        assertThrowsExactly(ServiceException.class,
                () -> ingredientService.findIngredientById("dajdnas"),
                INGREDIENT_ID_EXCEPTION);
        assertThrowsExactly(ServiceException.class,
                () -> ingredientService.findIngredientById("123456"),
                INGREDIENT_ID_EXCEPTION);
    }

    @Test
    void testShowAllIngredientsInStock() {
        String notificationInfo = "(reduced quantity in shop stock)";
        List<String> list = ingredientService.showAllIngredientsInStock();
        assertEquals(list.size(), 15);
        for (String s : list) {
            List<String> ingredientAsString = List.of(s.split("\t"));
            if (Integer.parseInt(ingredientAsString.get(2).replaceAll(CHARACTERS_TO_DELETE, "")) <= 10)
                assertEquals(ingredientAsString.get(3), notificationInfo);
        }
    }
}
