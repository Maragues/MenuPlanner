package com.maragues.menu_planner.model.providers;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.Recipe;

import rx.functions.Func1;

/**
 * Created by miguelaragues on 22/12/16.
 */

public class RecipeProviderDelight extends BaseProviderDelight<Recipe> implements IRecipeProvider {
  @NonNull
  @Override
  String getByIdQuery() {
    return Recipe.SELECT_BY_ID;
  }

  @NonNull
  @Override
  String getSelectNonDeletedQuery() {
    return Recipe.SELECT_ALL;
  }

  @Override
  Func1 getMapperFunc() {
    return Recipe.MAPPER_FUNC;
  }

  @NonNull
  @Override
  String getTableName() {
    return Recipe.TABLE_NAME;
  }
}
