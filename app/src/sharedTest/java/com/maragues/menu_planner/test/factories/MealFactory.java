package com.maragues.menu_planner.test.factories;

import com.maragues.menu_planner.model.Meal;

/**
 * Created by miguelaragues on 23/1/17.
 */

public abstract class MealFactory {
  public static final String MEAL_ID = "blabla keykey";

  private MealFactory(){}

  public static Meal base(){
    return Meal.empty()
            .withId(MEAL_ID)
            .withUid(UserFactory.DEFAULT_UID);
  }
}
