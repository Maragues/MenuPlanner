package com.maragues.menu_planner.ui.meal.editor;

import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.ui.common.IBaseLoggedInView;

import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;
import net.grandcentrix.thirtyinch.distinctuntilchanged.DistinctUntilChanged;

/**
 * Created by miguelaragues on 20/1/17.
 */
interface IAddRecipeToMeal extends IBaseLoggedInView {
  @CallOnMainThread
  @DistinctUntilChanged
  void showRecipeList();

  @CallOnMainThread
  void navigateToCreateRecipe();

  @CallOnMainThread
  void finish();

  void storeResult(Recipe recipe);
}
