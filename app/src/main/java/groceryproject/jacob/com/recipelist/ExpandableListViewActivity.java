package groceryproject.jacob.com.recipelist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
            //TODO: Add checkboxes instead of colors
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //v.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.green));

                //findViewById(R.id.expandable_list_recipe_ingredient_item).setSelected(true);

                //int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                //parent.setItemChecked(1, true);
                //parent.setActivated(true);
                //expListView.setActivated(true);
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



}
