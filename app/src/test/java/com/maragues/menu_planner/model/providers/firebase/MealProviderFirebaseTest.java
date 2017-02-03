package com.maragues.menu_planner.model.providers.firebase;

import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.test.TestUtils;
import com.maragues.menu_planner.test.factories.GroupFactory;
import com.maragues.menu_planner.test.factories.MealFactory;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import io.reactivex.observers.TestObserver;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by miguelaragues on 23/1/17.
 */
public class MealProviderFirebaseTest extends BaseProviderFirebaseTest<MealProviderFirebase> {

  @Override
  protected MealProviderFirebase createProvider() {
    return new MealProviderFirebase();
  }

  @Test
  public void create_assingsKeyIfItsEmpty() {
    String expectedKey = "my key";
    Meal recipe = MealFactory.base();
    doReturn(recipe.withId(expectedKey)).when(provider).assignKeyIfEmpty(eq(recipe));

    mockSingleResponse();

    TestObserver<Meal> observer = new TestObserver<>();

    provider.create(recipe).subscribe(observer);

    observer.assertComplete();
    assertEquals(expectedKey, observer.values().get(0).id());
  }

  @Test
  public void create_respectsExistingKey() {
    String expectedKey = "my key";
    Meal meal = MealFactory.base().withId(expectedKey);

    mockSingleResponse();

    TestObserver<Meal> observer = new TestObserver<>();

    provider.create(meal).subscribe(observer);

    observer.assertComplete();
    assertEquals(expectedKey, observer.values().get(0).id());
  }

  /*
  ASSIGN KEY IF EMPTY
   */

  @Test
  public void assignKeyIfEmpty_doesNotCreateNewKeyIfExists() {
    Meal mockMeal = mock(Meal.class);
    String existingKey = "not empty";
    doReturn(existingKey).when(mockMeal).id();

    Meal returnedMeal = provider.assignKeyIfEmpty(mockMeal);

    verify(provider, never()).createKey();

    assertEquals(existingKey, returnedMeal.id());
  }

  @Test
  public void assignKeyIfEmpty_generatesNewKeyIfExists() {
    Meal mockMeal = mock(Meal.class);
    doAnswer(new Answer<Meal>() {
      @Override
      public Meal answer(InvocationOnMock invocation) throws Throwable {
        return MealFactory.base().withId(invocation.getArgument(0));
      }
    }).when(mockMeal).withId(anyString());

    String generatedKey = "generated";
    doReturn(generatedKey)
            .when(provider)
            .createKey();

    provider.assignKeyIfEmpty(mockMeal);

    verify(provider).createKey();
    verify(mockMeal).withId(eq(generatedKey));
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

  @Test
  public void synchronizableToMap_addsGroupId() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    String key = "my key";
    Map<String, Object> map = provider.synchronizableToMap(MealFactory.base().withId(key));

    String expectedPath = "/" + MealProviderFirebase.MEALS_KEY + "/" + GroupFactory.DEFAULT_GROUP_ID + "/" + key;

    assertTrue(map.keySet().contains(expectedPath));
    assertEquals(GroupFactory.DEFAULT_GROUP_ID,
            invokeGetGroupId(map.get(expectedPath)));
  }

  private String invokeGetGroupId(Object object) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Class<?> clazz = TestUtils.getFirebaseValueClass(Meal.class);

    Method method = clazz.getDeclaredMethod("getGroupId");

    method.setAccessible(true);

    return String.valueOf(method.invoke(object));
  }
}