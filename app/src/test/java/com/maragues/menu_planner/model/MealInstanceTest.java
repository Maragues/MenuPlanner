package com.maragues.menu_planner.model;

import com.maragues.menu_planner.model.providers.IMealInstanceLabelProvider;
import com.maragues.menu_planner.test.factories.MealFactory;
import com.maragues.menu_planner.test.factories.MealInstanceFactory;
import com.maragues.menu_planner.ui.test.BaseUnitTest;

import org.junit.Test;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by miguelaragues on 23/1/17.
 */
public class MealInstanceTest extends BaseUnitTest {

  @Test
  public void fromLocalDateTime() {
    LocalDateTime expectedDateTime = LocalDateTime.now().minusMinutes(7);

    MealInstance mealInstance = MealInstance.fromLocalDateTime(expectedDateTime);

    assertNotNull(mealInstance);

    assertEquals(expectedDateTime, mealInstance.dateTime());
    assertEquals(String.valueOf(expectedDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()), mealInstance.id());
  }

  @Test
  public void withLabel_updatesIDToEpochMillis() {
    LocalDateTime initialTime = LocalDateTime.now().withHour(1).withMinute(54);
    MealInstance mealInstance = MealInstance.fromLocalDateTime(initialTime);

    String oldId = mealInstance.id();

    mealInstance = mealInstance.withLabel(MealInstanceLabel.DINNER);
    assertNotSame(oldId, mealInstance.id());

    LocalDateTime expectedDateTime = initialTime.with(MealInstanceLabel.DINNER.time());
    assertEquals(String.valueOf(expectedDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()), mealInstance.id());
  }

  @Test
  public void withLabel_dinner() {
    MealInstance mealInstance = MealInstance.fromLocalDateTime(LocalDateTime.now().withHour(1).withMinute(54));

    assertNotSame(20, mealInstance.dateTime().getHour());
    assertNotSame(0, mealInstance.dateTime().getMinute());
    assertNotSame(IMealInstanceLabelProvider.DINNER_ID, mealInstance.labelId());

    mealInstance = mealInstance.withLabel(MealInstanceLabel.DINNER);

    assertEquals(20, mealInstance.dateTime().getHour());
    assertEquals(0, mealInstance.dateTime().getMinute());
    assertEquals(IMealInstanceLabelProvider.DINNER_ID, mealInstance.labelId());
  }

  @Test
  public void withLabel_lunch() {
    MealInstance mealInstance = MealInstance.fromLocalDateTime(LocalDateTime.now().withHour(1).withMinute(54));

    assertNotSame(12, mealInstance.dateTime().getHour());
    assertNotSame(0, mealInstance.dateTime().getMinute());
    assertNotSame(IMealInstanceLabelProvider.LUNCH_ID, mealInstance.labelId());

    mealInstance = mealInstance.withLabel(MealInstanceLabel.LUNCH);

    assertEquals(12, mealInstance.dateTime().getHour());
    assertEquals(0, mealInstance.dateTime().getMinute());
    assertEquals(IMealInstanceLabelProvider.LUNCH_ID, mealInstance.labelId());
  }

  @Test
  public void fromMeal_storesMealId() {
    Meal meal = MealFactory.base();
    MealInstance mealInstance = MealInstanceFactory.base(LocalDateTime.now());

    assertNull(mealInstance.mealId());

    mealInstance = mealInstance.fromMeal(meal);

    assertEquals(meal.id(), mealInstance.mealId());
  }

  @Test
  public void fromMeal_storesRecipes() {
    String firstRecipe = "Puerros";
    String firstRecipeId = "key1";
    String secondRecipe = "Marioposas";
    String secondRecipeId = "key2";
    Map<String, RecipeMeal> recipes = new HashMap<>();
    recipes.put(firstRecipeId, RecipeMeal.empty(firstRecipeId, firstRecipe));
    recipes.put(secondRecipeId, RecipeMeal.empty(secondRecipeId, secondRecipe));

    Meal meal = MealFactory.base().withRecipes(recipes);
    MealInstance mealInstance = MealInstanceFactory.base(LocalDateTime.now());

    assertTrue(mealInstance.recipes().isEmpty());

    mealInstance = mealInstance.fromMeal(meal);

    assertNotNull(mealInstance.recipes());

    assertNotSame(meal.recipes(), mealInstance.recipes());

    assertEquals(firstRecipeId, mealInstance.recipes().get(firstRecipeId).recipeId());
    assertEquals(firstRecipe, mealInstance.recipes().get(firstRecipeId).name());

    assertEquals(secondRecipeId, mealInstance.recipes().get(secondRecipeId).recipeId());
    assertEquals(secondRecipe, mealInstance.recipes().get(secondRecipeId).name());
  }

  /*@Test
  public void toFirebaseValue_usesCorrectTimeFormat() {
    String key = "my key";
    LocalDateTime dateTime = LocalDateTime.now();
    int weekOfYear = dateTime.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
    int dayOfWeek = dateTime.getDayOfWeek().getValue();
    int hour = 12;
    int minutes = 33;
    MealInstance mealInstance = MealInstanceFactory
            .base(dateTime.withHour(hour).withMinute(minutes))
            .withLabelId(IMealInstanceLabelProvider.DINNER_ID)
            .withId(key);

    $$AutoValue_MealInstance.FirebaseValue firebaseMealInstance = ($$AutoValue_MealInstance.FirebaseValue) mealInstance.toFirebaseValue();

    assertEquals(hour + ":" + minutes, firebaseMealInstance.());
    assertEquals(IMealInstanceLabelProvider.DINNER_ID, firebaseMealInstance.getLabelId());
  }*/
}