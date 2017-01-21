package com.maragues.menu_planner.test.mock.providers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.UserInfo;
import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.model.providers.IUserProvider;

import io.reactivex.Single;

/**
 * Created by miguelaragues on 4/1/17.
 */

public class MockUserProvider extends MockBaseProvider<User> implements IUserProvider {
  public static final String DEFAULT_UID = "E7uvIt9HU3dJk5ozpFfc8u2DUk72";

  private String uuid = DEFAULT_UID;

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  @Override
  public Single<User> create(@NonNull UserInfo userInfo) {
    return Single.just(User.fromUserInfo(userInfo));
  }

  @Nullable
  @Override
  public String getUid() {
    return uuid;
  }
}
