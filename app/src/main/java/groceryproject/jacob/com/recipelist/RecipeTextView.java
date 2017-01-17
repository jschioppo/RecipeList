package groceryproject.jacob.com.recipelist;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class RecipeTextView extends AppCompatActivity {

    private TextView mRecipeName;
    private TextView mServings;
    private TextView mCookTime;
    private TextView mPrepTime;
    private TextView mDirections;
    private TextView mIngredients;
    private Button mEditButton;
    private Button mNavigateRecipesButton;
    private Button mDeleteButton;
    private int REQUEST_CODE = 1;
    RecipeDB dbHelper = new RecipeDB(this);
    Recipe selectedRecipe = new Recipe();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedRecipe = getIntent().getExtras().getParcelable("view_recipe_key");

        if (savedInstanceState != null){
            selectedRecipe = savedInstanceState.getParcelable("savedRecipe");
        }

        loadActivity(selectedRecipe);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("savedRecipe", selectedRecipe);
    }

    protected void loadActivity(final Recipe passedRecipe){

        setContentView(R.layout.activity_recipe_text_view);

        mRecipeName = (TextView) findViewById(R.id.recipe_name_text_view);
        mRecipeName.setText(passedRecipe.getRecipeName());

        mServings = (TextView) findViewById(R.id.serving_text_view);
        if(passedRecipe.getServings() != null && passedRecipe.getServings() != "") {
            mServings.setText(passedRecipe.getServings());
        }

        mPrepTime = (TextView) findViewById(R.id.prep_time_text_view);
        mPrepTime.setText(null);
        if(passedRecipe.getPrepTime() != null && passedRecipe.getPrepTime() != "") {
            mPrepTime.setText("Prep time: " + selectedRecipe.getPrepTime() + " minutes");
        }

        mCookTime = (TextView) findViewById(R.id.cook_time_text_view);
        if(passedRecipe.getCookTime() != null && passedRecipe.getCookTime() != "") {
            mCookTime.setText("Cook time: " + passedRecipe.getCookTime() + " minutes");
        }

        //Below code builds a string concatenating a bullet to each line followed by a new line
        //Each line is an index of the directions arraylist
        List<String> ingredientsList = passedRecipe.getIngredients();
        int ingredientsSize = ingredientsList.size();
        String ingredientsConcat = "";

        for(int i = 0; i < ingredientsSize; i++){
            ingredientsConcat += "\u2022 " + ingredientsList.get(i) + "\n";
        }
        mIngredients = (TextView) findViewById(R.id.ingredients_text_view);
        if(ingredientsConcat.equals("\u2022 " + "\n") == false) {
            mIngredients.setText(ingredientsConcat);
        }


        //This code does almost the same as above for the directions, except it appends a number
        //to each line instead of a bullet.
        List<String> directionsList = passedRecipe.getDirections();
        int directionsSize = directionsList.size();
        String directionsConcat = "";

        for(int i = 0; i < directionsSize; i++){
            directionsConcat += (i + 1) + ". " + directionsList.get(i) + "\n";
        }
        mDirections = (TextView) findViewById(R.id.directions_text_view);
        if(directionsConcat.equals("1. \n") == false) {
            mDirections.setText(directionsConcat);
        }

        /*
        mEditButton = (Button) findViewById(R.id.edit_recipe_button);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecipeTextView.this, EditRecipe.class);
                i.putExtra("passed_recipe_key", passedRecipe);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
        */

        mNavigateRecipesButton = (Button) findViewById(R.id.navigate_to_recipes_button_text_view);
        mNavigateRecipesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(RecipeTextView.this, RecipeList.class);
                startActivity(i);
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item){
        //Handles menu buttons
        switch (item.getItemId()){
            case R.id.recipe_text_view_delete_button_action_bar:
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
                builder.setTitle("Are you sure?");
                builder.setMessage("This action cannot be undone.");
                builder.setPositiveButton("I'm sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbHelper.deleteRecipe(selectedRecipe);
                        Intent p = new Intent(RecipeTextView.this, RecipeList.class);
                        startActivity(p);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
                return true;
            case R.id.recipe_text_view_edit_button_action_bar:
                Intent i = new Intent(RecipeTextView.this, EditRecipe.class);
                i.putExtra("passed_recipe_key", selectedRecipe);
                startActivityForResult(i, REQUEST_CODE);
            default:
                Log.d("Name,", "default called, textview class");
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    //This is called when the user is finished EDITING the recipe chosen.
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK) {
                Recipe editedRecipe = (Recipe) data.getExtras().getParcelable("recipe_key");

                //Since the user is returned to this class and not RecipeList, we want to make sure that the appropriate
                //list inside the list of recipes. This is only necessary here since creating a new recipe returns the user to the recipe list.

                RecipeDB dbHelper = new RecipeDB(this);
                dbHelper.updateRecipe(editedRecipe);

                selectedRecipe = editedRecipe;
                this.loadActivity(editedRecipe);

            }
        }
    }

    //Makes the menu bar appear as it is in the action_bar_recipe_list_buttons menu layout file
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_text_view_buttons, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
    }
}
