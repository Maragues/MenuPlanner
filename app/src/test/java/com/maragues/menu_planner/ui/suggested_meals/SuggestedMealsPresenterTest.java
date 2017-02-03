package com.maragues.menu_planner.ui.suggested_meals;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.MealInstanceLabel;
import com.maragues.menu_planner.test.factories.MealFactory;
import com.maragues.menu_planner.test.factories.MealInstanceFactory;
import com.maragues.menu_planner.test.mock.providers.MockMealProvider;
import com.maragues.menu_planner.ui.test.BasePresenterTest;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Single;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by miguelaragues on 25/1/17.
 */
public class SuggestedMealsPresenterTest extends BasePresenterTest<ISuggestedMeals, SuggestedMealsPresenter> {

  MealInstance mealInstance;

  MockMealProvider mockMealProvider;

  public SuggestedMealsPresenterTest() {
    super(ISuggestedMeals.class);

    mockMealProvider = (MockMealProvider) App.appComponent.mealProvider();
  }

  @Override
  public void setUp() {
    super.setUp();

    mealInstance = null;
  }

  @NonNull
  @Override
  protected SuggestedMealsPresenter createPresenter() {
    return new SuggestedMealsPresenter(mealInstance);
  }

  /*
  CREATE MEAL
   */
  @Test
  public void createMeal_invokesNavigateToCreateWithKey() {
    prepareScenarioWithMeals();

    initPresenter();

    String expectedKey = "my expe key";

    doReturn(Single.just(expectedKey))
            .when(App.appComponent.mealProvider())
            .generateKey();

    presenter.onCreateMealClicked();

    verify(view).navigateToCreateMeal(eq(expectedKey));
  }

  @Test
  public void createMeal_storesKey() {
    prepareScenarioWithMeals();

    initPresenter();

    String expectedKey = "my expe key";

    doReturn(Single.just(expectedKey))
            .when(App.appComponent.mealProvider())
            .generateKey();

    presenter.onCreateMealClicked();

    assertEquals(expectedKey, presenter.expectedKey);
  }

  @Test
  public void createMeal_doesNotFinish() {
    prepareScenarioWithMeals();

    initPresenter();

    String expectedKey = "my expe key";

    doReturn(Single.just(expectedKey))
            .when(App.appComponent.mealProvider())
            .generateKey();

    presenter.onCreateMealClicked();

    verify(view, never()).finish();
  }

  /*
  MEAL CLICKED
   */

  @Test
  public void click_createsMealInstanceWithCorrectFields() {
    prepareScenarioWithMeals();

    MealInstanceLabel expectedLabel = MealInstanceLabel.DINNER;
    LocalDateTime expectedTime = expectedLabel.time().atDate(LocalDate.now().minusDays(3));
    mealInstance = MealInstanceFactory.base(expectedTime).withLabel(expectedLabel);

    initPresenter();

    String expectedMealKey = "meal keeke";
    Meal meal = MealFactory.base().withId(expectedMealKey);
    presenter.onMealClicked(meal);

    ArgumentCaptor<MealInstance> captor = ArgumentCaptor.forClass(MealInstance.class);

    verify(App.appComponent.mealInstanceProvider()).create(captor.capture());

    MealInstance createdMealInstance = captor.getValue();

    assertNotNull(createdMealInstance);
    assertEquals(expectedTime, createdMealInstance.dateTime());
    assertEquals(expectedMealKey, createdMealInstance.mealId());
    assertEquals(expectedLabel.id(), createdMealInstance.labelId());
  }

  @Test
  public void click_invokeOnMealInstanceCreated() {
    prepareScenarioWithMeals();

    LocalDateTime expectedTime = LocalDateTime.now().minusDays(3);
    mealInstance = MealInstanceFactory.base(expectedTime);

    initPresenter();

    Meal meal = MealFactory.base();
    presenter.onMealClicked(meal);

    verify(presenter).onMealInstanceCreated(any(MealInstance.class));
  }

  @Test
  public void onMealInstanceCreated_invokesFinish() {
    prepareScenarioWithMeals();

    initPresenter();

    presenter.onMealInstanceCreated(MealInstanceFactory.base(LocalDateTime.now()));

    verify(view, never()).navigateToCreateMeal(anyString());

    verify(view).finish();
  }

  /*
  RETURN FROM CREATE MEAL
   */

  @Test
  public void returnCreateMeal_mealCreationSuccess() {
    prepareScenarioWithMeals();

    initPresenter();

    presenter.onCreateMealSuccess(MealFactory.withRecipes());

    verify(view).finish();
  }

  @Test
  public void returnCreateMeal_mealCreationFailed_doesNotFinish() {
    prepareScenarioWithMeals();

    initPresenter();

    presenter.onCreateMealCanceled();

    verify(view, never()).finish();
  }

  /*
  MEAL CREATED
   */

