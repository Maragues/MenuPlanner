package com.maragues.menu_planner.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;

import java.util.List;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

/**
 * Created by miguelaragues on 22/12/16.
 */
@AutoValue
@FirebaseValue
public abstract class Recipe implements ISynchronizable<Recipe> {

  @Nullable //so that we can represent user-recipes
  public abstract String userId();

  @Nullable //so that we can represent user-recipes
  public abstract String groupId();

  public abstract String name();

  @Nullable
  public abstract String url();

  @Nullable
  public abstract String description();

  @Nullable
  public abstract List<String> ingredients();

  public static Builder builder() {
    return new AutoValue_Recipe.Builder();
  }

  public static Recipe empty(@NonNull String name) {
    return builder().setName(name).build();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setName(String value);

    public abstract Builder setId(String value);

    public abstract Builder setUserId(String value);

    public abstract Builder setGroupId(String value);

    public abstract Builder setUrl(String value);

    public abstract Builder setDescription(String value);

    public abstract Builder setIngredients(List<String> ingredients);

    public abstract Recipe build();
  }

  public abstract Recipe withUserId(String userId);

  public abstract Recipe withName(String name);

  public abstract Recipe withUrl(String url);

  public abstract Recipe withDescription(String description);

  public abstract Recipe withIngredients(List<String> ingredients);

  public abstract Recipe withGroupId(String groupId);

  @Exclude
  public String shortDescription() {
    //TODO return short description
    return description();
  }

  public boolean validates() {
    return !TextUtils.isEmpty(name());
  }

  public static Recipe create(DataSnapshot dataSnapshot) {
    return dataSnapshot.getValue(AutoValue_Recipe.FirebaseValue.class)
            .toAutoValue()
            .withId(dataSnapshot.getKey());
  }

  public Object toFirebaseValue() {
    return new AutoValue_Recipe.FirebaseValue(this);
  }
}
