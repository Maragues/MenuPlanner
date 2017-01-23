package com.maragues.menu_planner.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.adapters.LocalDateTimeAdapter;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.temporal.WeekFields;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.mattlogan.auto.value.firebase.adapter.FirebaseAdapter;
import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

/**
 * Created by miguelaragues on 16/1/17.
 */
@AutoValue
@FirebaseValue
public abstract class MealInstance implements ISynchronizable<MealInstance> {

  @NonNull
  @FirebaseAdapter(LocalDateTimeAdapter.class)
  public abstract LocalDateTime dateTime();

  @Nullable
  public abstract String labelId();

  @Nullable
  public abstract String name();

  @Nullable
  public abstract String mealId();

  @NonNull
  public abstract List<RecipeMeal> recipes();

  public static MealInstance.Builder builder() {
    return new AutoValue_MealInstance.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract MealInstance.Builder setId(String value);

    public abstract MealInstance.Builder setLabelId(String value);

    public abstract MealInstance.Builder setName(String value);

    public abstract MealInstance.Builder setMealId(String value);

    public abstract MealInstance.Builder setDateTime(LocalDateTime value);

    public abstract MealInstance.Builder setRecipes(List<RecipeMeal> value);

    public abstract MealInstance build();
  }

  public static MealInstance fromLocalDate(@NonNull LocalDate date) {
    return fromLocalDateTime(date.atStartOfDay());
  }

  public static MealInstance fromLocalDateTime(@NonNull LocalDateTime dateTime) {
    return builder()
            .setDateTime(dateTime)
            .setRecipes(new ArrayList<>())
            .build();
  }

  public abstract MealInstance withName(String name);

  public abstract MealInstance withLabelId(String labelId);

  public abstract MealInstance withRecipes(List<RecipeMeal> recipes);

  public abstract MealInstance withMealId(String mealId);

  public abstract MealInstance withDateTime(LocalDateTime dateTime);

  public MealInstance fromMeal(Meal meal) {
    if (App.appComponent.textUtils().isEmpty(meal.id()))
      throw new IllegalArgumentException("MealInstance needs a mealId");

    return withMealId(meal.id());
  }

  public boolean hasRecipes() {
    return recipes() != null && !recipes().isEmpty();
  }

  public static MealInstance create(DataSnapshot dataSnapshot) {
    return dataSnapshot.getValue(AutoValue_MealInstance.FirebaseValue.class).toAutoValue();
  }

  public int weekNumber() {
    return dateTime().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
  }
}
