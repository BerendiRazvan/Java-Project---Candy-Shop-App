package service.sweetService;

import builder.*;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import domain.sweet.SweetType;
import exception.BuildException;
import exception.RepositoryException;
import exception.ServiceException;
import org.junit.jupiter.api.*;
import repository.ingredientRepository.IngredientRepository;
import repository.sweetRepository.SweetRepository;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static service.ConstantValues.*;

class SweetServiceImplTest {
    private SweetService sweetService;
    private Ingredient ingredient;

    private IngredientBuilder ingredientBuilder;
    private SweetBuilder sweetBuilder;

    @BeforeAll
    static void setUpAll() {
        System.out.println("Tests for SweetServiceImpl");
    }

    @BeforeEach
    void setUp() throws BuildException {
        SweetInMemoryRepositoryBuilder sweetInMemoryRepositoryBuilder = new SweetInMemoryRepositoryBuilder();
        IngredientInMemoryRepositoryBuilder ingredientInMemoryRepositoryBuilder = new IngredientInMemoryRepositoryBuilder();

        SweetServiceImplBuilder sweetServiceImplBuilder = new SweetServiceImplBuilder();

        ingredientBuilder = new IngredientBuilder();
        sweetBuilder = new SweetBuilder();

        ingredient = ingredientBuilder.build(ID, INGREDIENT_NAME, INGREDIENT_PRICE, AMOUNT);

        SweetRepository sweetRepository = sweetInMemoryRepositoryBuilder.build(new ArrayList<>());

        try {
            sweetRepository.add(sweetBuilder.build(ID,
                    Arrays.asList(
                            ingredientBuilder.build(1, "Sugar", 1.5),
                            ingredientBuilder.build(2, "Milk", 1),
                            ingredientBuilder.build(3, "Flour", 0.75)),
                    SweetType.DONUT, 5));
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }

        IngredientRepository ingredientRepository = ingredientInMemoryRepositoryBuilder.build(new ArrayList<>());
        ingredientRepository.generateIngredients();

        sweetService = sweetServiceImplBuilder.build(sweetRepository, ingredientRepository);
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
    void testValidFindSweetById() throws ServiceException, BuildException {
        Optional<Sweet> sweet = sweetService.findSweetById(String.valueOf(1L));
        if (sweet.isPresent()) {
            assertEquals(sweet.get().getId(), 1L);
            assertEquals(sweet.get().getSweetType(), SweetType.DONUT);
            assertEquals(sweet.get().getPrice(), 5);
            assertEquals(sweet.get().getIngredientsList().toString(), new ArrayList<>(List.of(
                    ingredientBuilder.build(1, "Sugar", 1.5),
                    ingredientBuilder.build(2, "Milk", 1),
                    ingredientBuilder.build(3, "Flour", 0.75))).toString());
            assertEquals(sweet.get().getExtraIngredients(), new ArrayList<>());
        } else fail("Sweet findSweetById failed");
    }

    @Test
    void testInvalidFindSweetById() throws ServiceException {
        assertThrowsExactly(ServiceException.class,
                () -> sweetService.findSweetById("abcd"),
                SWEET_ID_EXCEPTION);
        assertThrowsExactly(ServiceException.class,
                () -> sweetService.findSweetById("777"),
                SWEET_ID_EXCEPTION);
    }

    @Test
    void testCreateEmptySweet() throws ServiceException, BuildException {
        Optional<Sweet> sweet = sweetService.createNewSweetWithoutIngredients();
        if (sweet.isPresent()) {
            assertEquals(sweet.get().getSweetType(), SweetType.UNIQUE);
            assertEquals(sweet.get().getId(), ID + 1);
            assertEquals(sweet.get().getOriginalPrice(), SWEET_DEFAULT_PRICE);
            assertEquals(sweet.get().getExtraPrice(), 0);
            assertEquals(sweet.get().getPrice(), SWEET_DEFAULT_PRICE);
            assertTrue(sweet.get().getExtraIngredients().isEmpty());
            assertTrue(sweet.get().getIngredientsList().isEmpty());
        } else fail("Sweet createEmptySweet failed");
    }

    @Test
    void testValidAddIngredientToSweet() throws ServiceException, BuildException {
        Optional<Sweet> sweet = sweetService.createNewSweetWithoutIngredients();
        if (sweet.isPresent()) {
            assertEquals(sweet.get().getIngredientsList().size(), 0);

            sweetService.addIngredientToSweet(sweet.get(), ingredient, AMOUNT);
            assertEquals(sweet.get().getIngredientsList().size(), AMOUNT);
            assertEquals(sweet.get().getOriginalPrice(), 2 + INGREDIENT_PRICE * AMOUNT);
        } else fail("Sweet addIngredientToSweet failed");
    }

    @Test
    void testValidAddAllIngredientsToSweet() throws ServiceException, BuildException {
        Optional<Sweet> sweet = sweetService.createNewSweetWithoutIngredients();
        if (sweet.isPresent()) {
            assertEquals(sweet.get().getIngredientsList().size(), 0);

            String add1 = "oreo,2;ice cream,3;";
            sweetService.addAllIngredientsToSweet(sweet.get(), add1);
            assertEquals(sweet.get().getIngredientsList().size(), 5);
            verifIfAllAdded(add1, sweet.get().getIngredientsList().subList(0, 5));

            String add2 = "sugar,1;sugar,3;sugar,2;";
            sweetService.addAllIngredientsToSweet(sweet.get(), add2);
            assertEquals(sweet.get().getIngredientsList().size(), 11);
            verifIfAllAdded(add2, sweet.get().getIngredientsList().subList(5, 11));

            String add3 = "cacao,2;";
            sweetService.addAllIngredientsToSweet(sweet.get(), add3);
            assertEquals(sweet.get().getIngredientsList().size(), 13);
            verifIfAllAdded(add3, sweet.get().getIngredientsList().subList(11, 13));
        } else fail("Sweet addAllIngredientsToSweet failed");
    }


    @Test
    void testInvalidAddAllIngredientsToSweet() throws ServiceException, BuildException {
        Optional<Sweet> sweet = sweetService.createNewSweetWithoutIngredients();
        if (sweet.isPresent()) {
            assertEquals(sweet.get().getIngredientsList().size(), 0);

            assertThrowsExactly(ServiceException.class,
                    () -> sweetService.addAllIngredientsToSweet(sweet.get(), "cacao,buna;"),
                    INGREDIENT_AMOUNT_EXCEPTION);

            assertThrowsExactly(ServiceException.class,
                    () -> sweetService.addAllIngredientsToSweet(sweet.get(), "cacaua,3;"),
                    INGREDIENT_NAME_EXCEPTION);
        } else fail("Sweet addAllIngredientsToSweet failed");
    }

    private void verifIfAllAdded(String sequence, List<Ingredient> ingredientsList) {
        for (Ingredient i : ingredientsList)
            if (!sequence.toUpperCase().contains(i.getName().toUpperCase()))
                fail();
    }

}