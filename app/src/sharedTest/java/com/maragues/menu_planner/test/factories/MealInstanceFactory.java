package com.maragues.menu_planner.test.factories;

import com.maragues.menu_planner.model.MealInstance;

import org.threeten.bp.LocalDateTime;

/**
 * Created by miguelaragues on 23/1/17.
 */

public abstract class MealInstanceFactory {
  private MealInstanceFactory() {
  }

  public static MealInstance withRecipes(LocalDateTime dateTime) {
    return base(dateTime).fromMeal(MealFactory.withRecipes());
  }

  public static MealInstance base(LocalDateTime dateTime) {
    return MealInstance.fromLocalDateTime(dateTime);
  }

  public static MealInstance base() {
    return base(LocalDateTime.now());
  }
}
