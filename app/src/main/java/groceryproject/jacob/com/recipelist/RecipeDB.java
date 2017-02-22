package groceryproject.jacob.com.recipelist;

/**
 * Created by Jacob on 11/12/2016.
 */
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.util.Log;


public class RecipeDB extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 6;

    // Database Name
    private static final String DATABASE_NAME = "recipeManager";


    private static final String TABLE_RECIPES = "recipes";
    private static final String TABLE_GROCERIES = "groceries";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "recipe_name";
    private static final String KEY_COOK_TIME = "cook_time";
    private static final String KEY_PREP_TIME = "prep_time";
    private static final String KEY_SERVINGS = "servings";
    private static final String KEY_INGREDIENTS = "ingredients";
    private static final String KEY_DIRECTIONS = "directions";
    private static final String KEY_IN_LIST = "isInList";

    //Values for the grocery list table
    private static final String KEY_ID_GROCERIES = "id_groceries";
    private static final String KEY_INGREDIENTS_GROCERIES = "ingredient_groceries";
    private static final String KEY_NAME_GROCERIES = "name_groceries";

    public RecipeDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_SERVINGS + " TEXT," + KEY_PREP_TIME + " TEXT, " + KEY_COOK_TIME + " TEXT,"
                + KEY_INGREDIENTS + " TEXT," + KEY_DIRECTIONS + " TEXT, " + KEY_IN_LIST + " INTEGER" + ")";

        String CREATE_GROCERIES_TABLE = "CREATE TABLE " + TABLE_GROCERIES + "("
                + KEY_ID_GROCERIES + " INTEGER," + KEY_NAME_GROCERIES + " TEXT,"
                + KEY_INGREDIENTS_GROCERIES + " TEXT" + ")";

        db.execSQL(CREATE_RECIPES_TABLE);
        db.execSQL(CREATE_GROCERIES_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROCERIES);

        onCreate(db);
    }



    void addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, recipe.getRecipeName());
        values.put(KEY_SERVINGS, recipe.getServings());
        values.put(KEY_PREP_TIME, recipe.getPrepTime());
        values.put(KEY_COOK_TIME, recipe.getCookTime());

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

        values.put(KEY_INGREDIENTS, ingedientsConcat);
        values.put(KEY_DIRECTIONS, directionsConcat);

        boolean inList = recipe.isInList();
        int flag = (inList) ? 1: 0;

        values.put(KEY_IN_LIST, flag);


        db.insert(TABLE_RECIPES, null, values);
        db.close(); // Closing database connection
    }

    //This will create a entry in the grocery table using an already existing recipe object.
    public void addGroceryItem(Recipe newListItem){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME_GROCERIES, newListItem.getRecipeName());
        values.put(KEY_ID_GROCERIES, newListItem.getID());

        String ingedientsConcat = "";

        if(newListItem.getIngredients() != null) {
            StringBuilder ingred = new StringBuilder();
            for (String s : newListItem.getIngredients()) {
                ingred.append(s);
                ingred.append("\t");
            }
            ingedientsConcat = ingred.toString();
        }

        values.put(KEY_INGREDIENTS_GROCERIES, ingedientsConcat);

        db.insert(TABLE_GROCERIES, null, values);
        db.close();
    }

    Recipe getRecipe(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RECIPES, new String[] { KEY_ID,
                        KEY_NAME, KEY_SERVINGS, KEY_PREP_TIME, KEY_COOK_TIME, KEY_INGREDIENTS, KEY_DIRECTIONS, KEY_IN_LIST },
                        KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

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

        int flag = cursor.getInt(7);
        boolean inList = (flag != 0);

        Recipe recipe = new Recipe(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), ingredientsList, directionsList, inList);
        return recipe;
    }

    Recipe getRecipe(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RECIPES, new String[] { KEY_ID,
                        KEY_NAME, KEY_SERVINGS, KEY_PREP_TIME, KEY_COOK_TIME, KEY_INGREDIENTS, KEY_DIRECTIONS, KEY_IN_LIST },
                KEY_NAME + "=?", new String[] { name }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

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

        int flag = cursor.getInt(7);
        boolean inList = (flag != 0);

        Recipe recipe = new Recipe(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), ingredientsList, directionsList, inList);
        return recipe;
    }


    

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipeList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_RECIPES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
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

                int flag = cursor.getInt(7);
                boolean inList = (flag != 0);
                recipe.setInList(inList);

                recipeList.add(recipe);
            } while (cursor.moveToNext());
        }


        return recipeList;
    }

    public List<GroceryListItem> getAllGroceries(){
        List<GroceryListItem> groceries = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_GROCERIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do {
                List<String> ingredientsList = new ArrayList<>();
                String ingredients = cursor.getString(2);

                if (ingredients != null){
                    if(ingredients.contains("\t")) {
                        ingredientsList = Arrays.asList(ingredients.split("\t"));
                    }
                    else{
                        ingredientsList = Arrays.asList(ingredients);
                    }
                }

                GroceryListItem groceryItem = new GroceryListItem(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), ingredientsList);

                groceries.add(groceryItem);


            }while (cursor.moveToNext());
        }


        return groceries;

    }


    public int updateRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, recipe.getRecipeName());
        values.put(KEY_SERVINGS, recipe.getServings());
        values.put(KEY_PREP_TIME, recipe.getPrepTime());
        values.put(KEY_COOK_TIME, recipe.getCookTime());

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

        values.put(KEY_INGREDIENTS, ingedientsConcat);
        values.put(KEY_DIRECTIONS, directionsConcat);

        boolean inList = recipe.isInList();
        int flag = (inList) ? 1: 0;

        values.put(KEY_IN_LIST, flag);

        // updating row
        return db.update(TABLE_RECIPES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(recipe.getID()) });
    }

    public int updateGrocery(GroceryListItem groceryItem){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME_GROCERIES, groceryItem.getRecipeName());

        String ingedientsConcat = "";

        if(groceryItem.getIngredients() != null) {
            StringBuilder ingred = new StringBuilder();
            for (String s : groceryItem.getIngredients()) {
                ingred.append(s);
                ingred.append("\t");
            }
            ingedientsConcat = ingred.toString();
        }

        values.put(KEY_INGREDIENTS_GROCERIES, ingedientsConcat);

        return  db.update(TABLE_GROCERIES, values, KEY_ID_GROCERIES + " = ?",
                new String[] { String.valueOf(groceryItem.getId()) });
    }


    public void deleteRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPES, KEY_ID + " = ?",
                new String[] { String.valueOf(recipe.getID()) });
        db.close();
    }

    public void deleteGrocery(String name){
        SQLiteDatabase db = this.getWritableDatabase();

        Recipe recipe = getRecipe(name);
        recipe.setInList(false);
        updateRecipe(recipe);

        db.delete(TABLE_GROCERIES, KEY_NAME_GROCERIES + " = ?", new String[] {name});
        db.close();
    }

    public void deleteGroceryItem(GroceryListItem listItem){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GROCERIES, KEY_ID_GROCERIES + " = ?",
                new String[] { String.valueOf(listItem.getId()) });
        db.close();
    }

    public int getRecipeCount() {
        String countQuery = "SELECT  * FROM " + TABLE_RECIPES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }




}


