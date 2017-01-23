package com.maragues.menu_planner.test.factories;

import com.maragues.menu_planner.model.RecipeMeal;

import static com.maragues.menu_planner.test.factories.RecipeFactory.RECIPE_ID;
import static com.maragues.menu_planner.test.factories.RecipeFactory.RECIPE_NAME;

/**
 * Created by miguelaragues on 23/1/17.
 */

public abstract class RecipeMealFactory {

  private RecipeMealFactory(){}

  public static RecipeMeal base(){
    return RecipeMeal.empty(RECIPE_NAME, RECIPE_ID);
  }
}
