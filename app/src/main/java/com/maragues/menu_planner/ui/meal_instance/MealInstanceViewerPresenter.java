package com.maragues.menu_planner.ui.meal_instance;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.RecipeMeal;
import com.maragues.menu_planner.ui.common.BaseLoggedInPresenter;

import java.util.Iterator;

/**
 * Created by miguelaragues on 25/1/17.
 */

public class MealInstanceViewerPresenter extends BaseLoggedInPresenter<IMealInstanceViewer> {
  private final MealInstance mealInstance;

  MealInstanceViewerPresenter(@NonNull MealInstance mealInstance) {
    this.mealInstance = mealInstance;
  }

  @Override
  protected void onAttachView(@NonNull IMealInstanceViewer view) {
    super.onAttachView(view);

    showRecipes();
  }

  void showRecipes() {
    String recipeText = "";

    Iterator<RecipeMeal> it = mealInstance.recipeCollection().iterator();
    while (it.hasNext()) {
      recipeText += it.next().name();
      if (it.hasNext()) recipeText += "\n";
    }

    getView().showRecipes(recipeText);
  }
}
