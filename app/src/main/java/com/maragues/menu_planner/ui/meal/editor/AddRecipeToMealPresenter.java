package com.maragues.menu_planner.ui.meal.editor;

import android.support.annotation.NonNull;

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
  This flag allows us to react to a recipe being created from EditRecipeActivity, otherwise
  we'd need the fragment to invoke a listener or something like that
   */
  private boolean firstLoad;

  @Override
  protected void onCreate() {
    super.onCreate();
  }

  @Override
  protected void onAttachView(@NonNull IAddRecipeToMeal view) {
    super.onAttachView(view);
  }

  public void loadRecipes() {
    firstLoad = true;

    App.appComponent.recipeProvider().list()
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterNext(recipes -> firstLoad = false)
            .subscribe(this::onRecipesLoaded);
  }

  private void onRecipesLoaded(List<Recipe> recipes) {
    if (recipes.isEmpty()) {
      if (getView() != null) {
        getView().navigateToCreateRecipe();
        getView().finish();
      } else {
        sendToView(view -> view.navigateToCreateRecipe());
        sendToView(view -> view.finish());
      }
    } else {
      if (firstLoad) {
        onRecipesFirstLoad();
      } else {
        onRecipeCreated(recipes.get(recipes.size() - 1));
      }
    }
  }

  private void onRecipesFirstLoad() {
    if (getView() != null) {
      getView().showRecipeList();
    } else {
      sendToView(view -> view.showRecipeList());
    }
  }

  public void onRecipeCreated(Recipe recipe) {
    if (getView() != null) {
      getView().storeResult(recipe);

      getView().finish();
    } else {
      sendToView(v -> v.storeResult(recipe));
      sendToView(v -> v.finish());
    }
  }

  public void onRecipeResultArrived() {
    if (getView() != null) {
      getView().finish();
    } else {
      sendToView(v -> v.finish());
    }
  }
}
