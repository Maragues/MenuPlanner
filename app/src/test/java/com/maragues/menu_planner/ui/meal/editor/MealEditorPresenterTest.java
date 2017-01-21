package com.maragues.menu_planner.ui.meal.editor;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.ui.test.BasePresenterTest;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by miguelaragues on 20/1/17.
 */
public class MealEditorPresenterTest extends BasePresenterTest<IMealEditor, MealEditorPresenter> {

  public MealEditorPresenterTest() {
    super(IMealEditor.class);
  }

  @NonNull
  @Override
  protected MealEditorPresenter createPresenter() {
    return new MealEditorPresenter();
  }

  @Test
  public void save_usesProvider() {
    presenter.onSaveClicked();

//    verify(App.appComponent.mealProvider()).create(any(Meal.class));
  }

  @Test
  public void loadMeal_emptyId(){
    doReturn(null).when(view).getMealId();

    initPresenter();

    verify(presenter).onNewMeal();
  }

  @Test
  public void clickAddRecipe_navigatesToSearchRecipes() {
    initPresenter();

    presenter.onAddRecipeClicked();

    verify(view).navigateToAddRecipe();
  }

  @Test
  public void onNewMeal_doesNotHideEmptyLayout() {
    initPresenter();

    presenter.onNewMeal();

    verify(view, never()).hideEmptyLayout();
  }

  @Test
  public void onNewMeal_createsLocalMeal() {
    initPresenter();

    presenter.onNewMeal();

    assertNotNull(presenter.meal);
  }

  @Test
  public void mealLoaded__doesNotHideEmptyLayout_ifEmpty() {
    initPresenter();

    Meal mockedMeal = mock(Meal.class);
    doReturn(Collections.emptyList()).when(mockedMeal).recipes();
    presenter.onMealLoaded(mockedMeal);

    verify(view, never()).hideEmptyLayout();
  }

  @Test
  public void mealLoaded__hidesEmptyLayout_ifNotEmpty() {
    initPresenter();

    Meal mockedMeal = mock(Meal.class);
    List<Recipe> recipes = new ArrayList<>();
    recipes.add(mock(Recipe.class));
    doReturn(recipes).when(mockedMeal).recipes();
    presenter.onMealLoaded(mockedMeal);

    verify(view).hideEmptyLayout();
  }
}