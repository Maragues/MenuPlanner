package com.maragues.menu_planner.test.mock.providers;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.model.providers.IMealProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by miguelaragues on 4/1/17.
 */

public class MockMealProvider implements IMealProvider {
  private List<Meal> meals = new ArrayList<>();

  @Override
  public Observable<List<Meal>> list() {
    return Observable.just(meals);
  }

  @Override
  public void create(@NonNull Meal meal) {
    meals.add(meal);
  }
}
