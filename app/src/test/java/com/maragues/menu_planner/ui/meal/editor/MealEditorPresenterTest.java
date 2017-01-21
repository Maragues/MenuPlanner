package com.maragues.menu_planner.ui.meal.editor;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.test.mock.providers.MockMealProvider;
import com.maragues.menu_planner.ui.test.BasePresenterTest;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by miguelaragues on 20/1/17.
 */
public class MealEditorPresenterTest extends BasePresenterTest<IMealEditor, MealEditorPresenter> {

  MealInstance mealInstance;

  MockMealProvider mockMealProvider;

  public MealEditorPresenterTest() {
    super(IMealEditor.class);

    mockMealProvider = (MockMealProvider) App.appComponent.mealProvider();
  }

  @Override
  public void setUp() {
    super.setUp();

    mealInstance = null;
  }

  @NonNull
  @Override
  protected MealEditorPresenter createPresenter() {
    return new MealEditorPresenter(mealInstance);
  }

  /*
  SAVE
   */
  @Test
  public void save_emptyMealInstance_usesMealProvider() {
    initPresenter();

    presenter.onSaveClicked();

    verify(App.appComponent.mealProvider()).create(any(Meal.class));
  }

  @Test
  public void save_withMealInstance_usesMealProvider() {
    mealInstance = MealInstance.fromLocalDateTime(LocalDateTime.MAX);

    initPresenter();

    presenter.onSaveClicked();

    verify(App.appComponent.mealProvider()).create(any(Meal.class));
  }

  @Test
  public void save_withMealInstance_usesMealInstanceProvider() {
    mealInstance = MealInstance.fromLocalDateTime(LocalDateTime.MAX);

    doReturn(Single.just(Meal.emptyMeal().withId("mealId")))
            .when(mockMealProvider)
            .create(any(Meal.class));

    initPresenter();

    presenter.onSaveClicked();

    verify(App.appComponent.mealInstanceProvider()).create(any(MealInstance.class));
  }

  @Test
  public void save_withMealInstance_savesMealInstanceWithCorrectDate() {
    final LocalDateTime expectedTime = LocalDateTime.MAX;

    mealInstance = MealInstance.fromLocalDateTime(expectedTime);

    doReturn(Single.just(Meal.emptyMeal().withId("mealId")))
            .when(mockMealProvider)
            .create(any(Meal.class));

    initPresenter();

    presenter.onSaveClicked();

    ArgumentCaptor<MealInstance> mealInstanceArgumentCaptor = ArgumentCaptor.forClass(MealInstance.class);

    verify(App.appComponent.mealInstanceProvider()).create(mealInstanceArgumentCaptor.capture());

    assertEquals(expectedTime, mealInstanceArgumentCaptor.getValue().dateTime());
  }

  @Test
  public void save_withMealInstance_savesMealInstanceWithCorrectMealId() {
    mealInstance = MealInstance.fromLocalDateTime(LocalDateTime.MAX);

    String expectedMealId = "myMealId";

    doReturn(Single.just(Meal.emptyMeal().withId(expectedMealId)))
            .when(mockMealProvider)
            .create(any(Meal.class));

    initPresenter();

    presenter.onSaveClicked();

    ArgumentCaptor<MealInstance> mealInstanceArgumentCaptor = ArgumentCaptor.forClass(MealInstance.class);

    verify(App.appComponent.mealInstanceProvider()).create(mealInstanceArgumentCaptor.capture());

    assertEquals(expectedMealId, mealInstanceArgumentCaptor.getValue().mealId());
  }

  /*
  LOAD MEAL ID
   */

  @Test
  public void loadMeal_emptyId() {
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