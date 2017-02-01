package com.maragues.menu_planner.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.adapters.LocalDateTimeAdapter;
import com.maragues.menu_planner.model.adapters.LocalTimeAdapter;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
  public abstract String mealId();

  @NonNull
  public abstract Map<String, RecipeMeal> recipes();

  public static MealInstance.Builder builder() {
    return new AutoValue_MealInstance.Builder();
  }

  public String formattedTime() {
    return new LocalTimeAdapter().toFirebaseValue(dateTime().toLocalTime());
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract MealInstance.Builder setId(String value);

    public abstract MealInstance.Builder setLabelId(String value);

    public abstract MealInstance.Builder setMealId(String value);

    public abstract MealInstance.Builder setDateTime(LocalDateTime value);

    public abstract MealInstance.Builder setRecipes(Map<String, RecipeMeal> value);

    public abstract MealInstance build();
  }

  public static MealInstance fromLocalDateTime(@NonNull LocalDateTime dateTime) {
    return builder()
            .setDateTime(dateTime)
            .setRecipes(new HashMap<>())
            .setId(String.valueOf(dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()))
            .build();
  }

  public abstract MealInstance withLabelId(String labelId);

  public abstract MealInstance withRecipes(Map<String, RecipeMeal> recipes);

  public abstract MealInstance withMealId(String mealId);

  public abstract MealInstance withDateTime(LocalDateTime dateTime);

  public MealInstance withLabel(MealInstanceLabel label) {
    LocalDateTime newDateTime = dateTime().with(label.time());
    return withLabelId(label.id())
            .withDateTime(newDateTime)
            .withId(String.valueOf(newDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()));
  }

  public MealInstance fromMeal(Meal meal) {
    if (App.appComponent.textUtils().isEmpty(meal.id()))
      throw new IllegalArgumentException("MealInstance needs a mealId");

    return withMealId(meal.id())
            .withRecipes(new HashMap<>(meal.recipes()));
  }

  public boolean hasRecipes() {
    return !recipes().isEmpty();
  }

  public static MealInstance create(DataSnapshot dataSnapshot) {
    return dataSnapshot.getValue(AutoValue_MealInstance.FirebaseValue.class)
            .toAutoValue()
            .withId(dataSnapshot.getKey());
  }

  public Object toFirebaseValue() {
    return new AutoValue_MealInstance.FirebaseValue(this);
  }

  public Collection<RecipeMeal> recipeCollection(){
    return new ArrayList<>(recipes().values());
  }
}
