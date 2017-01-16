package com.maragues.menu_planner.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.List;

/**
 * Created by miguelaragues on 16/1/17.
 */
@AutoValue
public abstract class MealSlot {

  @NonNull
  public abstract LocalDateTime dateTime();

  @Nullable
  public abstract String name();

  @Nullable
  public abstract String mealId();

  @Nullable
  public abstract List<String> recipes();

  public static MealSlot.Builder builder() {
    return new AutoValue_MealSlot.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract MealSlot.Builder setName(String value);

    public abstract MealSlot.Builder setMealId(String value);

    public abstract MealSlot.Builder setDateTime(LocalDateTime value);

    public abstract MealSlot.Builder setRecipes(List<String> value);

    public abstract MealSlot build();
  }

  public static MealSlot fromLocalDate(@NonNull LocalDate date) {
    return builder().setDateTime(date.atStartOfDay()).build();
  }

  public abstract MealSlot withName(String name);

  public abstract MealSlot withRecipes(List<String> recipes);

  public abstract MealSlot withMealId(String mealId);

  public abstract MealSlot withDateTime(LocalDateTime dateTime);

  public boolean hasRecipes() {
    return recipes() != null && !recipes().isEmpty();
  }
}
