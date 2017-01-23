package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Group;
import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.model.providers.IGroupProvider;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by miguelaragues on 23/1/17.
 */

public class GroupProviderFirebase extends BaseProviderFirebase<Group> implements IGroupProvider {
  public GroupProviderFirebase() {
    super(Group.class);
  }

  @Override
  public Single<Group> create(@NonNull Group group, @NonNull User creator) {
    return Single.create(new SingleOnSubscribe<Group>() {
      @Override
      public void subscribe(SingleEmitter<Group> e) throws Exception {
        Group internalGroup = assignKey(group).withNewRole(creator, Group.ADMIN_ROLE);

        getReference().updateChildren(synchronizableToMap(internalGroup), (databaseError, databaseReference) -> {
          if (databaseError != null)
            e.onError(databaseError.toException());
          else {
            e.onSuccess(internalGroup);
          }
        });
      }
    });
  }

  protected Map<String, Object> synchronizableToMap(@NonNull Group group) {
    if (App.appComponent.textUtils().isEmpty(group.id()))
      throw new IllegalArgumentException("Recipe must have key");

    Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/" + GROUPS_KEY + "/" + group.id(), group.toFirebaseValue());

    return childUpdates;
  }

  Map<String, Object> toMap(Group group) {
    HashMap<String, Object> result = new HashMap<>();

    result.put(ID_KEY, group.id());

    if (!group.users().isEmpty()) {
      Iterator<String> it = group.users().keySet().iterator();

      while (it.hasNext()) {
        String userId = it.next();
        result.put(userId, group.users().get(userId));
      }
    }

    return result;
  }

  Group assignKey(Group group) {
    String key = getReference().child(GROUPS_KEY).push().getKey();

    return group.withId(key);
  }

  static final String GROUPS_KEY = "groups";
  static final String GROUP_USERS_KEY = "users";
}
