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
    RecipeDB dbHelper = new RecipeDB(this);
    List<Recipe> recipes;

    //private SQLiteDatabase mDatabase;

    //TODO: Create a new taskbar
    //TODO: Create a navigaton bar.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /*
        if(savedInstanceState != null){
            recipes = savedInstanceState.getParcelableArrayList("savedRecipes");
        }
        */
        recipes = dbHelper.getAllRecipes();

        //recipes.add(testTwo); //This is to prove the adapter is working


        String log = "No results";
        for (Recipe rn : recipes){
            log = "Id: " + rn.getID() + ", Name: " + rn.getRecipeName() + ", Cook Time: " + rn.getCookTime() + " , Prep Time: " + rn.getPrepTime();
        }
        Log.d("Name, ", "New Test------");
        Log.d("Name, ", log);

        //This log is printing no results when the app actually runs


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

    public void addNewReRecipe(Recipe recipe){
        dbHelper.addRecipe(recipe);
        recipes = dbHelper.getAllRecipes();
        mAdapter = new MyAdapter(recipes);
        mRecyclerView.setAdapter(mAdapter);
    }

    /*
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putParcelableArrayList("savedRecipes", recipes);

    }
    */



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


