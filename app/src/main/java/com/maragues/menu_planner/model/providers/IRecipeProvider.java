package com.maragues.menu_planner.model.providers;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.Recipe;

import io.reactivex.Single;

/**
 * Created by miguelaragues on 22/12/16.
 */

public interface IRecipeProvider extends IListableProvider<Recipe> {

  Single<Recipe> create(@NonNull Recipe item);

  Single<String> getKey();
}
