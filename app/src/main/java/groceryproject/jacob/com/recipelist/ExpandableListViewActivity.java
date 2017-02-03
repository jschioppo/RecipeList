package groceryproject.jacob.com.recipelist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class ExpandableListViewActivity extends AppCompatActivity {

    ExpandableIngredientListAdapter listAdapter;

    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    RecipeDB dbHelper = new RecipeDB(this);
    private Button mDeleteButton;

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
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });

    }


    private void prepareListData(){
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        int i = 0;

        List<GroceryListItem> groceries;
        groceries = dbHelper.getAllGroceries();

        for(GroceryListItem grocery : groceries){
            //Log.d("myApp", "Grocery found");
            listDataHeader.add(grocery.getRecipeName());
            listDataChild.put(listDataHeader.get(i), grocery.getIngredients());
            i++;
        }

    }
}
