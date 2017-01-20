package com.maragues.menu_planner.ui.meal.editor;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.test.mock.providers.MockRecipeProvider;
import com.maragues.menu_planner.ui.test.BasePresenterTest;

import org.junit.Test;

import static org.mockito.Mockito.mock;
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

  @Test
  public void emptyRecipes_opensNewRecipeScreen() {
    presenter.loadRecipes();

    verify(view).navigateToCreateRecipe();
  }

  @Test
  public void atLeastOneRecipe_showsRecipeList() {
    recipeProvider.create(mock(Recipe.class));

    presenter.loadRecipes();

    verify(view).showRecipeList();
  }
}