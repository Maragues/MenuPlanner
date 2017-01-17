package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.providers.IMealInstanceProvider;

import io.reactivex.Flowable;

/**
 * Created by miguelaragues on 17/1/17.
 */

public class MealInstanceProviderFirebase extends BaseListableFirebaseProvider<MealInstance>
        implements IMealInstanceProvider {

  public MealInstanceProviderFirebase() {
    super(MealInstance.class);
  }

  @Override
  protected Query listQuery() {
    return null;
  }

  @Override
  protected MealInstance snapshotToInstance(DataSnapshot dataSnapshot) {
    return MealInstance.create(dataSnapshot);
  }

  @Override
  public Flowable<MealInstance> create(@NonNull MealInstance item) {
    return null;
  }
}
