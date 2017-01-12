package groceryproject.jacob.com.recipelist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

public class RecipeList extends AppCompatActivity{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int REQUEST_CODE=1;
    RecipeDB dbHelper = new RecipeDB(this);
    List<Recipe> recipes;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipes = dbHelper.getAllRecipes();

        String log = "";
        for (Recipe rn : recipes){
            log += "Id: " + rn.getID() + ", Name: " + rn.getRecipeName() + ", Cook Time: " + rn.getCookTime() + " , Prep Time: " + rn.getPrepTime();
            log += "\n";
        }
        Log.d("Name, ", "New Test------");
        Log.d("Name, ", log);

        setContentView(R.layout.activity_recipe_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.list_recycler_view);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new RecipeListAdapter(recipes);
        mRecyclerView.setAdapter(mAdapter);

    }

    public boolean onOptionsItemSelected(MenuItem item){
        //Handles menu buttons
        switch (item.getItemId()){
            case R.id.recipe_list_add_recipe_actionbar_button:
                //This button creates a new empty Recipe object and passes it to the EditRecipe class
                //The Recipe object is passed as a parcelable
                Recipe passedRecipe = new Recipe();
                Intent i = new Intent(RecipeList.this, EditRecipe.class);
                i.putExtra("passed_recipe_key", (Parcelable) passedRecipe);
                startActivityForResult(i, REQUEST_CODE);
                return true;
            default:
                Log.d("Name,", "default called");
                return super.onOptionsItemSelected(item);

        }
    }

    public void addNewReRecipe(Recipe recipe){
        dbHelper.addRecipe(recipe);
        recipes = dbHelper.getAllRecipes();
        mAdapter = new RecipeListAdapter(recipes);
        mRecyclerView.setAdapter(mAdapter);
    }

    //Makes the menu bar appear as it is in the action_bar_add_button_recipe_list menu layout file
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_add_button_recipe_list, menu);
        return true;
    }


    //This code is called after creating a new recipe. This is only for creating, and not editing.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK) {
                Recipe createdRecipe = data.getExtras().getParcelable("recipe_key");
                addNewReRecipe(createdRecipe);
            }
        }
    }


}


