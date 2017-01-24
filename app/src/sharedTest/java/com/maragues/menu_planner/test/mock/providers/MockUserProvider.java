package com.maragues.menu_planner.test.mock.providers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.UserInfo;
import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.model.providers.IUserProvider;
import com.maragues.menu_planner.test.factories.GroupFactory;

import io.reactivex.Maybe;
import io.reactivex.Single;

import static com.maragues.menu_planner.test.factories.UserFactory.DEFAULT_UID;

/**
 * Created by miguelaragues on 4/1/17.
 */

public class MockUserProvider extends MockBaseProvider<User> implements IUserProvider {

  private String uuid = DEFAULT_UID;
  private String groupId = GroupFactory.DEFAULT_GROUP_ID;

  public void setUid(String uuid) {
    this.uuid = uuid;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
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

  @Nullable
  @Override
  public String getGroupId() {
    return groupId;
  }

  @Override
  public Single<Boolean> exists(UserInfo firebaseUser) {
    return null;
  }

  @Override
  public Maybe<User> get(String uid) {
    return null;
  }
}
