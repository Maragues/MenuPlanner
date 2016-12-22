package com.maragues.menu_planner.model.providers;

import com.maragues.menu_planner.model.Recipe;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by miguelaragues on 22/12/16.
 */

public interface IRecipeProvider {
    Observable<List<Recipe>> list();
}
