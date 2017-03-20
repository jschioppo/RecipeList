package groceryproject.jacob.com.recipelist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Jacob on 1/12/2017.
 */


public class ExpandableIngredientListAdapter extends BaseExpandableListAdapter{

    private Context mContext;
    private List<String> recipeNames; // header titles
    private HashMap<String, List<String>> recipeIngredients;
    private Button mDeleteButton;
    private static int[][] checkState;
    //private SharedPreferences.Editor mEditPrefs;
    //private SharedPreferences mPreferences;
    private int totalLength;
    private static final char NEXT_ITEM = ' ';


    public ExpandableIngredientListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData){
        this.mContext = context;
        this.recipeNames = listDataHeader;
        this.recipeIngredients = listChildData;

        int[] lengths = new int[recipeNames.size()];
        //Log.d("Tag", Integer.toString(recipeNames.size()));

        for(int i = 0; i <  recipeNames.size(); i++){
            lengths[i] = recipeIngredients.get(recipeNames.get(i)).size();
            //Log.d("Tag", "Index " + i + ": " + Integer.toString(recipeIngredients.get(recipeNames.get(i)).size()));
            totalLength += lengths[i];
        }

        try{
            checkState = getCheckState();
        }
        catch (Exception e){
            if(checkState == null){
                checkState = new int[recipeNames.size()][totalLength];
            }
            else{
                int [][] checkstateOld = checkState;
                checkState = new int[recipeNames.size()][totalLength];
                for(int i = 0; i < checkstateOld.length; i++){
                    for(int n = 0; n < checkstateOld[i].length; n++){
                        if (checkstateOld[i][n] == 1){
                            checkState[i][n] = 1;
                        }
                    }
                }
            }
        }


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
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        LayoutInflater infalInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.expandable_list_view_item, null);

        TextView txtListChild = (TextView) convertView.findViewById(R.id.expandable_list_recipe_ingredient_item);

        txtListChild.setText(childText);



        final CheckBox ingredientCheck = (CheckBox) convertView.findViewById(R.id.expandable_list_view_itemm_check_box);
        ingredientCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ingredientCheck.isChecked()) {
                    checkState[groupPosition][childPosition] = 0;
                }
                else {
                    //Log.d("Tag", Integer.toString(groupPosition) + " , " + Integer.toString(childPosition));
                    checkState[groupPosition][childPosition] = 1;
                }

                setCheckState(checkState);
            }
        });


        if(checkState[groupPosition][childPosition] == 1){
            ingredientCheck.setChecked(true);
        }

        return convertView;
    }


    public void setCheckState(int [][] s){
        String str = serialize(s);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("boolList", str);
        editor.clear();
        editor.apply();

        Log.d("Tag", "Life: ");
        for(int i = 0; i < recipeNames.size(); i++){
            for(int n = 0; n < recipeIngredients.get(recipeNames.get(i)).size(); n++){
                Log.d("Tag", String.valueOf(s[i][n]));
            }
        }
    }

    public int[][] getCheckState() throws IOException{
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String str = prefs.getString("boolList", "");
        int [][] s = deserialize(str);

        Log.d("Tag", "Death: ");
        for(int i = 0; i < recipeNames.size(); i++){
            for(int n = 0; n < recipeIngredients.get(recipeNames.get(i)).size(); n++){
                Log.d("Tag", String.valueOf(s[i][n]));
            }
        }

        return s;


    }

    private static String serialize(int[][] array) {
        StringBuilder s = new StringBuilder();
        s.append(array.length).append(NEXT_ITEM);

        for(int[] row : array) {
            s.append(row.length).append(NEXT_ITEM);

            for(int item : row) {
                s.append(String.valueOf(item)).append(NEXT_ITEM);
            }
        }

        return s.toString();
    }

    private static int[][] deserialize(String str) throws IOException {
        StreamTokenizer tok = new StreamTokenizer(new StringReader(str));
        tok.resetSyntax();
        tok.wordChars('0', '1');
        tok.whitespaceChars(NEXT_ITEM, NEXT_ITEM);
        tok.parseNumbers();

        tok.nextToken();

        int     rows = (int) tok.nval;
        int[][] out  = new int[rows][];

        for(int i = 0; i < rows; i++) {
            tok.nextToken();

            int   length = (int) tok.nval;
            int[] row    = new int[length];
            out[i]       = row;

            for(int j = 0; j < length; j++) {
                tok.nextToken();
                row[j] = (int) tok.nval;
            }
        }

        return out;
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

                    //for(int i = 0; i < checkState[groupPosition].length; i++){
                        //checkState[groupPosition][i] = 0;
                    //}

                    dbHelper.deleteGrocery(groceryName);
                    adjustArray(groupPosition);
                    setCheckState(checkState);
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


    public void adjustArray(int index){
        List<int[]> boolList = new ArrayList<int[]>(Arrays.asList(checkState));
        boolList.remove(index);
        checkState = boolList.toArray(new int[][]{});
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
