package com.maragues.menu_planner.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.maragues.menu_planner.model.adapters.LocalDateTimeAdapter;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.List;

import me.mattlogan.auto.value.firebase.adapter.FirebaseAdapter;
import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

/**
 * Created by miguelaragues on 16/1/17.
 */
@AutoValue
@FirebaseValue
public abstract class MealInstance implements ISynchronizable {

  @NonNull
  @FirebaseAdapter(LocalDateTimeAdapter.class)
  public abstract LocalDateTime dateTime();

  @Nullable
  public abstract String name();

  @Nullable
  public abstract String mealId();

  @Nullable
  public abstract List<String> recipes();

  public static MealInstance.Builder builder() {
    return new AutoValue_MealInstance.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract MealInstance.Builder setId(String value);

    public abstract MealInstance.Builder setName(String value);

    public abstract MealInstance.Builder setMealId(String value);

    public abstract MealInstance.Builder setDateTime(LocalDateTime value);

    public abstract MealInstance.Builder setRecipes(List<String> value);

    public abstract MealInstance build();
  }

  public static MealInstance fromLocalDate(@NonNull LocalDate date) {
    return builder().setDateTime(date.atStartOfDay()).build();
  }

  public abstract MealInstance withName(String name);

  public abstract MealInstance withRecipes(List<String> recipes);

  public abstract MealInstance withMealId(String mealId);

  public abstract MealInstance withDateTime(LocalDateTime dateTime);

  public boolean hasRecipes() {
    return recipes() != null && !recipes().isEmpty();
  }
}
