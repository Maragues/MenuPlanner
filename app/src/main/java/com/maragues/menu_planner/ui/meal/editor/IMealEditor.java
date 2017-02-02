package com.maragues.menu_planner.ui.meal.editor;

import com.maragues.menu_planner.model.RecipeMeal;
import com.maragues.menu_planner.ui.common.IBaseLoggedInView;

import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;
import net.grandcentrix.thirtyinch.distinctuntilchanged.DistinctUntilChanged;

import java.util.Collection;

/**
 * Created by miguelaragues on 17/1/17.
 */

public interface IMealEditor extends IBaseLoggedInView {
  @CallOnMainThread
  void navigateToAddRecipe();

  @CallOnMainThread
  void hideEmptyLayout();

  @CallOnMainThread
  void showEmptyLayout();

  @CallOnMainThread
  @DistinctUntilChanged
  void showRecipes(Collection<RecipeMeal> recipes);

  @CallOnMainThread
  void finish();
}
