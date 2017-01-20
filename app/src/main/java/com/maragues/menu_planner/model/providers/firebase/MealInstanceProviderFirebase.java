package com.maragues.menu_planner.model.providers.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.providers.IMealInstanceProvider;

import java.util.Map;

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
  protected Map<String, Object> synchronizableToMap(MealInstance item) {
    return null;
  }

  @Override
  public void create(MealInstance clickedMealInstance) {

  }
}
