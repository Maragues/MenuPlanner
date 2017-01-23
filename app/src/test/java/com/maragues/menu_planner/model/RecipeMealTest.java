package com.maragues.menu_planner.model;

import com.maragues.menu_planner.test.factories.RecipeFactory;
import com.maragues.menu_planner.ui.test.BaseUnitTest;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by miguelaragues on 23/1/17.
 */
public class RecipeMealTest extends BaseUnitTest {

  @Test
  public void testEmpty() {
    assertNotNull(RecipeMeal.empty("bla", "blo"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void fromRecipe_failsIfNoRecipeId() {
    RecipeMeal.fromRecipe(Recipe.empty(RecipeFactory.RECIPE_NAME));
  }

  @Test
  public void fromRecipe_fillsFields(){
    RecipeMeal recipeMeal = RecipeMeal.fromRecipe(RecipeFactory.base());

    assertEquals(RecipeFactory.RECIPE_ID, recipeMeal.recipeId());;
    assertEquals(RecipeFactory.RECIPE_NAME, recipeMeal.name());;
  }
}