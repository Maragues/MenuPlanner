package com.maragues.menu_planner.ui.meal.editor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.ui.common.BaseLoggedInPresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by miguelaragues on 17/1/17.
 */

public class MealEditorPresenter extends BaseLoggedInPresenter<IMealEditor> {
  Meal meal;

  @Override
  protected void onAttachView(@NonNull IMealEditor view) {
    super.onAttachView(view);

    loadMeal(getView().getMealId());
  }

  private void loadMeal(@Nullable String mealId) {
    if (App.appComponent.textUtils().isEmpty(mealId)) {
      onNewMeal();
    } else {
      App.appComponent.mealProvider().get(mealId)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .doOnSuccess(m -> onMealLoaded(m))
              .doOnError(this::onErrorLoadingMeal);
    }
  }

  private void onErrorLoadingMeal(Throwable throwable) {
    // TODO: 20/1/17
  }

  void onMealLoaded(Meal meal) {
    this.meal = meal;

    if (!meal.recipes().isEmpty())
      getView().hideEmptyLayout();
  }

  void onNewMeal() {

  }

  void onSaveClicked() {

  }

  void onAddRecipeClicked() {
    getView().navigateToAddRecipe();
  }

  public void onRecipeAdded(Recipe recipe) {
    Toast.makeText(App.appComponent.context(), "Recipe added " + recipe.name(), Toast.LENGTH_SHORT).show();
  }
}
