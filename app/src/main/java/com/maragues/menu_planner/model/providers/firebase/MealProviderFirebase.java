package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.Query;
import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.model.providers.IMealProvider;

import io.reactivex.Observable;

/**
 * Created by miguelaragues on 17/1/17.
 */

public class MealProviderFirebase extends BaseListableFirebaseProvider<Meal> implements IMealProvider {

  public MealProviderFirebase() {
    super(Meal.class);
  }

  @Override
  protected Query listQuery() {
    return null;
  }

  @Override
  public void create(@NonNull Meal meal) {

  }
}
