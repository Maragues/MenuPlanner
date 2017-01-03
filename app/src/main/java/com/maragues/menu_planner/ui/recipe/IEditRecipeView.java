package com.maragues.menu_planner.ui.recipe;

import com.maragues.menu_planner.ui.IBaseLoggedInView;

/**
 * Created by miguelaragues on 3/1/17.
 */
public interface IEditRecipeView extends IBaseLoggedInView{
  String getRecipeTitle();

  String getRecipeDescription();

  void finish();
}
