package com.maragues.menu_planner.test.mock.providers;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.model.providers.IRecipeProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by miguelaragues on 4/1/17.
 */

public class MockRecipeProvider implements IRecipeProvider {
  private List<Recipe> recipes = new ArrayList<>();

  @Override
  public Observable<List<Recipe>> list() {
    return Observable.just(recipes);
  }

  @Override
  public void create(@NonNull Recipe recipe) {
    recipes.add(recipe);
  }
}
