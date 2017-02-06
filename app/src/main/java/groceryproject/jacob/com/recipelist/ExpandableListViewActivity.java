package groceryproject.jacob.com.recipelist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class ExpandableListViewActivity extends AppCompatActivity {

    ExpandableIngredientListAdapter listAdapter;

    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    RecipeDB dbHelper = new RecipeDB(this);
    private ArrayList<GroceryListItem> groceries;
    private Context mContext;

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
            listDataHeader.add(grocery.getRecipeName());
            listDataChild.put(listDataHeader.get(i), grocery.getIngredients());
            i++;
        }

    }

    /*
    private class ExpandableListAdapter extends BaseExpandableListAdapter{

        private LayoutInflater inflater;

        public ExpandableListAdapter(){
            inflater = LayoutInflater.from(ExpandableListViewActivity.this);
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parentView){

            final GroceryListItem grocery = groceries.get(groupPosition);

            convertView = inflater.inflate(R.layout.expandable_list_view_group, parentView, false);

            return convertView;

        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parentView){

            final GroceryListItem grocery = groceries.get(groupPosition);
            final List<String> ingredients = grocery.getIngredients();

            convertView = inflater.inflate(R.layout.expandable_list_view_item, parentView, false);



        }




    }
    */
}
