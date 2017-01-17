package com.maragues.menu_planner.model.providers;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.Meal;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by miguelaragues on 17/1/17.
 */

public interface IMealProvider {
  Observable<List<Meal>> list();

  void create(@NonNull Meal meal);
}
