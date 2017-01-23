package com.maragues.menu_planner.test.factories;

import com.maragues.menu_planner.model.Recipe;

/**
 * Created by miguelaragues on 23/1/17.
 */

public abstract class RecipeFactory {
  public static final String RECIPE_NAME = "Manitas de cerdo";
  public static final String RECIPE_ID = "blabla keykey";

  private RecipeFactory(){}

  public static Recipe base(){
    return Recipe.empty(RECIPE_NAME)
            .withId(RECIPE_ID)
            .withGroupId(GroupFactory.DEFAULT_GROUP_ID)
            .withUid(UserFactory.DEFAULT_UID);
  }
}
