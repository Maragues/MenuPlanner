package com.maragues.menu_planner.model.providers.firebase;

import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.MealInstanceLabel;
import com.maragues.menu_planner.test.factories.GroupFactory;
import com.maragues.menu_planner.test.factories.MealInstanceFactory;

import org.junit.Test;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by miguelaragues on 23/1/17.
 */
public class MealInstanceProviderFirebaseTest extends BaseProviderFirebaseTest<MealInstanceProviderFirebase> {

  @Override
  protected MealInstanceProviderFirebase createProvider() {
    return new MealInstanceProviderFirebase();
  }

  @Test
  public void getMealInstanceReference_usesCorrectPath() {
    LocalDateTime dateTime = LocalDateTime.now().with(MealInstanceLabel.DINNER.time());
    provider.getMealInstanceReference(MealInstanceFactory.base(dateTime));

    verify(databaseReference).child(eq(MealInstanceProviderFirebase.MEAL_INSTANCES_KEY));
    verify(databaseReference).child(eq(GroupFactory.DEFAULT_GROUP_ID));
    verify(databaseReference).child(eq(String.valueOf(dateTime.toInstant(ZoneOffset.UTC).toEpochMilli())));
  }

  /*
  QUERies
   */
  @Test
  public void listQuery_usesCorrectPath() {
    provider.listQuery();

    verify(databaseReference).child(eq(MealInstanceProviderFirebase.MEAL_INSTANCES_KEY));
    verify(databaseReference).child(eq(GroupFactory.DEFAULT_GROUP_ID));
  }

  @Test
  public void listBetween_usesCorrectPath() {
    LocalDateTime tstart = LocalDateTime.now();
    LocalDateTime tend = tstart.plusDays(45);

    provider.listBetweenQuery(tstart, tend);

    verify(databaseReference).startAt(eq(String.valueOf(tstart.toInstant(ZoneOffset.UTC).toEpochMilli())));
    verify(databaseReference).endAt(eq(String.valueOf(tend.toInstant(ZoneOffset.UTC).toEpochMilli())));
  }

  @Test
  public void listBetween_ordersOnlyOnce() {
    LocalDateTime tstart = LocalDateTime.now();
    LocalDateTime tend = tstart.plusDays(45);

    provider.listBetweenQuery(tstart, tend);

    verify(databaseReference, times(1)).orderByKey();
  }

  /*
  SYNCHRONIZABLE TO MAP
   */
  @Test(expected = IllegalArgumentException.class)
  public void synchronizableToMap_noId() {
    provider.synchronizableToMap(MealInstanceFactory.base(LocalDateTime.now()).withId(null));
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
    int hour = 12;
    int minutes = 33;
    MealInstance mealInstance = MealInstanceFactory
            .base(dateTime.withHour(hour).withMinute(minutes))
            .withId(key);
    Map<String, Object> map = provider.synchronizableToMap(mealInstance);

    String expectedPath = "/" + MealInstanceProviderFirebase.MEAL_INSTANCES_KEY
            + "/" + GroupFactory.DEFAULT_GROUP_ID
            + "/" + mealInstance.dateTime().toInstant(ZoneOffset.UTC).toEpochMilli();

    assertTrue(
            "Expected " + expectedPath + ", got " + map.keySet().iterator().next(),
            map.keySet().contains(expectedPath)
    );
  }
}