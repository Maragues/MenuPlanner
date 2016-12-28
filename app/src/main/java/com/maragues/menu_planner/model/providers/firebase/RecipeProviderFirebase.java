package com.maragues.menu_planner.model.providers.firebase;

import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.model.providers.IRecipeProvider;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by miguelaragues on 28/12/16.
 */

public class RecipeProviderFirebase implements IRecipeProvider {
  @Override
  public Observable<List<Recipe>> list() {
    return null;
  }
}
