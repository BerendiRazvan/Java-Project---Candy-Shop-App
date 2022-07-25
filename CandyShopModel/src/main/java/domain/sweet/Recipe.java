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

    public void addIngredientToRecipe(Ingredient newIngredient) throws Exception {
        if (!ingredientsList.contains(newIngredient))
            ingredientsList.add(newIngredient);
        else
            throw new Exception("This ingredient exists!");
    }

    public void addExtraIngredient(Ingredient extraIngredient) throws Exception {
        if (!extraIngredients.contains(extraIngredient))
            extraIngredients.add(extraIngredient);
        else
            throw new Exception("This ingredient exists!");
    }

    public void removeIngredientFromRecipe(Ingredient ingredient) throws Exception {
        if (ingredientsList.contains(ingredient))
            ingredientsList.remove(ingredient);
        else
            throw new Exception("This ingredient does not exist!");
    }

    public void removeExtraIngredient(Ingredient extraIngredient) throws Exception {
        if (extraIngredients.contains(extraIngredient))
            extraIngredients.remove(extraIngredient);
        else
            throw new Exception("This ingredient does not exist!");
    }


    @Override
    public String toString() {
        return "Recipe:" +
                "\nIngredients: " + ingredientsList +
                "\nExtra ingredients to add: " + extraIngredients +
                '}';
    }
}
