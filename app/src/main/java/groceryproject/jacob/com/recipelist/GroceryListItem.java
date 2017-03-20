package groceryproject.jacob.com.recipelist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacob on 1/25/2017.
 */
public class GroceryListItem {

    private int mId;
    private String recipeName;
    private List<String> ingredients;
    private boolean[] isChecked;

    public GroceryListItem(int id, String name, List<String> ingredients){
        this.mId = id;
        this.recipeName = name;
        this.ingredients = ingredients;
        isChecked = new boolean[ingredients.size()];
    }

    public GroceryListItem(String name, List<String> ingredients){
        this.recipeName = name;
        this.ingredients = ingredients;
    }

    public GroceryListItem(){

    }

    public int getId() {
        return mId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setArrayIndexTrue(int index){
        isChecked[index] = true;
    }

    public boolean[] returnCheckedItems(){
        return isChecked;
    }
}
