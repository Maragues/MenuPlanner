package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.maragues.menu_planner.model.MealInstanceLabel;
import com.maragues.menu_planner.model.providers.IMealInstanceLabelProvider;

import java.util.Map;

/**
 * Created by miguelaragues on 17/1/17.
 */

public class MealInstanceLabelProviderFirebase extends BaseListableFirebaseProvider<MealInstanceLabel>
        implements IMealInstanceLabelProvider {


  public MealInstanceLabelProviderFirebase() {
    super(MealInstanceLabel.class);
  }

  @NonNull
  @Override
  protected Query createListQuery() {
    return getReference()
            .child(MEAL_LABELS_KEY);
  }

  @NonNull
  @Override
  protected Query createGetQuery(String id) {
    return getReference()
            .child(MEAL_LABELS_KEY)
            .child(id);
  }

  @NonNull
  @Override
  protected MealInstanceLabel snapshotToInstance(DataSnapshot dataSnapshot) {
    return MealInstanceLabel.create(dataSnapshot);
  }

  @NonNull
  @Override
  protected Map<String, Object> synchronizableToMap(MealInstanceLabel item) {
    return null;
  }

  private static final String MEAL_LABELS_KEY = "meal_labels";
}
