package com.maragues.menu_planner.ui.planner;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.ui.test.BasePresenterTest;

import org.junit.Test;

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
  public void clickOnDay_asksForLabel() {
    presenter.onAddtoDayClicked(mock(MealInstance.class));

    verify(view).askForMealInstanceLabel();
  }
}