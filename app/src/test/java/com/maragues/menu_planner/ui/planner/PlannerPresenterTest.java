package com.maragues.menu_planner.ui.planner;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.test.factories.MealInstanceFactory;
import com.maragues.menu_planner.ui.test.BasePresenterTest;

import org.junit.Test;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Flowable;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by miguelaragues on 17/1/17.
 */
public class PlannerPresenterTest extends BasePresenterTest<IPlanner, PlannerPresenter> {

  public PlannerPresenterTest() {
    super(IPlanner.class);
  }

  @NonNull
  @Override
  protected PlannerPresenter createPresenter() {
    return new PlannerPresenter();
  }

  /*
  NEW MEAL INSTANCES
   */

  @Test
  public void create_loadsMeals() {
    presenter = spy(createPresenter());

    onPresenterCreated();

    presenter.create();

    verify(presenter).loadMeals();
  }

  /*
  LOAD MEALS
   */
  @Test
  public void loadMeals_invokesProviderList() {
    initPresenter();

    verify(App.appComponent.mealInstanceProvider()).list();
  }

  /*
  MEALS SUBJECT
   */

  @Test
  public void mealsSubject_updatedOnNext() {
    Flowable<List<MealInstance>> observable = App.appComponent.mealInstanceProvider().list();
    TestSubscriber<List<MealInstance>> testObservable = observable.test();

//    doReturn(observable).when(App.appComponent.mealInstanceProvider()).list();

    initPresenter();

    TestObserver<List<MealInstance>> observer = presenter.mealsObservable().test();

    assertEquals(7, observer.values().get(0).size());

    List<MealInstance> mealInstances = new ArrayList<>(observer.values().get(0));
    mealInstances.add(MealInstanceFactory.base(LocalDateTime.now()));

    testObservable.onNext(mealInstances);

    assertEquals(8, observer.values().get(0).size());
  }

  /*
  DEFAULT MEAL_INSTANCES
   */

  @Test
  public void mealInstanceObservable_default_7() {
    initPresenter();

    List<MealInstance> instances = getMealInstancesThroughObservable();

    assertEquals(7, instances.size());
  }

  @Test
  public void defaultMealInstances_france_MondayFirstDay() {
    initPresenter();

    Locale.setDefault(Locale.FRANCE);

    List<MealInstance> instances = presenter.createDefaultMeals();

    assertEquals(DayOfWeek.MONDAY.getValue(), instances.get(0).dateTime().getDayOfWeek().getValue());
  }

  @Test
  public void defaultMealInstances_france_SevenDaysOfWeek() {
    initPresenter();

    Locale.setDefault(Locale.FRANCE);

    List<MealInstance> instances = presenter.createDefaultMeals();

    int firstDay = DayOfWeek.MONDAY.getValue();
    for (MealInstance instance : instances) {
      assertEquals(firstDay++, instance.dateTime().getDayOfWeek().getValue());
    }
  }

  @Test
  public void defaultMealInstances_SundayFirstDayInUS() {
    initPresenter();

    Locale.setDefault(Locale.US);

    List<MealInstance> instances = presenter.createDefaultMeals();

    assertEquals(DayOfWeek.SUNDAY.getValue(), instances.get(0).dateTime().getDayOfWeek().getValue());
  }

  @Test
  public void defaultMealInstances_US_SevenDaysOfWeek() {
    initPresenter();

    Locale.setDefault(Locale.US);

    List<MealInstance> instances = presenter.createDefaultMeals();

    assertEquals(DayOfWeek.SUNDAY.getValue(), instances.get(0).dateTime().getDayOfWeek().getValue());

    int secondDay = DayOfWeek.MONDAY.getValue();
    for (int i = 1; i < instances.size(); i++) {
      assertEquals(secondDay++, instances.get(i).dateTime().getDayOfWeek().getValue());
    }
  }

  @Test
  public void clickOnDay_asksForLabel() {
    initPresenter();

    presenter.onAddtoDayClicked(mock(MealInstance.class));

    verify(view).askForMealInstanceLabel();
  }

  private List<MealInstance> getMealInstancesThroughObservable() {
    return presenter.mealsObservable().test().assertValueCount(1).values().get(0);
  }
}