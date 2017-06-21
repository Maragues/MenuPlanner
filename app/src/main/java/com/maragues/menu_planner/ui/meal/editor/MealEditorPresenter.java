package com.maragues.menu_planner.ui.meal.editor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
  private final String expectedMealId;

  Meal meal;

  MealEditorPresenter(@Nullable String expectedMealId) {
    this.expectedMealId = expectedMealId;
  }

  @Override
  protected void onAttachView(@NonNull IMealEditor view) {
    super.onAttachView(view);

    if (meal == null)
      loadMeal(expectedMealId);
    else
      renderMeal();
  }

  void loadMeal(@Nullable String mealId) {
    if (App.appComponent.textUtils().isEmpty(mealId)) {
      onNewMeal(mealId);
    } else {
      //noinspection ConstantConditions
      disposables.add(App.appComponent.mealProvider().get(mealId)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .doFinally(() -> {
                if (meal != null) {
                  renderMeal();
                } else {
                  onNewMeal(mealId);
                }
              })
              .subscribe(this::onMealLoaded, this::onErrorLoadingMeal));
    }
  }

  void onNewMeal(String mealId) {
    meal = Meal.empty().withId(mealId);
  }

  void onErrorLoadingMeal(Throwable throwable) {
    // TODO: 20/1/17
  }

  void onMealLoaded(Meal meal) {
    this.meal = meal;
  }

   void renderMeal() {
    if (meal.recipes().isEmpty()) {
      showEmptyLayout();
    } else {
      hideEmptyLayout();

      showRecipes();
    }
  }

  private void showRecipes() {
    if (getView() != null) {
      getView().showRecipes(meal.recipeCollection());
    } else {
      sendToView(v -> v.showRecipes(meal.recipeCollection()));
    }
  }

  void onSaveClicked() {
    disposables.add(App.appComponent.mealProvider().create(meal)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(ignored -> finish())
            .subscribe()
    );
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
