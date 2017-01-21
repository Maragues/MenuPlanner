package com.maragues.menu_planner.ui.meal.editor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.ui.common.BaseLoggedInPresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by miguelaragues on 17/1/17.
 */

public class MealEditorPresenter extends BaseLoggedInPresenter<IMealEditor> {
  Meal meal;

  MealInstance mealInstance;

  MealEditorPresenter(@Nullable MealInstance mealInstance) {
    this.mealInstance = mealInstance;
  }

  @Override
  protected void onAttachView(@NonNull IMealEditor view) {
    super.onAttachView(view);

    if (meal == null)
      loadMeal(getView().getMealId());
    else
      renderMeal();
  }

  private void loadMeal(@Nullable String mealId) {
    if (App.appComponent.textUtils().isEmpty(mealId)) {
      onNewMeal();
    } else {
      App.appComponent.mealProvider().get(mealId)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .doOnSuccess(this::onMealLoaded)
              .doOnError(this::onErrorLoadingMeal);
    }
  }

  private void onErrorLoadingMeal(Throwable throwable) {
    // TODO: 20/1/17
  }

  void onMealLoaded(Meal meal) {
    this.meal = meal;

    renderMeal();
  }

  private void renderMeal() {
    if (meal.recipes().isEmpty()) {
      showEmptyLayout();
    } else {
      hideEmptyLayout();

      showRecipes();
    }
  }

  private void showRecipes() {
    if (getView() != null) {
      getView().showRecipes(meal.recipes());
    } else {
      sendToView(v -> v.showRecipes(meal.recipes()));
    }
  }

  void onNewMeal() {
    meal = Meal.emptyMeal();
  }

  void onSaveClicked() {
    App.appComponent.mealProvider().create(meal)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onMealSaved)
    ;
  }

  private void onMealSaved(Meal meal) {
    if (mealInstance != null) {
      App.appComponent.mealInstanceProvider().create(mealInstance.fromMeal(meal))
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(mealInstance -> {
                finish();
              })
      ;
    } else {
      finish();
    }
  }

  private void finish() {
    if (getView() != null) {
      getView().finish();
    } else {
      sendToView(IMealEditor::finish);
    }
  }

  void onAddRecipeClicked() {
    getView().navigateToAddRecipe();
  }

  public void onRecipeAdded(Recipe recipe) {
    meal = meal.withNewRecipe(recipe);

    renderMeal();
  }

  private void hideEmptyLayout() {
    if (getView() != null) {
      getView().hideEmptyLayout();
    } else {
      sendToView(IMealEditor::hideEmptyLayout);
    }
  }

  private void showEmptyLayout() {
    if (getView() != null) {
      getView().showEmptyLayout();
    } else {
      sendToView(IMealEditor::showEmptyLayout);
    }
  }
}
