package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.Query;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.providers.IMealInstanceProvider;

/**
 * Created by miguelaragues on 17/1/17.
 */

public class MealInstanceProviderFirebase extends BaseListableFirebaseProvider<MealInstance>
        implements IMealInstanceProvider {

  public MealInstanceProviderFirebase() {
    super(MealInstance.class);
  }

  @Override
  public void create(@NonNull MealInstance mealInstance) {

  }

  @Override
  protected Query listQuery() {
    return null;
  }
}
