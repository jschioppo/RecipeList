package groceryproject.jacob.com.recipelist;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jacob on 9/14/2016.
 */
//Implements serilizable so that it can be passed in a bundle as a serializable object
public class Recipe implements Parcelable {

    //These are all of the qualities a recipe contains, we will create an arraylist of this in the activity
    private String mRecipeName;
    private int mID;
    private String mServings;
    private String mPrepTime;
    private String mCookTime;
    private boolean isInList;

    private List<String> mIngredients;
    private List<String> mDirections;


    public Recipe(){

    }

    public Recipe(int id, String name, String serving, String prep, String cook, List<String>
                  ingredientsList, List<String> directionsList){

        this.mID = id;
        this.mRecipeName = name;
        this.mServings = serving;
        this.mPrepTime = prep;
        this.mCookTime = cook;
        this.mIngredients = ingredientsList;
        this.mDirections = directionsList;
        this.isInList = false;
    }

    public Recipe(String name, String serving, String prep, String cook, List<String>
            ingredientsList, List<String> directionsList){

        this.mRecipeName = name;
        this.mServings = serving;
        this.mPrepTime = prep;
        this.mCookTime = cook;
        this.mIngredients = ingredientsList;
        this.mDirections = directionsList;
        this.isInList = false;
    }


    public String getRecipeName() {
        return mRecipeName;
    }

    public int getID() {
        return mID;
    }

    public void setID(int id){
        mID = id;
    }

    public String getServings() {
        return mServings;
    }

    public String getPrepTime() {
        return mPrepTime;
    }


    public void setRecipeName(String recipeName) {
        mRecipeName = recipeName;
    }

    public void setServingSize(String servings) {
        mServings = servings;
    }

    public void setPrepTime(String prepTime) {
        mPrepTime = prepTime;
    }

    public void setServings(String servings) {
        mServings = servings;
    }

    public List<String> getIngredients() {
        return mIngredients;
    }

    public List<String> getDirections() {
        return mDirections;
    }

    public String getCookTime() {
        return mCookTime;
    }

    public void setCookTime(String cookTime) {
        mCookTime = cookTime;
    }

    public void setIngredients(List<String> ingredients) {
        mIngredients = ingredients;
    }

    public void setDirections(List<String> directions) {
        mDirections = directions;
    }

    public boolean isInList() {
        return isInList;
    }

    public void setInList(boolean inList) {
        isInList = inList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mRecipeName);
        dest.writeInt(this.mID);
        dest.writeString(this.mServings);
        dest.writeString(this.mPrepTime);
        dest.writeString(this.mCookTime);
        dest.writeByte(this.isInList ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.mIngredients);
        dest.writeStringList(this.mDirections);
    }

    protected Recipe(Parcel in) {
        this.mRecipeName = in.readString();
        this.mID = in.readInt();
        this.mServings = in.readString();
        this.mPrepTime = in.readString();
        this.mCookTime = in.readString();
        this.isInList = in.readByte() != 0;
        this.mIngredients = in.createStringArrayList();
        this.mDirections = in.createStringArrayList();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
