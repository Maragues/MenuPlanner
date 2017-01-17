package com.maragues.menu_planner.model.providers;

import com.maragues.menu_planner.model.MealInstanceLabel;

/**
 * Created by miguelaragues on 17/1/17.
 */

public interface IMealInstanceLabelProvider extends IListableProvider<MealInstanceLabel> {
  String LUNCH_ID = "Lunch";
  String DINNER_ID = "Dinner";
}
