package service.sweetService;

import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import domain.sweet.SweetType;
import org.junit.jupiter.api.*;
import repository.exception.RepositoryException;
import repository.ingredientRepository.IngredientInMemoryRepository;
import repository.ingredientRepository.IngredientRepository;
import repository.sweetRepository.SweetInMemoryRepository;
import repository.sweetRepository.SweetRepository;
import service.exception.ServiceException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static service.ConstantValues.*;

class SweetServiceImplTest {
    private SweetService sweetService;
    private Ingredient ingredient;

    @BeforeAll
    static void setUpAll() {
        System.out.println("Tests for SweetServiceImpl");
    }

    @BeforeEach
    void setUp() {
        ingredient = new Ingredient(ID, INGREDIENT_NAME, INGREDIENT_PRICE, AMOUNT);

        SweetRepository sweetRepository =
                new SweetInMemoryRepository(new ArrayList<>());

        try {
            sweetRepository.add(new Sweet(1,
                    Arrays.asList(
                            new Ingredient(1, "Sugar", 1.5),
                            new Ingredient(2, "Milk", 1),
                            new Ingredient(3, "Flour", 0.75)),
                    SweetType.DONUT, 5));
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        IngredientRepository ingredientRepository = new IngredientInMemoryRepository(new ArrayList<>());
        ingredientRepository.generateIngredients();
        sweetService = new SweetServiceImpl(sweetRepository, ingredientRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Tests passed");
    }

    @Test
    void testGetAvailableSweets() {
        assertEquals(sweetService.getAvailableSweets().size(), 1);
    }

    @Test
    void testValidFindSweetById() throws ServiceException {
        Sweet sweet = sweetService.findSweetById(String.valueOf(1L));
        assertEquals(sweet.getId(), 1L);
        assertEquals(sweet.getSweetType(), SweetType.DONUT);
        assertEquals(sweet.getPrice(), 5);
        assertEquals(sweet.getIngredientsList().toString(), new ArrayList<>(List.of(
                new Ingredient(1, "Sugar", 1.5),
                new Ingredient(2, "Milk", 1),
                new Ingredient(3, "Flour", 0.75))).toString());
        assertEquals(sweet.getExtraIngredients(), new ArrayList<>());
    }

    @Test
    void testInvalidFindSweetById() throws ServiceException {
        assertThrowsExactly(ServiceException.class,
                () -> sweetService.findSweetById("abcd"),
                SWEET_ID_EXCEPTION);

        Sweet sweet = sweetService.findSweetById("777");
        assertNull(sweet);
    }

    @Test
    void testCreateEmptySweet() throws ServiceException {
        Sweet sweet = sweetService.createNewSweetWithoutIngredients();

        assertEquals(sweet.getSweetType(), SweetType.UNIQUE);
        assertEquals(sweet.getId(), ID + 1);
        assertEquals(sweet.getOriginalPrice(), SWEET_DEFAULT_PRICE);
        assertEquals(sweet.getExtraPrice(), 0);
        assertEquals(sweet.getPrice(), SWEET_DEFAULT_PRICE);
        assertTrue(sweet.getExtraIngredients().isEmpty());
        assertTrue(sweet.getIngredientsList().isEmpty());
    }

    @Test
    void testValidAddIngredientToSweet() throws ServiceException {
        Sweet sweet = sweetService.createNewSweetWithoutIngredients();
        assertEquals(sweet.getIngredientsList().size(), 0);

        sweetService.addIngredientToSweet(sweet, ingredient, AMOUNT);
        assertEquals(sweet.getIngredientsList().size(), AMOUNT);
        assertEquals(sweet.getOriginalPrice(), 2 + INGREDIENT_PRICE * AMOUNT);
    }

    void testInvalidAddIngredientToSweet() {
        assertThrowsExactly(ServiceException.class,
                () -> sweetService.addIngredientToSweet(null, ingredient, AMOUNT),
                ID_INGREDIENT_EXCEPTION);
    }

    @Test
    void testValidAddAllIngredientsToSweet() throws ServiceException {
        Sweet sweet = sweetService.createNewSweetWithoutIngredients();
        assertEquals(sweet.getIngredientsList().size(), 0);

        String add1 = "oreo,2;ice cream,3;";
        sweetService.addAllIngredientsToSweet(sweet, add1);
        assertEquals(sweet.getIngredientsList().size(), 5);
        verifIfAllAdded(add1, sweet.getIngredientsList().subList(0, 5));

        String add2 = "sugar,1;sugar,3;sugar,2;";
        sweetService.addAllIngredientsToSweet(sweet, add2);
        assertEquals(sweet.getIngredientsList().size(), 11);
        verifIfAllAdded(add2, sweet.getIngredientsList().subList(5, 11));

        String add3 = "cacao,2;";
        sweetService.addAllIngredientsToSweet(sweet, add3);
        assertEquals(sweet.getIngredientsList().size(), 13);
        verifIfAllAdded(add3, sweet.getIngredientsList().subList(11, 13));
    }


    @Test
    void testInvalidAddAllIngredientsToSweet() throws ServiceException {
        Sweet sweet = sweetService.createNewSweetWithoutIngredients();
        assertEquals(sweet.getIngredientsList().size(), 0);

        assertThrowsExactly(ServiceException.class,
                () -> sweetService.addAllIngredientsToSweet(sweet, "cacao,buna;"),
                INGREDIENT_AMOUNT_EXCEPTION);

        assertThrowsExactly(ServiceException.class,
                () -> sweetService.addAllIngredientsToSweet(sweet, "cacaua,3;"),
                INGREDIENT_NAME_EXCEPTION);

    }

    private void verifIfAllAdded(String sequence, List<Ingredient> ingredientsList) {
        for (Ingredient i : ingredientsList)
            if (!sequence.toUpperCase().contains(i.getName().toUpperCase()))
                fail();
    }

}