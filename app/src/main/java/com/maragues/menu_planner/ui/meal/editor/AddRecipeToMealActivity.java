package com.maragues.menu_planner.ui.meal.editor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.ui.common.BaseLoggedInActivity;
import com.maragues.menu_planner.ui.recipe.editor.EditRecipeActivity;
import com.maragues.menu_planner.ui.recipe.list.RecipeListFragment;

import static com.maragues.menu_planner.ui.recipe.editor.EditRecipeActivity.EXTRA_RESULT_RECIPE;

public class AddRecipeToMealActivity
        extends BaseLoggedInActivity<AddRecipeToMealPresenter, IAddRecipeToMeal>
        implements IAddRecipeToMeal {

  private static final String RECIPE_LIST_TAG = "recipe_list_tag";
  private static final int CREATE_RECIPE_CODE = 5;

  public static Intent createIntent(Context context) {
    return new Intent(context, AddRecipeToMealActivity.class);
  }

  @Nullable
  public static Recipe extractRecipe(Intent data) {
    return EditRecipeActivity.extractRecipe(data);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_recipe_to_meal);

    getPresenter().loadRecipes();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == CREATE_RECIPE_CODE && resultCode == RESULT_OK) {
      setResult(RESULT_OK, data);

      getPresenter().onRecipeResultArrived();
    }
  }

  @Override
  public void showRecipeList() {
    getSupportFragmentManager().beginTransaction()
            .replace(R.id.activity_add_recipe_to_meal, RecipeListFragment.newInstance(), RECIPE_LIST_TAG)
            .commit();
  }

  @Override
  public void navigateToCreateRecipe() {
    startActivityForResult(EditRecipeActivity.createAddIntent(this), CREATE_RECIPE_CODE);
  }

  @NonNull
  @Override
  public AddRecipeToMealPresenter providePresenter() {
    return new AddRecipeToMealPresenter();
  }


  @Override
  public void storeResult(Recipe recipe) {
    Intent data = new Intent();
    data.putExtra(EXTRA_RESULT_RECIPE, recipe);

    setResult(RESULT_OK, data);
  }
}
