package groceryproject.jacob.com.recipelist;

/**
 * Created by Jacob on 9/27/2016.
 */
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder>{

    private List<Recipe> mRecipeSet;

    public RecipeListAdapter(List<Recipe> recipes){
        mRecipeSet = recipes;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //This is what will handle what happens when you click a recipe in the recycler view
        @Override
        public void onClick(View v){
            int position = getAdapterPosition();
            Intent i = new Intent(v.getContext(), RecipeTextView.class);
            Recipe selectedRecipe = mRecipeSet.get(position);
            i.putExtra("view_recipe_key", selectedRecipe);
            //Every view has a context, and to start the activity I must get that context
            v.getContext().startActivity(i);
        }

        public TextView mRecipeName;
        public TextView mPrepTime;
        public TextView mCookTime;
        public TextView mServingSize;
        public RelativeLayout mRecipeTextSection;


        public ViewHolder(View v) {
            super(v);
            mRecipeName = (TextView) v.findViewById(R.id.recipe_list_recycler_view_recipe_name);
            mServingSize = (TextView) v.findViewById(R.id.recipe_list_recycler_view_serving_size);
            mPrepTime = (TextView) v.findViewById(R.id.recipe_list_recycler_view_prep_time);
            mCookTime = (TextView) v.findViewById(R.id.recipe_list_recycler_view_cook_time);
            mRecipeTextSection = (RelativeLayout) v.findViewById(R.id.recycled_item_section_view);

            mRecipeTextSection.setOnClickListener(this);
        }

    }


    public void add(int position, Recipe item) {
        mRecipeSet.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Recipe item) {
        int position = mRecipeSet.indexOf(item);
        mRecipeSet.remove(position);
        notifyItemRemoved(position);
    }



    public RecipeListAdapter(ArrayList<Recipe> myRecipeset) {
        mRecipeSet = myRecipeset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecipeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item_recycled, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = mRecipeSet.get(position);
        String recipeName = recipe.getRecipeName();
        String prepTime = "Prep Time: " + String.valueOf(recipe.getPrepTime()) + " minutes";
        String cookTime = "Cook Time: " + String.valueOf(recipe.getCookTime()) + " minutes";
        String servingSize = "Serves: " + String.valueOf(recipe.getServings());

        holder.mRecipeName.setText(recipeName);

        //Only display values if they are not null
        if(recipe.getServings() != null) {
            holder.mServingSize.setText(servingSize);
        }
        if (recipe.getPrepTime() != null) {
            holder.mPrepTime.setText(prepTime);
        }
        if(recipe.getCookTime() != null) {
            holder.mCookTime.setText(cookTime);
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(mRecipeSet != null) {
            return mRecipeSet.size();
        }
        return 0;
    }



}
