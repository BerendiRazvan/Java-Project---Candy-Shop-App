package service.sweetService;

import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import domain.sweet.SweetType;
import exception.RepositoryException;
import exception.ServiceException;
import org.junit.jupiter.api.*;
import repository.ingredientRepository.IngredientInMemoryRepository;
import repository.ingredientRepository.IngredientRepository;
import repository.sweetRepository.SweetInMemoryRepository;
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

    @BeforeAll
    static void setUpAll() {
        System.out.println("Tests for SweetServiceImpl");
    }

    @BeforeEach
    void setUp() {
        ingredient = Ingredient.builder()
                .id(ID)
                .name(INGREDIENT_NAME)
                .price(INGREDIENT_PRICE)
                .amount(AMOUNT)
                .build();


        SweetRepository sweetRepository = SweetInMemoryRepository.builder()
                .sweetList(new ArrayList<>())
                .build();

        try {
            sweetRepository.add(Sweet.builder()
                    .id(1)
                    .ingredientsList(
                            Arrays.asList(
                                    Ingredient.builder().id(1).name("Sugar").price(1.5).build(),
                                    Ingredient.builder().id(2).name("Milk").price(1).build(),
                                    Ingredient.builder().id(3).name("Flour").price(0.75).build()))
                    .sweetType(SweetType.DONUT)
                    .price(5).build());
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }

        IngredientRepository ingredientRepository = IngredientInMemoryRepository.builder()
                .ingredientList(new ArrayList<>())
                .build();
        ingredientRepository.generateIngredients();

        sweetService = SweetServiceImpl.builder()
                .sweetRepository(sweetRepository)
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
    void testGetAvailableSweets() {
        assertEquals(sweetService.getAvailableSweets().size(), 1);
    }

    @Test
    void testValidFindSweetById() throws ServiceException {
        Optional<Sweet> sweet = sweetService.findSweetById(String.valueOf(1L));
        if (sweet.isPresent()) {
            assertEquals(sweet.get().getId(), 1L);
            assertEquals(sweet.get().getSweetType(), SweetType.DONUT);
            assertEquals(sweet.get().getPrice(), 5);
            assertEquals(sweet.get().getIngredientsList().toString(), new ArrayList<>(List.of(
                    Ingredient.builder().id(1).name("Sugar").price(1.5).build(),
                    Ingredient.builder().id(2).name("Milk").price(1).build(),
                    Ingredient.builder().id(3).name("Flour").price(0.75).build())).toString());
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
    void testCreateEmptySweet() throws ServiceException {
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
    void testValidAddIngredientToSweet() throws ServiceException {
        Optional<Sweet> sweet = sweetService.createNewSweetWithoutIngredients();
        if (sweet.isPresent()) {
            assertEquals(sweet.get().getIngredientsList().size(), 0);

            sweetService.addIngredientToSweet(sweet.get(), ingredient, AMOUNT);
            assertEquals(sweet.get().getIngredientsList().size(), AMOUNT);
            assertEquals(sweet.get().getOriginalPrice(), 2 + INGREDIENT_PRICE * AMOUNT);
        } else fail("Sweet addIngredientToSweet failed");
    }

    @Test
    void testValidAddAllIngredientsToSweet() throws ServiceException {
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
    void testInvalidAddAllIngredientsToSweet() throws ServiceException {
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