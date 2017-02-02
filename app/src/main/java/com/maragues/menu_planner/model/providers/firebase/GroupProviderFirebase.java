package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Group;
import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.model.providers.IGroupProvider;

import java.util.HashMap;
import java.util.Map;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by miguelaragues on 23/1/17.
 */

public class GroupProviderFirebase extends BaseProviderFirebase<Group> implements IGroupProvider {
  public GroupProviderFirebase() {
    super(Group.class);
  }

  @Override
  public Single<Group> create(@NonNull Group group, @NonNull User creator) {
    if (App.appComponent.textUtils().isEmpty(creator.id()))
      throw new IllegalArgumentException("creator must have an id assigned");

    return Single.create(e -> {
      Group internalGroup = group.withId(creator.id()).withNewStatus(creator, Group.STATUS_OWNER);

      getReference().updateChildren(synchronizableToMap(internalGroup), (databaseError, databaseReference) -> {
        if (databaseError != null)
          e.onError(databaseError.toException());
        else {
          e.onSuccess(internalGroup);
        }
      });
    });
  }

  @Override
  public Single<Group> update(@NonNull Group group) {
    if (App.appComponent.textUtils().isEmpty(group.id()))
      throw new IllegalArgumentException("group must have an id assigned");

    return Single.create(e -> {
      getReference().updateChildren(synchronizableToMap(group), (databaseError, databaseReference) -> {
        if (databaseError != null)
          e.onError(databaseError.toException());
        else {
          e.onSuccess(group);
        }
      });
    });
  }

  @Override
  public Flowable<Group> getUserGroup() {
    return RxFirebaseDatabase
            .observeValueEvent(getReference().child(GROUPS_KEY).child(App.appComponent.userProvider().getGroupId()),
                    Group::create);
  }

  protected Map<String, Object> synchronizableToMap(@NonNull Group group) {
    if (App.appComponent.textUtils().isEmpty(group.id()))
      throw new IllegalArgumentException("Recipe must have key");

    Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/" + GROUPS_KEY + "/" + group.id(), group.toFirebaseValue());

    return childUpdates;
  }

  static final String GROUPS_KEY = "groups";
  static final String GROUP_USERS_KEY = "users";
}
