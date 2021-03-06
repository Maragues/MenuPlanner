package com.maragues.menu_planner.model.providers;

import com.maragues.menu_planner.model.Meal;

import io.reactivex.Single;

/**
 * Created by miguelaragues on 17/1/17.
 */

public interface IMealProvider extends IListableProvider<Meal>{
  Single<Meal> create(Meal meal);

  Single<String> getKey();
}
