package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.BaseFirebaseKeys;
import com.maragues.menu_planner.model.Group;
import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.model.providers.IUserProvider;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
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
  public Single<User> create(@NonNull UserInfo userInfo) {
    return Single.create(new SingleOnSubscribe<User>() {
      @Override
      public void subscribe(SingleEmitter<User> e) throws Exception {
        getUserReference().runTransaction(new Transaction.Handler() {
          @Override
          public Transaction.Result doTransaction(MutableData mutableData) {
            if (mutableData.getValue() == null) {
              mutableData.setValue(User.fromUserInfo(userInfo).toFirebaseValue());

              return Transaction.success(mutableData);
            }

            return Transaction.abort();
          }

          @Override
          public void onComplete(DatabaseError databaseError, boolean commited, DataSnapshot dataSnapshot) {
            if (databaseError != null)
              e.onError(databaseError.toException());
            else {
              User admin = User.create(dataSnapshot);

              App.appComponent.groupProvider().create(Group.empty(), admin)
                      .doOnSuccess(group -> {
                        Map<String, Object> userGroupIdMap = new HashMap<>();
                        userGroupIdMap.put(BaseFirebaseKeys.GROUP_ID_KEY, group.id());

                        getUserReference()
                                .updateChildren(userGroupIdMap)
                                .addOnCompleteListener(task1 -> e.onSuccess(admin.withGroupId(group.id())));
                              }
                      )
                      .subscribe();
            }
          }
        });
      }

      private DatabaseReference getUserReference() {
        return getReference().child(USERS_KEY).child(userInfo.getUid());
      }
    })
            .subscribeOn(Schedulers.io());
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
    return null;
  }

  static final String USERS_KEY = "users";
}
