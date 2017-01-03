package com.maragues.menu_planner.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.BuildConfig;
import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.ui.BaseActivity;
import com.maragues.menu_planner.ui.week.WeekActivity;

import net.grandcentrix.thirtyinch.TiPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

/**
 * Created by miguelaragues on 28/12/16.
 */

public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
  private static final String TAG = LoginActivity.class.getSimpleName();

  private static final int RC_SIGN_IN = 1;
  private GoogleApiClient googleApiClient;

  private FirebaseAuth mAuth;

  private FirebaseAuth.AuthStateListener mAuthListener;

  @BindView(R.id.login_progress)
  ProgressBar progressBar;

  Disposable disposable;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_login);

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.SERVER_CIENT_ID)
            .requestEmail()
            .build();

    googleApiClient = new GoogleApiClient.Builder(this)
            .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build();

    mAuth = FirebaseAuth.getInstance();

    mAuthListener = firebaseAuth -> {
      FirebaseUser user = firebaseAuth.getCurrentUser();
      if (user != null) {
        disposable = App.appComponent.userProvider()
                .create(user)
                .doOnSuccess(this::onUserSignedIn)
                .doOnError(Throwable::printStackTrace)
                .subscribe();
      } else {
        // User is signed out
        Log.d(TAG, "onAuthStateChanged:signed_out");
      }
      // ...
    };
  }

  @Override
  protected void onPause() {
    super.onPause();

    if (disposable != null && !disposable.isDisposed())
      disposable.dispose();
  }

  private void onUserSignedIn(User user) {
    progressBar.setVisibility(View.GONE);

    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.id());
    startActivity(new Intent(LoginActivity.this, WeekActivity.class));

    finish();
  }

  @OnClick(R.id.sign_in_button)
  void signIn() {
    progressBar.setVisibility(View.VISIBLE);

    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
    if (requestCode == RC_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      if (result.isSuccess()) {
        // Google Sign In was successful, authenticate with Firebase
        GoogleSignInAccount account = result.getSignInAccount();
        firebaseAuthWithGoogle(account);
      } else {
        Log.d(TAG, "status " + result.getStatus().getStatusCode());

        // Google Sign In failed, update UI appropriately
        // ...
      }
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    mAuth.addAuthStateListener(mAuthListener);
  }

  @Override
  public void onStop() {
    super.onStop();
    if (mAuthListener != null) {
      mAuth.removeAuthStateListener(mAuthListener);
    }
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }

  private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
    Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

    AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
    mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                //AuthStateListener has been invoked at this point or after

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                  Log.w(TAG, "signInWithCredential", task.getException());
                  Toast.makeText(LoginActivity.this, "Authentication failed.",
                          Toast.LENGTH_SHORT).show();
                }
                // ...
              }
            });
  }

  @NonNull
  @Override
  public TiPresenter providePresenter() {
    return null;
  }
}
