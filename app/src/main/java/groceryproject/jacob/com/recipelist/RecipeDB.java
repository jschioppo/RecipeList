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
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "recipeManager";


    private static final String TABLE_RECIPES = "recipes";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "recipe_name";
    private static final String KEY_COOK_TIME = "cook_time";
    private static final String KEY_PREP_TIME = "prep_time";
    private static final String KEY_SERVINGS = "servings";
    private static final String KEY_INGREDIENTS = "ingredients";
    private static final String KEY_DIRECTIONS = "directions";
    private static final String KEY_IN_LIST = "isInList";

    public RecipeDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_SERVINGS + " TEXT," + KEY_PREP_TIME + " TEXT, " + KEY_COOK_TIME + " TEXT,"
                + KEY_INGREDIENTS + " TEXT," + KEY_DIRECTIONS + " TEXT, " + KEY_IN_LIST + " INTEGER" + ")";

        db.execSQL(CREATE_RECIPES_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        // Create tables again
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

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipeList = new ArrayList<Recipe>();

        String selectQuery = "SELECT  * FROM " + TABLE_RECIPES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();

                /*
                Log.d("Name, ", "Index 0: " + cursor.getString(0));
                Log.d("Name, ", "Index 1: " + cursor.getString(1));
                Log.d("Name, ", "Index 2: " + cursor.getString(2));
                Log.d("Name, ", "Index 3: " + cursor.getString(3));
                Log.d("Name, ", "Index 4: " + cursor.getString(4));
                Log.d("Name, ", "Index 5: " + cursor.getString(5));
                Log.d("Name, ", "Index 6: " + cursor.getString(6));
                Log.d("Name, ", "Index 7: " + cursor.getInt(7));
                */

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

        // updating row
        return db.update(TABLE_RECIPES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(recipe.getID()) });
    }


    public void deleteRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPES, KEY_ID + " = ?",
                new String[] { String.valueOf(recipe.getID()) });
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


