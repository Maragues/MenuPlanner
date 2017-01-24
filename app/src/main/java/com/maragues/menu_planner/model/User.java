package com.maragues.menu_planner.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.IgnoreExtraProperties;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

/**
 * Created by miguelaragues on 30/12/16.
 */
@AutoValue
@FirebaseValue
@IgnoreExtraProperties
public abstract class User implements ISynchronizable<User> {
  @Nullable
  public abstract String name();

  @Nullable
  public abstract String email();

  @Nullable
  public abstract String photoUrl();

  @Nullable
  public abstract String providerId();

  @Nullable
  public abstract String groupId();

  static User.Builder builder() {
    return new AutoValue_User.Builder();
  }

  public static User empty() {
    return builder().build();
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setName(String value);

    abstract Builder setEmail(String value);

    abstract Builder setId(String value);

    abstract Builder setPhotoUrl(String value);

    abstract Builder setProviderId(String value);

    abstract Builder setGroupId(String value);

    abstract User build();
  }

  public static User create(DataSnapshot dataSnapshot) {
    return dataSnapshot.getValue(AutoValue_User.FirebaseValue.class).toAutoValue();
  }

  public Object toFirebaseValue() {
    return new AutoValue_User.FirebaseValue(this);
  }

  //model shouldn't know about firebase, this should be elsewhere
  public static User fromUserInfo(@NonNull UserInfo userInfo) {
    Builder builder = builder()
            .setName(userInfo.getDisplayName())
            .setEmail(userInfo.getEmail())
            .setId(userInfo.getUid())
            .setProviderId(userInfo.getProviderId());

    if (userInfo.getPhotoUrl() != null)
      builder.setPhotoUrl(userInfo.getPhotoUrl().toString());

    return builder.build();
  }

  public abstract User withGroupId(String groupId);

  public abstract User withEmail(String email);
}
