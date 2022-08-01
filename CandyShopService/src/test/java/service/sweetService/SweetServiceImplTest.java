package service.sweetService;

import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import domain.sweet.SweetType;
import repository.sweetsRepository.SweetInMemoryRepository;
import repository.sweetsRepository.SweetRepository;
import service.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SweetServiceImplTest {

    private static SweetService sweetService;

    @org.junit.jupiter.api.BeforeAll
    static void setUpAll() {
        System.out.println("Tests for SweetServiceImpl");

        SweetRepository sweetRepository =
                new SweetInMemoryRepository(SweetInMemoryRepository.generateSweets());
        sweetService = new SweetServiceImpl(sweetRepository);
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.AfterAll
    static void tearDownAll() {
        System.out.println("Tests passed");
    }

    @org.junit.jupiter.api.Test
    void getAvailableSweets() {
        assertEquals(sweetService.getAvailableSweets().size(), 15);
    }

    private void validTestsFindSweetById(){
        try {
            Sweet sweet = sweetService.findSweetById(String.valueOf(1L));

            assertEquals(sweet.getIdSweet(), 1L);
            assertEquals(sweet.getSweetType(), SweetType.DONUT);
            assertEquals(sweet.getPrice(), 5);
            assertEquals(sweet.getIngredientsList().toString(), new ArrayList<>(List.of(
                    new Ingredient(1, "Sugar", 1.5),
                    new Ingredient(2, "Milk", 1),
                    new Ingredient(3, "Flour", 0.75))).toString());
            assertEquals(sweet.getExtraIngredients(), new ArrayList<>());
        } catch (ServiceException e) {
            fail();
        }
    }

    private void invalidTestsFindSweetById(){

        try {
            Sweet sweet = sweetService.findSweetById("dasdasdasdada");
            fail();
        } catch (ServiceException e) {
            assertEquals(e.getMessage(), "Invalid sweet id!");
        }

        try {
            Sweet sweet = sweetService.findSweetById("777");
            assertNull(sweet);
        } catch (ServiceException e) {
            fail();
        }
    }

    @org.junit.jupiter.api.Test
    void findSweetById() {
        validTestsFindSweetById();
        invalidTestsFindSweetById();
    }
}