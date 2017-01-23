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