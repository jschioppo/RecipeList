package groceryproject.jacob.com.recipelist;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Jacob on 1/12/2017.
 */


public class ExpandableIngredientListAdapter extends BaseExpandableListAdapter{

    private Context mContext;
    private List<String> recipeNames; // header titles
    private HashMap<String, List<String>> recipeIngredients;
    private Button mDeleteButton;
    private List<GroceryListItem> mGrocerySet;


    //I believe that the key in doing an adapter change is in getting rid of these parameters and finding a way to populate the list purely from my database.
    public ExpandableIngredientListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
        this.mContext = context;
        this.recipeNames = listDataHeader;
        this.recipeIngredients = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.recipeIngredients.get(this.recipeNames.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        //Changed from groupPosition to groupPosition - 1
        final String childText = (String) getChild(groupPosition, childPosition);


            LayoutInflater infalInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_view_item, null);


        TextView txtListChild = (TextView) convertView.findViewById(R.id.expandable_list_recipe_ingredient_item);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.recipeIngredients.get(this.recipeNames.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.recipeNames.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.recipeNames.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);

            final LayoutInflater infalInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_view_group, null);

            mDeleteButton = (Button) convertView.findViewById(R.id.delete_recipe_from_grocery_list_button);
            //TODO: Add a dialog to ensure user wants to delete the recipe
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecipeDB dbHelper = new RecipeDB(parent.getContext());
                    String groceryName = recipeNames.get(groupPosition);
                    recipeNames.remove(groupPosition);
                    recipeIngredients.remove(groupPosition);

                    Recipe grabRecipe = dbHelper.getRecipe(groceryName);
                    grabRecipe.setInList(false);
                    dbHelper.updateRecipe(grabRecipe);

                    dbHelper.deleteGrocery(groceryName);
                    notifyDataSetChanged();


                    //**************************************************************************************
                    //Creating a new intent works, but I know this solution probably isn't the best practice
                    //I would like to figure out a way to call notifydatasetchanged from here.
                    //**************************************************************************************

                    //Intent i = new Intent(mContext, ExpandableListViewActivity.class);
                    //mContext.startActivity(i);
                }
            });


        TextView lblListHeader = (TextView) convertView.findViewById(R.id.expandable_list_recipe_header);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);



        return convertView;
    }



    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
