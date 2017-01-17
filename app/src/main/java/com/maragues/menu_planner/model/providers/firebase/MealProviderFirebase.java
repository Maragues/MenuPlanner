package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.model.providers.IMealProvider;

/**
 * Created by miguelaragues on 17/1/17.
 */

public class MealProviderFirebase extends BaseProviderFirebase<Meal> implements IMealProvider {
  @Override
  public void create(@NonNull Meal meal) {

  }
}
