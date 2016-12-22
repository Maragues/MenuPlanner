package com.maragues.menu_planner.model;

import android.database.Cursor;

import com.google.auto.value.AutoValue;
import com.maragues.menu_planner.RecipeModel;

import rx.functions.Func1;

/**
 * Created by miguelaragues on 22/12/16.
 */
@AutoValue
public abstract class Recipe implements RecipeModel, ISynchronizable {
  public static final Factory<Recipe> FACTORY = new Factory<>(AutoValue_Recipe::new);

  public static Func1<Cursor, Recipe> MAPPER_FUNC = cursor -> FACTORY.select_allMapper().map(cursor);
}
