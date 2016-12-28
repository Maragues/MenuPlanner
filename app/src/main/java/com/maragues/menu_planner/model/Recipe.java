package com.maragues.menu_planner.model;

import com.google.auto.value.AutoValue;

import java.util.List;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

/**
 * Created by miguelaragues on 22/12/16.
 */
@AutoValue
@FirebaseValue
public abstract class Recipe implements ISynchronizable {
  public abstract String name();

  public abstract String url();

  public abstract String description();

  public abstract List<Ingredient> ingredients();

  static Builder builder() {
    return new AutoValue_Recipe.Builder();
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setName(String value);
    abstract Builder setUrl(String value);
    abstract Builder setDescription(String value);
    abstract Builder setIngredients(List<Ingredient> ingredients);
    abstract Recipe build();
  }

  public abstract Recipe withName(String name);

  public abstract Recipe withUrl(String url);

  public abstract Recipe withDescription(String description);

  public abstract Recipe withIngredients(List<Ingredient> ingredients);
}
