package com.maragues.menu_planner.model;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

/**
 * Created by miguelaragues on 22/12/16.
 */
@AutoValue
@FirebaseValue
public abstract class Recipe implements ISynchronizable {
  @Nullable //so that we can create before having a key. The server won't accept empty id
  public abstract String id();

  @Nullable //so that we can represent user-recipes
  public abstract String uid();

  public abstract String name();

  @Nullable
  public abstract String url();

  @Nullable
  public abstract String description();

  @Nullable
  public abstract List<Ingredient> ingredients();

  public static Builder builder() {
    return new AutoValue_Recipe.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setName(String value);

    public abstract Builder setId(String value);

    public abstract Builder setUid(String value);

    public abstract Builder setUrl(String value);

    public abstract Builder setDescription(String value);

    public abstract Builder setIngredients(List<Ingredient> ingredients);

    public abstract Recipe build();
  }

  public abstract Recipe withName(String name);

  public abstract Recipe withUrl(String url);

  public abstract Recipe withDescription(String description);

  public abstract Recipe withId(String id);

  public abstract Recipe withIngredients(List<Ingredient> ingredients);

  @Exclude
  public Map<String, Object> toMap() {
    HashMap<String, Object> result = new HashMap<>();
    result.put("uid", uid());
    result.put("name", name());

    if (!TextUtils.isEmpty(url()))
      result.put("url", url());

    if (!TextUtils.isEmpty(description()))
      result.put("description", description());

    return result;
  }

  @Exclude
  public Map<String, Object> toSummaryMap() {
    HashMap<String, Object> result = new HashMap<>();
    result.put("name", name());

    if (!TextUtils.isEmpty(description()))
      result.put("shortDescription", shortDescription());

    return result;
  }

  private String shortDescription() {
    //TODO return short description
    return description();
  }

  public boolean validates() {
    return !TextUtils.isEmpty(name());
  }

  public static Recipe create(DataSnapshot dataSnapshot) {
    return dataSnapshot.getValue(AutoValue_Recipe.FirebaseValue.class).toAutoValue()
            .withId(dataSnapshot.getKey());
  }
}
