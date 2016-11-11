package groceryproject.jacob.com.recipelist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jacob on 10/28/2016.
 */
public class RecipeSaver extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "recipeManager";

    //The recipe table that manages the list of recipes a user creates
    private static final String TABLE_RECIPES = "recipes";

    //The columns for the recipes table
    public static final String ID = "uuid";
    public static final String RECIPE_NAME = "recipe name";
    public static final String SERVINGS = "servings";
    public static final String PREP_TIME = "prep time";
    public static final String COOK_TIME = "cook time";
    public static final String INGREDIENTS = "ingredients";
    public static final String DIRECTIONS = "directions";

 
    public RecipeSaver(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + "( "
                + ID + " INTEGER PRIMARY KEY, " + RECIPE_NAME + " TEXT, " + SERVINGS + " TEXT, "
                + PREP_TIME + " TEXT, " + COOK_TIME + " TEXT, " + INGREDIENTS + " TEXT, " + DIRECTIONS + "TEXT)";

        db.execSQL(CREATE_RECIPES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);

        onCreate(db);
    }

    void addRecipe(Recipe recipe){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(RECIPE_NAME, recipe.getRecipeName());
        values.put(SERVINGS, recipe.getServings());
        values.put(PREP_TIME, recipe.getPrepTime());
        values.put(COOK_TIME, recipe.getCookTime());

        //Next few lines turns an array list of strings into one string seperated by tabs
        String directionsConcat = "";
        String ingedientsConcat = "";

        if(recipe.getIngredients() != null) {
            StringBuilder ingred = new StringBuilder();
            for (String s : recipe.getIngredients()) {
                ingred.append(s);
                ingred.append("\t");
            }
            ingedientsConcat = ingred.toString();
        }

        if(recipe.getDirections() != null) {
            StringBuilder direct = new StringBuilder();
            for (String s : recipe.getDirections()) {
                direct.append(s);
                direct.append("\t");
            }
            directionsConcat = direct.toString();
        }

        values.put(INGREDIENTS, ingedientsConcat);
        values.put(DIRECTIONS, directionsConcat);

        db.insert(TABLE_RECIPES, null, values);
        db.close();
    }

    public Recipe getRecipe(String id){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RECIPES, new String[] {ID, RECIPE_NAME, SERVINGS,
                PREP_TIME, COOK_TIME, INGREDIENTS, DIRECTIONS}, ID + "=?", new String[] {String.valueOf(id)},
                null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Recipe recipe = new Recipe();

        recipe.setID(Integer.parseInt(cursor.getString(0)));
        recipe.setRecipeName(cursor.getString(1));
        recipe.setServingSize(cursor.getString(2));
        recipe.setPrepTime(cursor.getString(3));
        recipe.setCookTime(cursor.getString(4));

        List<String> directionsList = new ArrayList<>();
        List<String> ingredientsList = new ArrayList<>();

        String ingredients = cursor.getString(5);
        String directions = cursor.getString(6);


        if (directions != null){
            if(directions.contains("\t")) {
                directionsList = Arrays.asList(directions.split("\t"));
            }
            else{
                directionsList = Arrays.asList(directions);
            }
        }

        if (ingredients != null){
            if(ingredients.contains("\t")) {
                ingredientsList = Arrays.asList(ingredients.split("\t"));
            }
            else{
                ingredientsList = Arrays.asList(ingredients);
            }
        }

        recipe.setIngredients(ingredientsList);
        recipe.setDirections(directionsList);





        return recipe;
    }

    public ArrayList<Recipe> getAllRecipes(){
        ArrayList<Recipe> recipeList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_RECIPES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do {
                Recipe recipe = new Recipe();

                recipe.setID(Integer.parseInt(cursor.getString(0)));
                recipe.setRecipeName(cursor.getString(1));
                recipe.setServingSize(cursor.getString(2));
                recipe.setPrepTime(cursor.getString(3));
                recipe.setCookTime(cursor.getString(4));

                List<String> directionsList = new ArrayList<>();
                List<String> ingredientsList = new ArrayList<>();

                String ingredients = cursor.getString(5);
                String directions = cursor.getString(6);


                if (directions != null){
                    if(directions.contains("\t")) {
                        directionsList = Arrays.asList(directions.split("\t"));
                    }
                    else{
                        directionsList = Arrays.asList(directions);
                    }
                }


                if (ingredients != null){
                    if(ingredients.contains("\t")) {
                        ingredientsList = Arrays.asList(ingredients.split("\t"));
                    }
                    else{
                        ingredientsList = Arrays.asList(ingredients);
                    }
                }

                recipe.setIngredients(ingredientsList);
                recipe.setDirections(directionsList);

                recipeList.add(recipe);

            } while(cursor.moveToNext());
        }

        return recipeList;
    }

    public int getRecipeCount(){
        String countQuery = "SELECT * FROM " + TABLE_RECIPES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int updateRecipe(Recipe recipe){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RECIPE_NAME, recipe.getRecipeName());
        values.put(SERVINGS, recipe.getServings());
        values.put(PREP_TIME, recipe.getPrepTime());
        values.put(COOK_TIME, recipe.getCookTime());

        StringBuilder ingred = new StringBuilder();
        for(String s : recipe.getIngredients()){
            ingred.append(s);
            ingred.append("\t");
        }

        StringBuilder direct = new StringBuilder();
        for(String s : recipe.getDirections()){
            direct.append(s);
            direct.append("\t");
        }

        String ingedientsConcat = ingred.toString();
        String directionsConcat = direct.toString();

        values.put(INGREDIENTS, ingedientsConcat);
        values.put(DIRECTIONS, directionsConcat);

        return db.update(TABLE_RECIPES, values, ID + " =?",
                new String[] {String.valueOf(recipe.getID())});
    }

    public void deleteRecipe(Recipe recipe){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPES, ID + " =?", new String[] {String.valueOf(recipe.getID())});
        db.close();
    }


}
