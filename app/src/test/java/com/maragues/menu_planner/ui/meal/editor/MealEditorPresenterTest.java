package com.maragues.menu_planner.ui.meal.editor;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.RecipeMeal;
import com.maragues.menu_planner.test.factories.MealFactory;
import com.maragues.menu_planner.test.factories.MealInstanceFactory;
import com.maragues.menu_planner.test.factories.RecipeFactory;
import com.maragues.menu_planner.test.mock.providers.MockMealProvider;
import com.maragues.menu_planner.ui.test.BasePresenterTest;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Maybe;
import io.reactivex.Single;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by miguelaragues on 20/1/17.
 */
public class MealEditorPresenterTest extends BasePresenterTest<IMealEditor, MealEditorPresenter> {

  String expectedMealId;

  MockMealProvider mockMealProvider;

  public MealEditorPresenterTest() {
    super(IMealEditor.class);

    mockMealProvider = (MockMealProvider) App.appComponent.mealProvider();
  }

  @Override
  public void setUp() {
    super.setUp();

    expectedMealId = null;
  }

  @NonNull
  @Override
  protected MealEditorPresenter createPresenter() {
    return new MealEditorPresenter(expectedMealId);
  }

  /*
  LOAD MEAL
   */

  @Test
  public void loadMeal_emptyId() {
    expectedMealId = null;

    initPresenter();

    verify(presenter).loadMeal(isNull());
  }

  @Test
  public void loadMeal_attemptsToGetExpectedId() {
    expectedMealId = "My iddd";

    initPresenter();

    //I'm going to try fetch a non existing id, let's see what Database returns
    verify(App.appComponent.mealProvider()).get(eq(expectedMealId));
  }

  @Test
  public void loadMeal_success_invokesOnMealLoaded() {
    expectedMealId = "My iddd";

    Maybe<Meal> maybeMeal = Maybe.create(e -> e.onSuccess(MealFactory.base()));
    doReturn(maybeMeal).when(App.appComponent.mealProvider()).get(eq(expectedMealId));

    initPresenter();

    verify(presenter).onMealLoaded(eq(MealFactory.base()));
  }

  @Test
  public void loadMeal_success_rendersMeal() {
    expectedMealId = "My iddd";

    Maybe<Meal> maybeMeal = Maybe.create(e -> e.onSuccess(MealFactory.base()));
    doReturn(maybeMeal).when(App.appComponent.mealProvider()).get(eq(expectedMealId));

    initPresenter();

    verify(presenter).renderMeal();
  }

  @Test
  public void loadMeal_NoSuccess_completes_invokesOnNewMeal() {
    expectedMealId = "My iddd";

    Maybe<Meal> maybeMeal = Maybe.create(e -> e.onComplete());
    doReturn(maybeMeal).when(App.appComponent.mealProvider()).get(eq(expectedMealId));

    initPresenter();

    verify(presenter).onNewMeal(eq(expectedMealId));
  }

  @Test
  public void loadMeal_error_invokesError() {
    expectedMealId = "My iddd";

    Maybe<Meal> maybeMeal = Maybe.create(e -> e.onError(new Exception()));
    doReturn(maybeMeal).when(App.appComponent.mealProvider()).get(eq(expectedMealId));

    initPresenter();

    verify(presenter).onErrorLoadingMeal(any(Throwable.class));
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
  public void save_withExpectedMealId_usesMealProvider() {
    expectedMealId = MealInstanceFactory.MEAL_INSTANCE_ID;

    initPresenter();

    presenter.onSaveClicked();

    verify(App.appComponent.mealProvider()).create(any(Meal.class));
  }

  @Test
  public void save_withExpectedMealId_savesMealWithExpectedKey() {
    expectedMealId = MealInstanceFactory.MEAL_INSTANCE_ID;

    initPresenter();

    presenter.onSaveClicked();

    ArgumentCaptor<Meal> mealArgumentCaptor = ArgumentCaptor.forClass(Meal.class);

    verify(App.appComponent.mealProvider()).create(mealArgumentCaptor.capture());

    assertEquals(MealInstanceFactory.MEAL_INSTANCE_ID, mealArgumentCaptor.getValue().id());
  }

  @Test
  public void save_withExpectedMealId_doesNotTouchMealInstanceProvider() { //test no legacy code executed
    expectedMealId = MealInstanceFactory.MEAL_INSTANCE_ID;

    doReturn(Single.just(Meal.empty().withId("mealId")))
            .when(mockMealProvider)
            .create(any(Meal.class));

    initPresenter();

    presenter.onSaveClicked();

    verify(App.appComponent.mealInstanceProvider(), never()).create(any(MealInstance.class));
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

    presenter.onNewMeal("bla");

    verify(view, never()).hideEmptyLayout();
  }

  @Test
  public void onNewMeal_createsLocalMealWithExpectedId() {
    expectedMealId = "my expectedId";
    initPresenter();

    presenter.onNewMeal(expectedMealId);

    assertNotNull(presenter.meal);
    assertEquals(expectedMealId, presenter.meal.id());
  }

  @Test
  public void mealLoaded_storesMeal() {
    initPresenter();

    Meal meal = MealFactory.withRecipes();

    presenter.onMealLoaded(meal);

    assertEquals(meal, presenter.meal);
  }

  @Test
  public void renderMeal_doesNotHideEmptyLayout_ifEmpty() {
    initPresenter();

    Meal meal = MealFactory.base();

    presenter.onMealLoaded(meal);

    presenter.renderMeal();

    verify(view, never()).hideEmptyLayout();
  }

  @Test
  public void renderMeal_hidesEmptyLayout_ifNotEmpty() {
    initPresenter();

    presenter.onMealLoaded(MealFactory.withRecipes());

    presenter.renderMeal();

    verify(view).hideEmptyLayout();
  }

  @Test
  public void renderMeal_loadsRecipes() {
    initPresenter();

    Map<String, RecipeMeal> recipes = new HashMap<>();
    recipes.put(RecipeFactory.RECIPE_ID, mock(RecipeMeal.class));
    Meal meal = MealFactory.base().withRecipes(recipes);

    presenter.onMealLoaded(meal);

    presenter.renderMeal();

    verify(view).showRecipes(eq(meal.recipeCollection()));
  }
}