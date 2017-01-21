package com.maragues.menu_planner.model;

import android.support.annotation.NonNull;

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

  public abstract String name();

  public abstract String email();

  public abstract String photoUrl();

  public abstract String providerId();

  static User.Builder builder() {
    return new AutoValue_User.Builder();
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract User.Builder setName(String value);

    abstract User.Builder setemail(String value);

    abstract User.Builder setId(String value);

    abstract User.Builder setPhotoUrl(String value);

    abstract User.Builder setProviderId(String value);

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
            .setemail(userInfo.getEmail())
            .setId(userInfo.getUid())
            .setProviderId(userInfo.getProviderId());

    if (userInfo.getPhotoUrl() != null)
      builder.setPhotoUrl(userInfo.getPhotoUrl().toString());

    return builder.build();
  }
}
