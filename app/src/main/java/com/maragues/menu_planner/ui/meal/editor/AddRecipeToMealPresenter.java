package com.maragues.menu_planner.ui.meal.editor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.ui.common.BaseLoggedInPresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by miguelaragues on 20/1/17.
 */
public class AddRecipeToMealPresenter extends BaseLoggedInPresenter<IAddRecipeToMeal> {

  /*
  Stores the key of the recipe the user would create if he did
   */
  public String expectedKey;

  @Override
  protected void onCreate() {
    super.onCreate();
  }

  @Override
  protected void onAttachView(@NonNull IAddRecipeToMeal view) {
    super.onAttachView(view);
  }

  public void loadRecipes() {

    App.appComponent.recipeProvider().list()
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onRecipesLoaded);
  }

  void onRecipesLoaded(List<Recipe> recipes) {
    if (recipes.isEmpty()) {
      onCreateRecipeClicked();
    } else {
      if (listContainsExpectedId(recipes)) {
        onRecipeAdded(extractExpectedRecipe(recipes));

        expectedKey = null;
      } else {
        onRecipesFirstLoad(recipes);
      }
    }
  }

  @Nullable
  private Recipe extractExpectedRecipe(List<Recipe> recipes) {
    if (recipes != null && !recipes.isEmpty()) {
      for (int i = 0; i < recipes.size(); i++) {
        Recipe recipe = recipes.get(i);
        if (recipe.id() != null && recipe.id().equals(expectedKey))
          return recipe;
      }
    }

    return null;
  }

  boolean listContainsExpectedId(List<Recipe> recipes) {
    if (recipes.isEmpty() || expectedKey == null)
      return false;

    for (int i = 0; i < recipes.size(); i++) {
      if (recipes.get(i).id() != null && recipes.get(i).id().equals(expectedKey))
        return true;
    }

    return false;
  }

  private void onRecipesFirstLoad(List<Recipe> recipes) {
    if (getView() != null) {
      getView().showRecipeList(recipes);
    } else {
      sendToView(view -> view.showRecipeList(recipes));
    }
  }

  public void onRecipeAdded(Recipe recipe) {
    if (recipe != null) {
      if (getView() != null) {
        getView().storeResult(recipe);

        getView().finish();
      } else {
        sendToView(v -> v.storeResult(recipe));
        sendToView(IAddRecipeToMeal::finish);
      }
    }
  }

  void onRecipeResultArrived() {
    if (getView() != null) {
      getView().finish();
    } else {
      sendToView(IAddRecipeToMeal::finish);
    }
  }

  void onRecipeClicked(@NonNull Recipe recipe) {
    onRecipeAdded(recipe);
  }

  void onCreateRecipeClicked() {
    App.appComponent.recipeProvider().getKey()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(key -> {
              expectedKey = key;

              navigateToCreateRecipe(key);
            })
            .subscribe();

  }

  private void navigateToCreateRecipe(String key) {
    if (getView() != null) {
      getView().navigateToCreateRecipe(key);
      ;
    } else {
      sendToView(v -> v.navigateToCreateRecipe(key));
    }
  }
}
