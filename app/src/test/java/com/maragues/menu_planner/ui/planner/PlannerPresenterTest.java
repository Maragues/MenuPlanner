package com.maragues.menu_planner.ui.planner;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.ui.test.BasePresenterTest;

import org.junit.Test;
import org.threeten.bp.DayOfWeek;

import java.util.List;
import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
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