package com.maragues.menu_planner.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

/**
 * Created by miguelaragues on 23/1/17.
 */
@AutoValue
@FirebaseValue
public abstract class Group implements ISynchronizable<Group> {
  public static final String OWNER_ROLE = "owner";
  public static final String ADMIN_ROLE = "admin";
  public static final String USER_ROLE = "user";

  @NonNull
  public abstract Map<String, UserGroup> users();

  static Builder builder() {
    return new AutoValue_Group.Builder();
  }

  public Group withNewRole(@NonNull User admin, @NonNull String role) {
    if (!role.equals(ADMIN_ROLE) && !role.equals(USER_ROLE) && !role.equals(OWNER_ROLE))
      throw new IllegalArgumentException("Role must be " + ADMIN_ROLE
              + " or " + USER_ROLE
              + " or " + OWNER_ROLE);

    Map<String, UserGroup> users = users();

    users.put(admin.id(), UserGroup.empty(admin.name(), role));

    return withUsers(users);
  }

  public static Group empty() {
    return builder().setUsers(new HashMap<>()).build();
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setId(String value);

    abstract Builder setUsers(Map<String, UserGroup> value);

    abstract Group build();
  }

  public static Group create(DataSnapshot dataSnapshot) {
    return dataSnapshot.getValue(AutoValue_Group.FirebaseValue.class).toAutoValue();
  }

  public Object toFirebaseValue() {
    return new AutoValue_Group.FirebaseValue(this);
  }

  public abstract Group withUsers(Map<String, UserGroup> users);
}
