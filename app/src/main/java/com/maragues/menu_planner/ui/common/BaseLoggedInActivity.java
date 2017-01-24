package com.maragues.menu_planner.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.grandcentrix.thirtyinch.TiPresenter;
import net.grandcentrix.thirtyinch.TiView;

/**
 * Created by miguelaragues on 28/12/16.
 */

public abstract class BaseLoggedInActivity<P extends TiPresenter<V>, V extends TiView> extends BaseActivity<P, V> {
  private static final String TAG = BaseLoggedInActivity.class.getSimpleName();

  private FirebaseAuth firebaseAuth;

  private FirebaseAuth.AuthStateListener authListener;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    firebaseAuth = FirebaseAuth.getInstance();

    authListener = firebaseAuth1 -> {
      FirebaseUser user = firebaseAuth1.getCurrentUser();

      if (user != null) {
        // User is signed in
        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
      } else {
        // User is signed out
        Log.d(TAG, "onAuthStateChanged:signed_out");
      }
      // ...
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
