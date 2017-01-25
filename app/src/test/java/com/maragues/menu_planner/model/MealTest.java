package com.maragues.menu_planner.model;

import com.maragues.menu_planner.test.factories.RecipeFactory;
import com.maragues.menu_planner.ui.test.BaseUnitTest;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by miguelaragues on 23/1/17.
 */
public class MealTest extends BaseUnitTest {
  @Test
  public void testEmpty() {
    assertNotNull(Meal.empty());
  }

  /*
  ADD RECIPE
   */
  @Test(expected = IllegalArgumentException.class)
  public void addRecipe_failsIfRecipeWithoutId() {
    Meal meal = Meal.empty();

    assertTrue(meal.recipes().isEmpty());

    meal.withNewRecipe(Recipe.empty("bla"));
  }

  @Test
  public void addRecipe() {
    Meal meal = Meal.empty();

    assertTrue(meal.recipes().isEmpty());

    Recipe recipe = RecipeFactory.base();
    meal = meal.withNewRecipe(recipe);

    assertEquals(1, meal.recipes().size());
    RecipeMeal recipeMeal = meal.recipes().get(recipe.id());

    assertEquals(recipe.name(), recipeMeal.name());
    assertEquals(recipe.id(), recipeMeal.recipeId());
  }
}