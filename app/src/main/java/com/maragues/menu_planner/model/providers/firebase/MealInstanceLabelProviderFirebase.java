package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.maragues.menu_planner.model.MealInstanceLabel;
import com.maragues.menu_planner.model.providers.IMealInstanceLabelProvider;

import io.reactivex.Flowable;

/**
 * Created by miguelaragues on 17/1/17.
 */

public class MealInstanceLabelProviderFirebase extends BaseListableFirebaseProvider<MealInstanceLabel>
        implements IMealInstanceLabelProvider {

  public MealInstanceLabelProviderFirebase() {
    super(MealInstanceLabel.class);
  }

  @Override
  protected Query listQuery() {
    return null;
  }

  @Override
  protected MealInstanceLabel snapshotToInstance(DataSnapshot dataSnapshot) {
    return MealInstanceLabel.create(dataSnapshot);
  }

  @Override
  public Flowable<MealInstanceLabel> create(@NonNull MealInstanceLabel item) {
    return null;
  }
}
