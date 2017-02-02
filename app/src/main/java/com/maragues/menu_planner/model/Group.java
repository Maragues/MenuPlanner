package com.maragues.menu_planner.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;
import com.maragues.menu_planner.App;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

/**
 * Created by miguelaragues on 23/1/17.
 */
@AutoValue
@FirebaseValue
public abstract class Group implements ISynchronizable<Group> {
  public static final String STATUS_OWNER = "owner";
  public static final String STATUS_ADMIN = "admin";
  public static final String STATUS_USER = "user";
  public static final String STATUS_PENDING = "pending";

  @NonNull
  public abstract Map<String, UserGroup> users();

  static Builder builder() {
    return new AutoValue_Group.Builder();
  }

  public Group withNewStatus(@NonNull User user, @NonNull String role) {
    if (!role.equals(STATUS_ADMIN)
            && !role.equals(STATUS_USER)
            && !role.equals(STATUS_OWNER)
            && !role.equals(STATUS_PENDING))
      throw new IllegalArgumentException("Role must be " + STATUS_ADMIN
              + " or " + STATUS_USER
              + " or " + STATUS_PENDING
              + " or " + STATUS_OWNER);

    Map<String, UserGroup> users = users();

    users.put(user.id(), UserGroup.fromUser(user, role));

    assertHasSingleOwner(users);

    return withUsers(users);
  }

  private void assertHasSingleOwner(@NonNull Map<String, UserGroup> users) {
    if (!users.isEmpty()) {
      Iterator<UserGroup> userIt = users.values().iterator();

      boolean hasOwner = false;

      while (userIt.hasNext())
        if (userIt.next().isOwner()) {
          if (hasOwner)
            throw new IllegalStateException("Group can't have two owners");

          hasOwner = true;
        }

      if (hasOwner)
        return;
    }

    throw new IllegalStateException("Group does not have owner");
  }

  public static Group empty() {
    return builder().setUsers(new HashMap<>()).build();
  }

  @NonNull
  public UserGroup owner() {
    Iterator<UserGroup> it = users().values().iterator();
    if (users().size() == 1)
      return it.next();

    while (it.hasNext()) {
      UserGroup candidate = it.next();
      if (candidate.isOwner()) return candidate;
    }

    throw new IllegalStateException("Group does not have owner");
  }


  @Nullable
  public UserGroup getUser(String id) {
    if (App.appComponent.textUtils().isEmpty(id))
      return null;

    return users().get(id);
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
