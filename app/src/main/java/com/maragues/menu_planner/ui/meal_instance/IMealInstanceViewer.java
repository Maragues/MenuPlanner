package com.maragues.menu_planner.ui.meal_instance;

import com.maragues.menu_planner.ui.common.IBaseLoggedInView;

/**
 * Created by miguelaragues on 25/1/17.
 */

public interface IMealInstanceViewer extends IBaseLoggedInView {
  void showRecipes(String recipes);
}
