package com.maragues.menu_planner.test.mock.providers;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.Group;
import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.model.providers.IGroupProvider;

import io.reactivex.Single;

/**
 * Created by miguelaragues on 23/1/17.
 */

public class MockGroupProvider extends MockBaseProvider<Group> implements IGroupProvider {
  @Override
  public Single<Group> create(@NonNull Group group, @NonNull User creator) {
    return Single.just(group.withNewRole(creator, Group.ADMIN_ROLE));
  }
}
