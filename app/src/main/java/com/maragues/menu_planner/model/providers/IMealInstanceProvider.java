package com.maragues.menu_planner.model.providers;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.MealInstance;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by miguelaragues on 17/1/17.
 */

public interface IMealInstanceProvider {
  Observable<List<MealInstance>> list();

  void create(@NonNull MealInstance mealInstance);
}
