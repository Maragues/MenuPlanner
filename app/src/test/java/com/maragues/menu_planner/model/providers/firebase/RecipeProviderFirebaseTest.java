package com.maragues.menu_planner.model.providers.firebase;

import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.test.factories.GroupFactory;
import com.maragues.menu_planner.test.factories.RecipeFactory;

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
public class RecipeProviderFirebaseTest extends BaseProviderFirebaseTest<RecipeProviderFirebase> {

  @Override
  protected RecipeProviderFirebase createProvider() {
    return new RecipeProviderFirebase();
  }

  @Test
  public void create_assingsKey() {
    String expectedKey = "my key";
    Recipe recipe = RecipeFactory.base();
    doReturn(recipe.withId(expectedKey)).when(provider).assignKey(eq(recipe));

    mockSingleResponse();

    TestObserver<Recipe> observer = new TestObserver<>();

    provider.create(recipe).subscribe(observer);

    observer.assertComplete();
    assertEquals(expectedKey, observer.values().get(0).id());
  }

  /*
  SYNCHRONIZABLE TO MAP
   */
  @Test(expected = IllegalArgumentException.class)
  public void synchronizableToMap_noId(){
    provider.synchronizableToMap(Recipe.empty(""));
  }

  @Test
  public void synchronizableToMap_updatesTwoPaths() {
    Map<String, Object> map = provider.synchronizableToMap(RecipeFactory.base());

    assertEquals(2, map.size());
  }

  @Test
  public void synchronizableToMap_createsRecipe() {
    String key = "my key";
    Map<String, Object> map = provider.synchronizableToMap(RecipeFactory.base().withId(key));

    String recipesPath = "/" + RecipeProviderFirebase.RECIPES_KEY + "/" + key;

    assertTrue(map.keySet().contains(recipesPath));
  }

  @Test
  public void synchronizableToMap_createsUserRecipe() {
    String recipeKey = "my key";
    Map<String, Object> map = provider.synchronizableToMap(
            RecipeFactory.base()
                    .withId(recipeKey)
    );

    String recipesPath = "/" + RecipeProviderFirebase.USER_RECIPES_KEY + "/" + GroupFactory.DEFAULT_GROUP_ID + "/" + recipeKey;

    assertTrue(map.keySet().contains(recipesPath));
  }

  /*
  SHORT DESCRIPTION
   */
  @Test
  public void summaryMap_containsFields() {
    String expectedName = "nameee";
    String expectedDescription = "My description is very interesting";

    Recipe recipe = Recipe.empty(expectedName).withDescription(expectedDescription);

    Map<String, Object> summaryMap = provider.toSummaryMap(recipe);

    assertEquals(expectedName, summaryMap.get(RecipeProviderFirebase.NAME_KEY));
    assertEquals(expectedDescription, summaryMap.get(RecipeProviderFirebase.SHORT_DESCRIPTION_KEY));
  }
}