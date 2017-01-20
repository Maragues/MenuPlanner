package com.maragues.menu_planner.ui.meal.editor;

import android.support.annotation.Nullable;

import com.maragues.menu_planner.ui.common.IBaseLoggedInView;

import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

/**
 * Created by miguelaragues on 17/1/17.
 */

public interface IMealEditor extends IBaseLoggedInView {
  @CallOnMainThread
  void navigateToAddRecipe();

  @Nullable
  String getMealId();

  @CallOnMainThread
  void hideEmptyLayout();

  @CallOnMainThread
  void showEmptyLayout();
}
