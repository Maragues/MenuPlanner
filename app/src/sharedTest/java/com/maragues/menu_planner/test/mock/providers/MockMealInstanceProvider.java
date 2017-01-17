package com.maragues.menu_planner.test.mock.providers;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.providers.IMealInstanceProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by miguelaragues on 4/1/17.
 */

public class MockMealInstanceProvider implements IMealInstanceProvider {
  private List<MealInstance> mealInstances = new ArrayList<>();

  @Override
  public Observable<List<MealInstance>> list() {
    return Observable.just(mealInstances);
  }

  @Override
  public void create(@NonNull MealInstance mealInstance) {
    mealInstances.add(mealInstance);
  }
}
