package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.model.providers.IUserProvider;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by miguelaragues on 30/12/16.
 */

public class UserProviderFirebase extends BaseProviderFirebase<User> implements IUserProvider {
  private static final String TAG = UserProviderFirebase.class.getSimpleName();

  @Override
  public Single<User> create(@NonNull UserInfo userInfo) {

    /*getReference().child(USERS_KEY).child(userInfo.getUid()).setValue(User.fromUserInfo(userInfo).toFirebaseValue())
            .addOnSuccessListener(new OnSuccessListener<Void>() {
      @Override
      public void onSuccess(Void aVoid) {

        Log.d(TAG,"UserProviderFirebase onSuccess");
      }
    })
    .addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Log.d(TAG,"UserProviderFirebase onFailure");

        e.printStackTrace();
      }
    });*/

    return Single.create(new SingleOnSubscribe<User>() {
      @Override
      public void subscribe(SingleEmitter<User> e) throws Exception {
        getReference().child(USERS_KEY).child(userInfo.getUid()).runTransaction(new Transaction.Handler() {
          @Override
          public Transaction.Result doTransaction(MutableData mutableData) {
            if (mutableData.getValue() == null) {
              Log.d(TAG, "UserProviderFirebase Creating user");
              mutableData.setValue(User.fromUserInfo(userInfo).toFirebaseValue());
              return Transaction.success(mutableData);
            }

            Log.d(TAG, "UserProviderFirebase Aborting transaction");
            return Transaction.abort();
          }

          @Override
          public void onComplete(DatabaseError databaseError, boolean commited, DataSnapshot dataSnapshot) {
            if (databaseError != null)
              e.onError(databaseError.toException());
            else {
              e.onSuccess(User.create(dataSnapshot));
            }
          }
        });
      }
    })
            .subscribeOn(Schedulers.computation());
  }

  @Override
  @Nullable
  public String getUid() {
    if (FirebaseAuth.getInstance() == null || FirebaseAuth.getInstance().getCurrentUser() == null)
      return null;

    return FirebaseAuth.getInstance().getCurrentUser().getUid();
  }

  static final String USERS_KEY = "users";

  @Override
  public void create(@NonNull User item) {
    //TODO do we need this?
  }
}
