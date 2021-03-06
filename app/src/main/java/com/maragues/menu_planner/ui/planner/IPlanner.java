package com.maragues.menu_planner.ui.planner;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.ui.common.IBaseLoggedInView;

import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;
import net.grandcentrix.thirtyinch.distinctuntilchanged.DistinctUntilChanged;

/**
 * Created by miguelaragues on 13/1/17.
 */

interface IPlanner extends IBaseLoggedInView {

  @CallOnMainThread
  void askForMealInstanceLabel();

  @CallOnMainThread
  void navigateToSuggestedMeals(@NonNull MealInstance mealInstance);

  @CallOnMainThread
  void navigateToMealInstanceViewer(@NonNull MealInstance mealInstance);

  @CallOnMainThread
  void showIsLoading();

  @CallOnMainThread
  void hideIsLoading();

  @CallOnMainThread
  @DistinctUntilChanged
  void setHeader(String text);
}
