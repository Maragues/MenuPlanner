package com.maragues.menu_planner.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by miguelaragues on 28/12/16.
 */

public abstract class BaseLoggedInActivity extends BaseActivity {
  private static final String TAG = BaseLoggedInActivity.class.getSimpleName();

  private FirebaseAuth firebaseAuth;

  private FirebaseAuth.AuthStateListener authListener;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    firebaseAuth = FirebaseAuth.getInstance();

    authListener = new FirebaseAuth.AuthStateListener() {
      @Override
      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
          // User is signed in
          Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
        } else {
          // User is signed out
          Log.d(TAG, "onAuthStateChanged:signed_out");
        }
        // ...
      }
    };
  }


  @Override
  public void onStart() {
    super.onStart();
    firebaseAuth.addAuthStateListener(authListener);
  }

  @Override
  public void onStop() {
    super.onStop();
    if (authListener != null) {
      firebaseAuth.removeAuthStateListener(authListener);
    }
  }
}
