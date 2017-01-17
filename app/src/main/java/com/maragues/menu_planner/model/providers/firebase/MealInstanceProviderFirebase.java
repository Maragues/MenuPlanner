package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.providers.IMealInstanceProvider;

/**
 * Created by miguelaragues on 17/1/17.
 */

public class MealInstanceProviderFirebase extends BaseProviderFirebase<MealInstance>
        implements IMealInstanceProvider {
  @Override
  public void create(@NonNull MealInstance mealInstance) {

  }
}
