package com.maragues.menu_planner.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * Created by maragues on 08/12/2016.
 * <p>
 * - Recipes, 1 or more
 * - Tags (quick, diet, parties, etc.)
 * - Comments (N by any group user)
 * - ¿Score? (1 by any group user)
 * - Total time/Preparation time. You may profit from parallelizing recipes
 * - Occurrences: list/calendar of when this meal has been eaten
 */

@AutoValue
public abstract class Meal implements ISynchronizable {

  @Nullable
  public abstract List<String> recipes();

  public static Meal.Builder builder() {
    return new AutoValue_Meal.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Meal.Builder setId(String value);

    public abstract Meal.Builder setRecipes(List<String> value);

    public abstract Meal build();
  }

}
