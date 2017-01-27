package com.maragues.menu_planner.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

/**
 * Created by miguelaragues on 27/1/17.
 */
@FirebaseValue
@AutoValue
public abstract class UserGroup implements ISynchronizable<UserGroup> {

  @Nullable //so that we can represent user-recipes
  public abstract String userId();

  public abstract String name();

  public abstract String role();


  public static Builder builder() {
    return new AutoValue_UserGroup.Builder();
  }

  public static UserGroup empty(@NonNull String name, @NonNull String role) {
    return builder().setName(name).setRole(role).build();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setName(String value);

    public abstract Builder setId(String value);

    public abstract Builder setUserId(String value);

    public abstract Builder setRole(String value);

    public abstract UserGroup build();
  }

  public abstract UserGroup withUserId(String userId);

  public abstract UserGroup withName(String name);

  public abstract UserGroup withRole(String role);

  public static UserGroup create(DataSnapshot dataSnapshot) {
    return dataSnapshot.getValue(AutoValue_UserGroup.FirebaseValue.class)
            .toAutoValue()
            .withId(dataSnapshot.getKey());
  }

  public Object toFirebaseValue() {
    return new AutoValue_UserGroup.FirebaseValue(this);
  }

  public boolean isAdmin() {
    return role().equals(Group.ADMIN_ROLE);
  }

  public boolean isUser() {
    return role().equals(Group.USER_ROLE);
  }

  public boolean isOwner() {
    return role().equals(Group.OWNER_ROLE);
  }
}
