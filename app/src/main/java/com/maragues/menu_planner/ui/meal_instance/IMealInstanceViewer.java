package com.maragues.menu_planner.ui.meal_instance;

import com.maragues.menu_planner.ui.common.IBaseLoggedInView;

import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;
import net.grandcentrix.thirtyinch.distinctuntilchanged.DistinctUntilChanged;

/**
 * Created by miguelaragues on 25/1/17.
 */

public interface IMealInstanceViewer extends IBaseLoggedInView {
  @CallOnMainThread
  @DistinctUntilChanged
  void showRecipes(String recipes);

  @CallOnMainThread
  void showErrorFetchingMealInstance();
}