  @Test
  public void mealCreated_withExpectedId_doesNotShowSuggestedMeals() {
    prepareScenarioWithMeals();

    final FlowableEmitter[] emitter = new FlowableEmitter[1];

    Flowable<List<Meal>> mealFlowable = Flowable.create(new FlowableOnSubscribe<List<Meal>>() {
      @Override
      public void subscribe(FlowableEmitter<List<Meal>> e) throws Exception {
        emitter[0] = e;
      }
    }, BackpressureStrategy.BUFFER);

    doReturn(mealFlowable).when(App.appComponent.mealProvider()).list();

    initPresenter();

    String expectedKey = "my expected";
    presenter.expectedKey = expectedKey;

    List<Meal> meals = new ArrayList<>();

    meals.add(MealFactory.withRecipes().withId(expectedKey));

    emitter[0].onNext(meals);

    verify(presenter, never()).showSuggestedMeals(anyList());
  }

  @Test
  public void mealCreated_withExpectedId_invokesFinish() {
    prepareScenarioWithMeals();

    final FlowableEmitter[] emitter = new FlowableEmitter[1];

    Flowable<List<Meal>> mealFlowable = Flowable.create(new FlowableOnSubscribe<List<Meal>>() {
      @Override
      public void subscribe(FlowableEmitter<List<Meal>> e) throws Exception {
        emitter[0] = e;
      }
    }, BackpressureStrategy.BUFFER);

    doReturn(mealFlowable).when(App.appComponent.mealProvider()).list();

    initPresenter();

    String expectedKey = "my expected";
    presenter.expectedKey = expectedKey;

    List<Meal> meals = new ArrayList<>();

    meals.add(MealFactory.withRecipes().withId(expectedKey));

    emitter[0].onNext(meals);

    verify(view).finish();
  }

  @Test
  public void mealCreated_notExpectedId_doesNotInvokeFinish() {
    prepareScenarioWithMeals();

    final FlowableEmitter[] emitter = new FlowableEmitter[1];

    Flowable<List<Meal>> mealFlowable = Flowable.create(new FlowableOnSubscribe<List<Meal>>() {
      @Override
      public void subscribe(FlowableEmitter<List<Meal>> e) throws Exception {
        emitter[0] = e;
      }
    }, BackpressureStrategy.BUFFER);

    doReturn(mealFlowable).when(App.appComponent.mealProvider()).list();

    initPresenter();

    String expectedKey = "my expected";
    presenter.expectedKey = expectedKey;

    List<Meal> meals = new ArrayList<>();

    meals.add(MealFactory.withRecipes().withId("an unexpected key"));

    emitter[0].onNext(meals);

    verify(view, never()).finish();
  }

  @Test
  public void mealCreated_notExpectedId_showsMeals() {
    prepareScenarioWithMeals();

    final FlowableEmitter[] emitter = new FlowableEmitter[1];

    Flowable<List<Meal>> mealFlowable = Flowable.create(new FlowableOnSubscribe<List<Meal>>() {
      @Override
      public void subscribe(FlowableEmitter<List<Meal>> e) throws Exception {
        emitter[0] = e;
      }
    }, BackpressureStrategy.BUFFER);

    doReturn(mealFlowable).when(App.appComponent.mealProvider()).list();

    initPresenter();

    String expectedKey = "my expected";
    presenter.expectedKey = expectedKey;

    List<Meal> meals = new ArrayList<>();

    meals.add(MealFactory.withRecipes().withId("an unexpected key"));

    emitter[0].onNext(meals);

    verify(view).showSuggestedMeals(anyList());
  }

  @Test
  public void save_withExpectedMealId_savesMealInstanceWithCorrectDate() {
    final LocalDateTime expectedTime = LocalDateTime.now();

    mealInstance = MealInstance.fromLocalDateTime(expectedTime);

    initPresenter();

    presenter.onCreateMealSuccess(MealFactory.withRecipes());

    ArgumentCaptor<MealInstance> mealInstanceArgumentCaptor = ArgumentCaptor.forClass(MealInstance.class);

    verify(App.appComponent.mealInstanceProvider()).create(mealInstanceArgumentCaptor.capture());

    assertEquals(expectedTime, mealInstanceArgumentCaptor.getValue().dateTime());
  }

  @Test
  public void save_withExpectedMealId_savesMealInstanceWithCorrectMealId() {
    final LocalDateTime expectedTime = LocalDateTime.now();

    mealInstance = MealInstance.fromLocalDateTime(expectedTime);

    String expectedMealId = "myMealId";

    initPresenter();

    presenter.onCreateMealSuccess(MealFactory.withRecipes().withId(expectedMealId));

    ArgumentCaptor<MealInstance> mealInstanceArgumentCaptor = ArgumentCaptor.forClass(MealInstance.class);

    verify(App.appComponent.mealInstanceProvider()).create(mealInstanceArgumentCaptor.capture());

    assertEquals(expectedMealId, mealInstanceArgumentCaptor.getValue().mealId());
  }

  @Test
  public void returnCreateMeal_mealCreationFailed_clearsExpectedMeal() {
    prepareScenarioWithMeals();

    initPresenter();

    presenter.expectedKey = "blabla";

    presenter.onCreateMealCanceled();

    assertNull(presenter.expectedKey);
  }

  /*
  CREATE MEAL
   */

  @Test
  public void start_zeroMeals_takesToCreateMeal() {
    initPresenter();

    verify(view).navigateToCreateMeal(anyString());
  }

  @Test
  public void start_zeroMeals_DoesNotFinish() {
    initPresenter();

    verify(view, never()).finish();
  }

  void prepareScenarioWithMeals() {
    mockMealProvider.create(MealFactory.base());
  }
}