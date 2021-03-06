package com.maragues.menu_planner.model.providers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maragues.menu_planner.model.User;

import io.reactivex.Maybe;
import io.reactivex.Single;


/**
 * Created by miguelaragues on 30/12/16.
 */

public interface IUserProvider extends IProvider<User> {

  Single<User> create(@NonNull User user);

  @Nullable
  String getUid();

  @Nullable
  String getGroupId();

  Single<Boolean> exists(@NonNull User user);

  Maybe<User> get(String uid);
}
