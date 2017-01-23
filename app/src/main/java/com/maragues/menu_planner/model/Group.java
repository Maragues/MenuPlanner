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
  public static final String ADMIN_ROLE = "admin";
  public static final String USER_ROLE = "user";

  @NonNull
  public abstract Map<String, String> users();

  static Group.Builder builder() {
    return new AutoValue_Group.Builder();
  }

  public Group withNewRole(@NonNull User admin, @NonNull String role) {
    if(!role.equals(ADMIN_ROLE) && !role.equals(USER_ROLE))
      throw new IllegalArgumentException("Role must be "+ADMIN_ROLE+" or "+USER_ROLE);

    Map<String, String> users = users();

    users.put(admin.id(), role);

    return withUsers(users);
  }

  public static Group empty() {
    return builder().setUsers(new HashMap<>()).build();
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract Group.Builder setId(String value);

    abstract Group.Builder setUsers(Map<String, String> value);

    abstract Group build();
  }

  public static Group create(DataSnapshot dataSnapshot) {
    return dataSnapshot.getValue(AutoValue_Group.FirebaseValue.class).toAutoValue();
  }

  public Object toFirebaseValue() {
    return new AutoValue_Group.FirebaseValue(this);
  }

  public abstract Group withUsers(Map<String, String> users);
}
