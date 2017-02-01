package com.maragues.menu_planner.ui.meal_instance;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.test.factories.MealInstanceFactory;
import com.maragues.menu_planner.ui.test.BasePresenterTest;

import org.junit.Test;

import io.reactivex.Single;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * Created by miguelaragues on 1/2/17.
 */
public class MealInstanceViewerPresenterTest extends BasePresenterTest<IMealInstanceViewer, MealInstanceViewerPresenter> {

  String mealId = MealInstanceFactory.MEAL_INSTANCE_ID;

  boolean preventFetch = false;

  public MealInstanceViewerPresenterTest() {
    super(IMealInstanceViewer.class);
  }

  @NonNull
  @Override
  protected MealInstanceViewerPresenter createPresenter() {
    return new MealInstanceViewerPresenter(mealId);
  }

  @Override
  public void setUp() {
    super.setUp();

    preventFetch = false;
  }

  @Test
  public void nullMealId_invokesShowError() {
    mealId = null;

    initPresenter();

    verify(presenter).onFetchError(any());
  }

  @Test
  public void errorFetchMeal_invokesShowError() {
    doReturn(Single.error(new Resources.NotFoundException()))
            .when(App.appComponent.mealInstanceProvider())
            .get(eq(mealId));

    initPresenter();

    verify(presenter).onFetchError(any(Resources.NotFoundException.class));
  }

  @Test
  public void onFetchError_invokesShowError() {
    preventFetch = true;

    initPresenter();

    presenter.onFetchError(new Resources.NotFoundException());

    verify(view).showErrorFetchingMealInstance();
  }

  @Test
  public void fetchMeal_invokesOnMealFetched() {
    MealInstance expectedMealInstance = MealInstanceFactory.withRecipes();
    doReturn(Single.just(expectedMealInstance))
            .when(App.appComponent.mealInstanceProvider())
            .get(eq(mealId));

    initPresenter();

    verify(presenter).onMealInstanceFetched(eq(expectedMealInstance));
  }

  @Test
  public void onMealInstanceFetched_storesMealInstance() {
    preventFetch = true;

    initPresenter();

    assertNull(presenter.mealInstance);

    MealInstance mealInstance = MealInstanceFactory.base();
    presenter.onMealInstanceFetched(mealInstance);

    assertNotNull(presenter.mealInstance);
  }

  @Test
  public void onMealInstanceFetched_showsRecipes() {
    preventFetch = true;

    initPresenter();

    MealInstance mealInstance = MealInstanceFactory.base();
    presenter.onMealInstanceFetched(mealInstance);

    verify(view).showRecipes(anyString());
  }

  @Override
  protected void onPresenterCreated() {
    if (preventFetch)
      doNothing().when(presenter).fetchMealInstance();

    super.onPresenterCreated();
  }
}