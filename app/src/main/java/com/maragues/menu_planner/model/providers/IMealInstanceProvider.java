package com.maragues.menu_planner.model.providers;

import com.maragues.menu_planner.model.MealInstance;

import io.reactivex.Single;

/**
 * Created by miguelaragues on 17/1/17.
 */

public interface IMealInstanceProvider extends IListableProvider<MealInstance> {
  Single<MealInstance> create(MealInstance clickedMealInstance);
}
