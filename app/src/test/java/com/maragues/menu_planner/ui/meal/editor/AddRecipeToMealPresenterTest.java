package com.maragues.menu_planner.ui.meal.editor;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.test.factories.RecipeFactory;
import com.maragues.menu_planner.test.mock.providers.MockRecipeProvider;
import com.maragues.menu_planner.ui.test.BasePresenterTest;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by miguelaragues on 20/1/17.
 */
public class AddRecipeToMealPresenterTest extends BasePresenterTest<IAddRecipeToMeal, AddRecipeToMealPresenter> {

  MockRecipeProvider recipeProvider;

  public AddRecipeToMealPresenterTest() {
    super(IAddRecipeToMeal.class);

    recipeProvider = (MockRecipeProvider) App.appComponent.recipeProvider();
  }

  @Override
  public void setUp() {
    super.setUp();

    recipeProvider.reset();
  }

  @NonNull
  @Override
  protected AddRecipeToMealPresenter createPresenter() {
    return new AddRecipeToMealPresenter();
  }

  /*
  RECIPE CLICKED
   */
  @Test
  public void click_recipeIsStoredAsResult() {
    initPresenter();

    Recipe recipe = RecipeFactory.base();
    presenter.onRecipeClicked(recipe);

    verify(view).storeResult(eq(recipe));
  }

  @Test
  public void click_invokeFinish() {
    initPresenter();

    Recipe recipe = RecipeFactory.base();
    presenter.onRecipeClicked(recipe);

    verify(view).finish();
  }

  /*
  CREATE RECIPE
   */
  @Test
  public void createRecipe_invokesNavigateToCreateWithKey() {
    initPresenter();

    String expectedKey = "my expe key";

    doReturn(Single.just(expectedKey))
            .when(App.appComponent.recipeProvider())
            .getKey();

    presenter.onCreateRecipeClicked();

    verify(view).navigateToCreateRecipe(eq(expectedKey));
  }

  @Test
  public void createRecipe_storesKey() {
    initPresenter();

    String expectedKey = "my expe key";

    doReturn(Single.just(expectedKey))
            .when(App.appComponent.recipeProvider())
            .getKey();

    presenter.onCreateRecipeClicked();

    assertEquals(expectedKey, presenter.expectedKey);
  }

  @Captor
  ArgumentCaptor<List<Recipe>> recipeCaptor;

  /*
  RECIPES OBSERVER
   */
  @Test
  public void recipesLoaded_newRecipesFromServer_showsrecipes() {
    initPresenter();

    assertNull(presenter.expectedKey);

    List<Recipe> recipeList = new ArrayList<>();
    recipeList.add(RecipeFactory.base());

    presenter.onRecipesLoaded(recipeList);

    verify(presenter).onRecipesLoaded(eq(recipeList));

    verify(view).showRecipeList(anyList());

    verify(presenter, never()).onRecipeAdded(any(Recipe.class));
  }

  @Test
  public void recipesLoaded_newRecipeByUser_invokesOnRecipeAdded() {
    initPresenter();

    String generatedKey = "generated_key";
    presenter.expectedKey = generatedKey;

    List<Recipe> recipeList = new ArrayList<>();
    Recipe newRecipe = RecipeFactory.base().withId(generatedKey);
    recipeList.add(newRecipe);

    presenter.onRecipesLoaded(recipeList);

    verify(view, never()).showRecipeList(anyList());

    verify(presenter).onRecipeAdded(eq(newRecipe));
  }

  /*
  ON RECIPE ADDED
   */

  @Test
  public void onRecipeAdded_storesResult() {
    initPresenter();

    Recipe recipe = RecipeFactory.base();

    presenter.onRecipeAdded(recipe);

    verify(view).storeResult(eq(recipe));
  }

  @Test
  public void onRecipeAdded_finish() {
    initPresenter();

    Recipe recipe = RecipeFactory.base();

    presenter.onRecipeAdded(recipe);

    verify(view).finish();
  }

  /*
  INITIAL SCENARIO
   */

  @Test
  public void emptyRecipes_opensNewRecipeScreen() {
    initPresenter();

    presenter.loadRecipes();

    verify(view).navigateToCreateRecipe(eq(RecipeFactory.RECIPE_ID));
  }

  @Test
  public void atLeastOneRecipe_showsRecipeList() {
    initPresenter();

    recipeProvider.create(mock(Recipe.class));

    presenter.loadRecipes();

    verify(view).showRecipeList(anyList());
  }
}