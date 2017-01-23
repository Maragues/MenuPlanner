package com.maragues.menu_planner.test.factories;

import com.maragues.menu_planner.model.MealInstance;

import org.threeten.bp.LocalDateTime;

/**
 * Created by miguelaragues on 23/1/17.
 */

public abstract class MealInstanceFactory {
  private MealInstanceFactory() {
  }

  public static MealInstance base(LocalDateTime dateTime) {
    return MealInstance.fromLocalDateTime(dateTime);
  }
}
