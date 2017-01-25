package com.maragues.menu_planner.ui.meal.editor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.ui.common.BaseLoggedInActivity;
import com.maragues.menu_planner.ui.recipe.editor.EditRecipeActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.maragues.menu_planner.ui.recipe.editor.EditRecipeActivity.EXTRA_RESULT_RECIPE;

public class AddRecipeToMealActivity
        extends BaseLoggedInActivity<AddRecipeToMealPresenter, IAddRecipeToMeal>
        implements IAddRecipeToMeal {

  @BindView(R.id.recipe_list_suggestions)
  RecyclerView recyclerView;

  private final List<Recipe> recipes = new ArrayList<>();

  private final RecipeSuggesterAdapter adapter = new RecipeSuggesterAdapter(recipes);

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

    setupList();
  }

  @OnClick(R.id.add_recipe_to_meal_fab)
  void onAddRecipeClicked() {
    getPresenter().onCreateRecipeClicked();
  }

  private void setupList() {
    adapter.recipeClickedObservable().subscribe(recipe -> getPresenter().onRecipeClicked(recipe));

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
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
  public void showRecipeList(List<Recipe> newRecipes) {
    recipes.clear();
    recipes.addAll(newRecipes);

    adapter.notifyDataSetChanged();
  }

  @Override
  public void navigateToCreateRecipe(String key) {
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
