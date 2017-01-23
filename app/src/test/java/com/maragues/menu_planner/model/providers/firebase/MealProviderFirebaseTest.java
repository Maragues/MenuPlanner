package com.maragues.menu_planner.model.providers.firebase;

import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.test.factories.GroupFactory;
import com.maragues.menu_planner.test.factories.MealFactory;

import org.junit.Test;

import java.util.Map;

import io.reactivex.observers.TestObserver;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

/**
 * Created by miguelaragues on 23/1/17.
 */
public class MealProviderFirebaseTest extends BaseProviderFirebaseTest<MealProviderFirebase> {

  @Override
  protected MealProviderFirebase createProvider() {
    return new MealProviderFirebase();
  }

  @Test
  public void create_assingsKey() {
    String expectedKey = "my key";
    Meal recipe = MealFactory.base();
    doReturn(recipe.withId(expectedKey)).when(provider).assignKey(eq(recipe));

    mockSingleResponse();

    TestObserver<Meal> observer = new TestObserver<>();

    provider.create(recipe).subscribe(observer);

    observer.assertComplete();
    assertEquals(expectedKey, observer.values().get(0).id());
  }

  /*
  SYNCHRONIZABLE TO MAP
   */
  @Test(expected = IllegalArgumentException.class)
  public void synchronizableToMap_noId() {
    provider.synchronizableToMap(Meal.empty());
  }

  @Test
  public void synchronizableToMap_updatesOnePath() {
    Map<String, Object> map = provider.synchronizableToMap(MealFactory.base());

    assertEquals(1, map.size());
  }

  @Test
  public void synchronizableToMap_createsRecipe() {
    String key = "my key";
    Map<String, Object> map = provider.synchronizableToMap(MealFactory.base().withId(key));

    String expectedPath = "/" + MealProviderFirebase.MEALS_KEY + "/" + GroupFactory.DEFAULT_GROUP_ID + "/" + key;

    assertTrue(map.keySet().contains(expectedPath));
  }
}