package com.maragues.menu_planner.ui.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.maragues.menu_planner.BuildConfig;
import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.ui.common.BaseActivity;
import com.maragues.menu_planner.ui.home.HomeActivity;
import com.maragues.menu_planner.ui.team.TeamUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by miguelaragues on 28/12/16.
 */

public class LoginActivity extends BaseActivity<LoginPresenter, ILogin>
        implements ILogin, GoogleApiClient.OnConnectionFailedListener {
  private static final String TAG = LoginActivity.class.getSimpleName();
  private static final String EXTRA_INVITEDBY_UID = "extra_invited_by";

  public static Intent createIntent(@NonNull Context context) {
    return createIntent(context, null);
  }

  public static Intent createIntent(@NonNull Context context,
                                    @Nullable String invitedByUID) {
    Intent intent = new Intent(context, LoginActivity.class);

    if (invitedByUID != null)
      intent.putExtra(EXTRA_INVITEDBY_UID, invitedByUID);

    return intent;
  }

  private static final int RC_SIGN_IN = 1;
  private GoogleApiClient googleApiClient;

  private FirebaseAuth mAuth;

  private FirebaseAuth.AuthStateListener mAuthListener;

  @BindView(R.id.login_progress)
  ProgressBar progressBar;

  @BindView(R.id.sign_in_button)
  SignInButton signInButton;

  @BindView(R.id.login_invitedByLayout)
  ViewGroup invitedByLayout;

  @BindView(R.id.login_invitedBy_name)
  TextView invitedByName;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_login);

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.SERVER_CIENT_ID)
            .requestEmail()
            .build();

    googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build();

    mAuth = FirebaseAuth.getInstance();

    mAuthListener = firebaseAuth -> {
      FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

      getPresenter().onFirebaseUserArrived(firebaseUser);
    };
  }

  @OnClick(R.id.sign_in_button)
  void onStandardSignInClicked(){
    getPresenter().userClickedOnStandardSignIn();

    onSignInClicked();
  }

  @OnClick(R.id.login_invitedBy_button)
  void onWantsToJoinTeamClicked(){
    getPresenter().userClickedOnJoinTeam();

    onSignInClicked();
  }

  private void onSignInClicked() {
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
        Log.d(TAG, "role " + result.getStatus().getStatusCode());

        // Google Sign In failed, update UI appropriately
        // ...
      }
    }
  }

  @Override
  public void onStart() {
    super.onStart();

//    addAuthListener();
  }

  @Override
  public void addAuthListener() {
    mAuth.addAuthStateListener(mAuthListener);
  }

  @Nullable
  @Override
  public String getInvitedByUserId() {
    if (getIntent().getData() == null)
      return null;

    return TeamUtils.extractUserId(getIntent().getData());
  }

  @Override
  public void showInvitationLayout(@NonNull User invitedByUser) {
    invitedByLayout.setVisibility(View.VISIBLE);

    invitedByName.setText(invitedByUser.name()+" wants you to join his kitchen");
  }

  @Override
  public void onStop() {
    super.onStop();

    if (mAuthListener != null) {
      mAuth.removeAuthStateListener(mAuthListener);
    }
  }

  private BehaviorSubject<Boolean> hasInvitationSubject = BehaviorSubject.create();

  @Override
  public Observable<Boolean> invitationObservable() {
    return hasInvitationSubject;
  }

  public void checkInvitations() {
    FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
            .addOnSuccessListener(this, data -> {
              if (data == null) {
                Log.d(TAG, "getInvitation: no data");
                return;
              }

              // Get the deep link
              Uri deepLink = data.getLink();

              // Extract invite
              FirebaseAppInvite invite = FirebaseAppInvite.getInvitation(data);
              if (invite != null) {
                String invitationId = invite.getInvitationId();

                Log.d(TAG, "Deep link. " + deepLink + ", InvitationId " + invitationId);
              }

              // Handle the deep link
              // ...
            })
            .addOnFailureListener(this, e -> Log.w(TAG, "getDynamicLink:onFailure", e));
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    // TODO: 30/1/17 Determine if it was the invitations connection check or login
    hasInvitationSubject.onNext(false);
  }

  private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
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
  public LoginPresenter providePresenter() {
    return new LoginPresenter();
  }

  @Override
  public void hideProgressBar() {
    progressBar.setVisibility(View.GONE);
  }

  @Override
  public void navigateToHome() {
    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
  }
}
