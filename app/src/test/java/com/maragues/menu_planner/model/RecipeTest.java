package com.maragues.menu_planner.model;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by miguelaragues on 23/1/17.
 */
public class RecipeTest {
  @Test
  public void testEmpty() {
    assertNotNull(Recipe.empty(""));
  }

}