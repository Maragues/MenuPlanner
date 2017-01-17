package com.maragues.menu_planner.ui.suggested_meals;

import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.ui.common.IBaseLoggedInView;

import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;
import net.grandcentrix.thirtyinch.distinctuntilchanged.DistinctUntilChanged;

import java.util.List;

/**
 * Created by miguelaragues on 17/1/17.
 */

public interface ISuggested extends IBaseLoggedInView {
  @CallOnMainThread
  void navigateToCreateMeal();

  @CallOnMainThread
  @DistinctUntilChanged
  void showSuggestedMeals(List<Meal> meals);

  void finish();
}
