package domain.sweet;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private long idRecipe;
    private List<Ingredient> ingredientsList;
    private List<Ingredient> extraIngredients;

    public Recipe(long idRecipe, List<Ingredient> ingredientsList) {
        this.idRecipe = idRecipe;
        this.ingredientsList = ingredientsList;
        this.extraIngredients = new ArrayList<>();
    }

    public long getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(long idRecipe) {
        this.idRecipe = idRecipe;
    }

    public List<Ingredient> getIngredientsList() {
        return ingredientsList;
    }

    public List<Ingredient> getExtraIngredients() {
        return extraIngredients;
    }

    public void setIngredientsList(List<Ingredient> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public void setExtraIngredients(List<Ingredient> extraIngredients) {
        this.extraIngredients = extraIngredients;
    }


    @Override
    public String toString() {
        return "Recipe:" +
                "\nIngredients: " + ingredientsList +
                "\nExtra ingredients to add: " + extraIngredients +
                '}';
    }
}
