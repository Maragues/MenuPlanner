package com.maragues.menu_planner.model.providers.firebase;

import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.test.factories.GroupFactory;
import com.maragues.menu_planner.test.factories.MealInstanceFactory;

import org.junit.Test;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.temporal.WeekFields;

import java.util.Locale;
import java.util.Map;

import io.reactivex.observers.TestObserver;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

/**
 * Created by miguelaragues on 23/1/17.
 */
public class MealInstanceProviderFirebaseTest extends BaseProviderFirebaseTest<MealInstanceProviderFirebase> {

  @Override
  protected MealInstanceProviderFirebase createProvider() {
    return new MealInstanceProviderFirebase();
  }

  @Test
  public void create_assingsKey() {
    String expectedKey = "my key";
    MealInstance mealInstance = MealInstanceFactory.base(LocalDateTime.now());
    doReturn(mealInstance.withId(expectedKey)).when(provider).assignKey(eq(mealInstance));

    mockSingleResponse();

    TestObserver<MealInstance> observer = new TestObserver<>();

    provider.create(mealInstance).subscribe(observer);

    observer.assertComplete();
    assertEquals(expectedKey, observer.values().get(0).id());
  }

  /*
  SYNCHRONIZABLE TO MAP
   */
  @Test(expected = IllegalArgumentException.class)
  public void synchronizableToMap_noId() {
    provider.synchronizableToMap(MealInstanceFactory.base(LocalDateTime.MAX));
  }

  @Test
  public void synchronizableToMap_updatesOnePath() {
    Map<String, Object> map = provider.synchronizableToMap(
            MealInstanceFactory.base(LocalDateTime.now()).withId("bla")
    );

    assertEquals(1, map.size());
  }

  @Test
  public void synchronizableToMap_createsMealInstance() {
    String key = "my key";
    LocalDateTime dateTime = LocalDateTime.now();
    int weekOfYear = dateTime.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
    int dayOfWeek = dateTime.getDayOfWeek().getValue();
    int hour = 12;
    int minutes = 33;
    MealInstance mealInstance = MealInstanceFactory
            .base(dateTime.withHour(hour).withMinute(minutes))
            .withId(key);
    Map<String, Object> map = provider.synchronizableToMap(mealInstance);

    String expectedPath = "/" + MealInstanceProviderFirebase.MEAL_INSTANCES_KEY
            + "/" + GroupFactory.DEFAULT_GROUP_ID
            + "/" + weekOfYear
            + "/" + dayOfWeek
            + "/" + hour + ":" + minutes;

    assertTrue(
            "Expected " + expectedPath + ", got " + map.keySet().iterator().next(),
            map.keySet().contains(expectedPath)
    );
  }
}