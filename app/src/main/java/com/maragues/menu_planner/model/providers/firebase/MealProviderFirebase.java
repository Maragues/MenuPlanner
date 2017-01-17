package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.model.providers.IMealProvider;

import io.reactivex.Flowable;

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
  protected Meal snapshotToInstance(DataSnapshot dataSnapshot) {
    return Meal.create(dataSnapshot);
  }

  @Override
  public Flowable<Meal> create(@NonNull Meal item) {
    return null;
  }
}
