package groceryproject.jacob.com.recipelist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfWriter;

public class ExpandableListViewActivity extends AppCompatActivity {

    ExpandableIngredientListAdapter listAdapter;

    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    RecipeDB dbHelper = new RecipeDB(this);
    private Button mNavigateRecipesButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_list_view);

        expListView = (ExpandableListView) findViewById(R.id.expandable_list_base_view);

        prepareListData();

        listAdapter = new ExpandableIngredientListAdapter(this, listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);


        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override

            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });



        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                prepareListData();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                prepareListData();

            }
        });

        mNavigateRecipesButton = (Button) findViewById(R.id.navigate_to_recipes_button_expanded_view);
        mNavigateRecipesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(ExpandableListViewActivity.this, RecipeList.class);
                startActivity(i);
            }
        });

    }
    public void prepareListData(){
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        int i = 0;

        List<GroceryListItem> groceries;
        groceries = dbHelper.getAllGroceries();

        for(GroceryListItem grocery : groceries){
            listDataHeader.add(grocery.getRecipeName());
            listDataChild.put(listDataHeader.get(i), grocery.getIngredients());
            i++;
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ExpandableListViewActivity.this, RecipeList.class);
        startActivity(i);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        //Handles menu buttons
        switch (item.getItemId()){
            case R.id.grocery_list_delete_all_groceries_actionbar_button:
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
                builder.setTitle("Are you sure?");
                builder.setMessage("This will clear every item in your grocery list.");
                builder.setPositiveButton("I'm sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("boolList", "");
                        editor.clear();
                        editor.apply();

                        List<GroceryListItem> groceries;
                        groceries = dbHelper.getAllGroceries();

                        for(GroceryListItem item : groceries){
                            String groceryName = item.getRecipeName();
                            Recipe grabRecipe = dbHelper.getRecipe(groceryName);
                            grabRecipe.setInList(false);
                            dbHelper.updateRecipe(grabRecipe);
                        }

                        dbHelper.deleteAllGroceries();
                        Intent intent = new Intent(ExpandableListViewActivity.this, ExpandableListViewActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
                return true;

            case R.id.grocery_list_export_to_pdf_button:
                
                return true;

            default:
                Log.d("Name,", "default called, textview class");
                return super.onOptionsItemSelected(item);

        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_grocery_list_buttons, menu);
        return true;
    }





}
