package com.maragues.menu_planner.model.providers;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.Group;
import com.maragues.menu_planner.model.User;

import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by miguelaragues on 23/1/17.
 */

public interface IGroupProvider extends IProvider<Group> {
  Single<Group> create(@NonNull Group group, @NonNull User creator);

  Flowable<Group> getUserGroup();
}
