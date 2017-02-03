package com.maragues.menu_planner.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;
import com.maragues.menu_planner.App;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

/**
 * Created by miguelaragues on 27/1/17.
 */
@FirebaseValue
@AutoValue
public abstract class UserGroup implements ISynchronizable<UserGroup> {

  @NonNull
  public abstract String name();

  @NonNull
  public abstract String role();

  public static Builder builder() {
    return new AutoValue_UserGroup.Builder();
  }

  public static UserGroup fromUser(@NonNull User user, String status) {
    if(App.appComponent.textUtils().isEmpty(user.name()))
      throw new IllegalArgumentException("User must have a name");

    if(App.appComponent.textUtils().isEmpty(user.id()))
      throw new IllegalArgumentException("User must have a id");

    return builder()
            .setName(user.name())
            .setRole(status)
            .setId(user.id())
            .build();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setName(String value);

    public abstract Builder setId(String value);

    public abstract Builder setRole(String value);

    public abstract UserGroup build();
  }

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
    return role().equals(Group.ROLE_ADMIN);
  }

  public boolean isUser() {
    return role().equals(Group.ROLE_USER);
  }

  public boolean isOwner() {
    return role().equals(Group.ROLE_OWNER);
  }
}
