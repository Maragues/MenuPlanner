package com.maragues.menu_planner.model;

import org.junit.Test;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by miguelaragues on 23/1/17.
 */
public class MealInstanceTest {

  @Test
  public void fromLocalDate() {
    LocalDateTime expectedDateTime = LocalDate.now().atStartOfDay();

    MealInstance mealInstance = MealInstance.fromLocalDate(LocalDate.now());

    assertNotNull(mealInstance);

    assertEquals(expectedDateTime, mealInstance.dateTime());
  }

  @Test
  public void fromLocalDateTime() {
    LocalDateTime expectedDateTime = LocalDateTime.now().minusMinutes(7);

    MealInstance mealInstance = MealInstance.fromLocalDateTime(expectedDateTime);

    assertNotNull(mealInstance);

    assertEquals(expectedDateTime, mealInstance.dateTime());
  }
}