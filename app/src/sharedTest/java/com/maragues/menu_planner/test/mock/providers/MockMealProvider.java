package com.maragues.menu_planner.test.mock.providers;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.model.providers.IMealProvider;
import com.maragues.menu_planner.test.factories.MealFactory;

import io.reactivex.Single;

/**
 * Created by miguelaragues on 4/1/17.
 */

public class MockMealProvider extends MockBaseListableProvider<Meal> implements IMealProvider {
  @Override
  public Single<Meal> create(@NonNull Meal item) {
    return super.createInternal(item);
  }

  @Override
  public Single<String> generateKey() {
    return Single.just(MealFactory.MEAL_ID);
  }
}
