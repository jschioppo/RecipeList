package groceryproject.jacob.com.recipelist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

public class RecipeList extends AppCompatActivity{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int REQUEST_CODE=1;
    ArrayList<Recipe> recipes;

    //private SQLiteDatabase mDatabase;

    //TODO: Create a new taskbar
    //TODO: Create a navigaton bar.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecipeSaver dbHelper = new RecipeSaver(this);

        if(savedInstanceState != null){
            recipes = savedInstanceState.getParcelableArrayList("savedRecipes");
        }


        Recipe testRecipe = new Recipe();
        testRecipe.setRecipeName("Test Recipe");
        testRecipe.setServingSize("2 tests");
        testRecipe.setCookTime("20");
        testRecipe.setPrepTime("80");

        Recipe testRecipeTwo = new Recipe();
        testRecipeTwo.setRecipeName("Test two");

        dbHelper.addRecipe(testRecipe);
        dbHelper.addRecipe(testRecipeTwo);

        recipes = dbHelper.getAllRecipes();

        String log = "No results";
        for (Recipe rn : recipes){
            log = "Id: " + rn.getID() + ", Name: " + rn.getRecipeName();
        }
        Log.d("Name, ", log);



        setContentView(R.layout.activity_recipe_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.list_recycler_view);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new MyAdapter(recipes);
        mRecyclerView.setAdapter(mAdapter);



        //This button creates a new empty Recipe object and passes it to the EditRecipe class
        //The Recipe object is passed as a parcelable
        Button mCreateRecipeButton = (Button) findViewById(R.id.create_new_recipe_button);
        mCreateRecipeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Create a new empty recipe to be passed to the EditRecipe class
                Recipe passedRecipe = new Recipe();
                Intent i = new Intent(RecipeList.this, EditRecipe.class);
                i.putExtra("passed_recipe_key", (Parcelable) passedRecipe);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putParcelableArrayList("savedRecipes", recipes);

    }



    //This code is called after creating a new recipe. This is only for creating, and not editing.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK) {
                Recipe editedRecipe = data.getExtras().getParcelable("recipe_key");
                recipes.add(editedRecipe);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    //This will be called from RecipeTextView after a user edits a recipe they chose. When a new reicpe is
    //created, the user returns here. When a recipe is edited, the user returns to that text view. But
    //The recipe still needs to be updated within this list (Yay consistency!)(Holy cow Google has spell check in comments...the future is now.)(Hire me I'm comical and write overly long comments.)
    protected void updateList(Recipe editedRecipe){
        for(Recipe recipe : recipes){
            if(recipe.getID() == editedRecipe.getID()){
                int index = recipes.indexOf(recipe);
                recipes.set(index, editedRecipe);
                mAdapter.notifyDataSetChanged();
                break;
            }
        }

    }


}


