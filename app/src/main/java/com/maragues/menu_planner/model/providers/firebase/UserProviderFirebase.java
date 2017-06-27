package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.BaseFirebaseKeys;
import com.maragues.menu_planner.model.Group;
import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.model.providers.IUserProvider;

import org.reactivestreams.Publisher;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by miguelaragues on 30/12/16.
 */

public class UserProviderFirebase extends BaseProviderFirebase<User> implements IUserProvider {
  private static final String TAG = UserProviderFirebase.class.getSimpleName();

  public UserProviderFirebase() {
    super(User.class);
  }

  @Override
  /*
  Creating a user is a 3 step process
  1. Create the user
  2. Create the group with user as admin
  3. Once we know the groupId, assign it to user

  It could be done in a single step with a Map and if we managed the groups in this class,
  but I choosed to delegate Group management to its own provider.
   */
  public Single<User> create(@NonNull User user) {
    if (App.appComponent.textUtils().isEmpty(user.id()))
      throw new IllegalArgumentException("Id cannot be empty for user");

    return Single.create(new SingleOnSubscribe<User>() {
      @Override
      public void subscribe(SingleEmitter<User> e) throws Exception {
        getUserReference(user.id()).runTransaction(new Transaction.Handler() {
          @Override
          public Transaction.Result doTransaction(MutableData mutableData) {
            if (mutableData.getValue() == null) {
              mutableData.setValue(user.toFirebaseValue());

              return Transaction.success(mutableData);
            }

            return Transaction.abort();
          }

          @Override
          public void onComplete(DatabaseError databaseError, boolean commited, DataSnapshot dataSnapshot) {
            if (databaseError != null)
              e.onError(databaseError.toException());
            else {
              if (App.appComponent.textUtils().isEmpty(user.groupId())) {
                createGroupForUser(user, e);
              } else {
                addUserToGroup(user, e);
              }
            }
          }
        });
      }
    })
            .subscribeOn(Schedulers.io());
  }

  private void addUserToGroup(User user, SingleEmitter<User> e) {
    App.appComponent.groupProvider().get(user.groupId())
            .map(group -> group.addWithRole(user, Group.ROLE_USER))
            .flatMap(group -> App.appComponent.groupProvider().update(group))
            .subscribe(ignore -> e.onSuccess(user), e::onError);
  }

  private void createGroupForUser(User createdUser, SingleEmitter<User> e) {
    App.appComponent.groupProvider().create(Group.empty(), createdUser)
            .subscribe(group -> {
              Map<String, Object> userGroupIdMap = new HashMap<>();
              userGroupIdMap.put(BaseFirebaseKeys.GROUP_ID_KEY, group.id());

              getUserReference(createdUser.id())
                      .updateChildren(userGroupIdMap)
                      .addOnCompleteListener(task1 -> e.onSuccess(createdUser.withGroupId(group.id())));
            });
  }

  @Override
  @Nullable
  public String getUid() {
    if (FirebaseAuth.getInstance() == null || FirebaseAuth.getInstance().getCurrentUser() == null)
      return null;

    return FirebaseAuth.getInstance().getCurrentUser().getUid();
  }

  @Nullable
  @Override
  public String getGroupId() {
    return App.appComponent.signInPreferences().getGroupId();
  }

  @Override
  public Single<Boolean> exists(@NonNull User user) {
    if (user.id() == null)
      return Single.just(false);

    return Single.create(e ->
            getUserReference(user.id()).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                e.onSuccess(dataSnapshot.exists());
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {
                if (!handleDatabasError(databaseError))
                  e.onError(databaseError.toException());
              }
            })
    );
  }

  @Override
  public Maybe<User> get(String uid) {
    return Maybe.create(e ->
            getUserReference(uid).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                  e.onSuccess(User.create(dataSnapshot));
                else
                  e.onComplete();
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {
                if (!handleDatabasError(databaseError))
                  e.onError(databaseError.toException());
              }
            })
    );
  }

  @Override
  public Single<String> generateKey() {
    return Single.just(getReference().child(USERS_KEY).push().getKey());
  }

  private DatabaseReference getUserReference(String uid) {
    return getReference().child(USERS_KEY).child(uid);
  }

  static final String USERS_KEY = "users";
}
