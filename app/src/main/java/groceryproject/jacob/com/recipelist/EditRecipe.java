
package groceryproject.jacob.com.recipelist;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//This class is used to edit a recipe. Editing includes both creating a new recipe and editing an existing one
//The edit and create button are in RecipeTextView (Edit) and RecipeList (Create)
public class EditRecipe extends AppCompatActivity {
    private final String TAG = "myApp";
    private EditText mRecipeName;
    private EditText mServings;
    private EditText mPrepTime;
    private EditText mIngredients;
    private EditText mDirections;
    private Button mSaveButton;
    private EditText mCookTime;
    private int REQUEST_CODE = 1;

    //These are declared here so that I can use them within each edit text listener, and then set them to the values
    //of the recipe object when the save button is pushed.

    private String recipeName;
    private String prepTime;
    private String cookTime;
    private String servings;
    //For directions and ingredients, we will seperate these into a list of strings by \n (new line).
    private String directions;
    private String ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        final Recipe passedRecipe = (Recipe) getIntent().getExtras().getParcelable("passed_recipe_key");

        mRecipeName = (EditText) findViewById(R.id.recipe_name_text_edit);
        mPrepTime = (EditText) findViewById(R.id.prep_time_edit_text);
        mCookTime = (EditText) findViewById(R.id.cook_time_edit_text);
        mServings = (EditText) findViewById(R.id.serving_edit_text);
        mIngredients = (EditText) findViewById(R.id.ingredients_edit_text);
        mDirections = (EditText) findViewById(R.id.directions_edit_text);
        mSaveButton = (Button) findViewById(R.id.save_edit_recipe_button);


        //The following if statements will only be triggered if this class is accessed from editing an
        //already existing recipe. Otherwise a new recipe is created.
        if(passedRecipe.getRecipeName() != null){
            mRecipeName.setText(passedRecipe.getRecipeName(), TextView.BufferType.EDITABLE);
            recipeName = passedRecipe.getRecipeName();
        }
        else{
            passedRecipe.setInList(false);
        }
        if(passedRecipe.getPrepTime() != null){
            mPrepTime.setText(passedRecipe.getPrepTime(), TextView.BufferType.EDITABLE);
            prepTime = passedRecipe.getPrepTime();
        }
        if(passedRecipe.getCookTime() != null){
            mCookTime.setText(passedRecipe.getCookTime(), TextView.BufferType.EDITABLE);
            cookTime = passedRecipe.getCookTime();
        }
        if(passedRecipe.getServings() != null){
            mServings.setText(passedRecipe.getServings(), TextView.BufferType.EDITABLE);
            servings = passedRecipe.getServings();
        }
        //For the array list values, we check if the array list is empty
        //If it isn't empty, we save each value of the array list into a string concatenated with a new line
        //We then set that string as the new line
        if(passedRecipe.getIngredients() != null){
            String passedIngredientString = "";
            for(int i = 0; i < passedRecipe.getIngredients().size(); i++){
                passedIngredientString += passedRecipe.getIngredients().get(i) + "\n";
            }
            mIngredients.setText(passedIngredientString, TextView.BufferType.EDITABLE);
            ingredients = passedIngredientString;
        }
        if(passedRecipe.getDirections() != null){
            String passedDirectionString = "";
            for(int i = 0; i < passedRecipe.getDirections().size(); i++){
                passedDirectionString += passedRecipe.getDirections().get(i) + "\n";
            }
            mDirections.setText(passedDirectionString, TextView.BufferType.EDITABLE);
            directions = passedDirectionString;
        }



        //In the following Listeners I use .trim at the end to get rid of white space users tend to leave
        //For the integer values I first store the data in a string then parse it to an int.
        mRecipeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recipeName = mRecipeName.getText().toString().trim();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPrepTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                prepTime = mPrepTime.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mServings.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                servings = mServings.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCookTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cookTime = mCookTime.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDirections.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //For now we will save this in a string, then seperate each line into an array list.
                directions = mDirections.getText().toString().trim();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mIngredients.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //For now we will save this in a string, then seperate each line into an array list.
                ingredients = mIngredients.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //TODO
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If statement to make sure the recipe name exists. Every other value can be empty
                //if the user wishes
                if (TextUtils.isEmpty(recipeName)){
                    mRecipeName.setError("Recipe name can not be empty.");
                    return;
                }

                if (recipeName != null) {
                    passedRecipe.setRecipeName(recipeName);
                }
                if(cookTime != null) {
                    passedRecipe.setCookTime(cookTime);
                }
                if(prepTime != null) {
                    passedRecipe.setPrepTime(prepTime);
                }
                if (servings != null) {
                    passedRecipe.setServingSize(servings);
                }


                List<String> directionsList = new ArrayList<>();
                List<String> ingredientsList = new ArrayList<>();

                //Check if the edit text strings are null. if they are, add an empty string to each list
                //If they aren't null, check if they are multi lined.
                //If multi lined, save into a list split by new line. Otherwise, it is just one string. Add it to the list
                if (directions != null){
                    if(directions.contains("\n")) {
                        directionsList = Arrays.asList(directions.split("\n"));
                    }
                    else{
                        directionsList = Arrays.asList(directions);
                    }
                }
                else{
                    directionsList.add("");
                }

                if (ingredients != null){
                    if(ingredients.contains("\n")) {
                        ingredientsList = Arrays.asList(ingredients.split("\n"));
                    }
                    else{
                        ingredientsList = Arrays.asList(ingredients);
                    }
                }
                else{
                    ingredientsList.add("");
                }


                passedRecipe.setDirections(directionsList);
                passedRecipe.setIngredients(ingredientsList);

                Intent returnIntent = new Intent(EditRecipe.this, RecipeList.class);
                returnIntent.putExtra("recipe_key", passedRecipe);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }


}
