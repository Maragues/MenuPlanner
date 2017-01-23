package com.maragues.menu_planner.test.mock.providers;

import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.providers.IMealInstanceProvider;

import io.reactivex.Single;

/**
 * Created by miguelaragues on 4/1/17.
 */

public class MockMealInstanceProvider extends MockBaseListableProvider<MealInstance>
        implements IMealInstanceProvider {

  @Override
  public Single<MealInstance> create(MealInstance clickedMealInstance) {
    return super.createInternal(clickedMealInstance);
  }
}
