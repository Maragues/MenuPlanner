package com.maragues.menu_planner.ui.login;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.test.factories.UserFactory;
import com.maragues.menu_planner.test.mock.providers.MockUserProvider;
import com.maragues.menu_planner.ui.test.BasePresenterTest;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import io.reactivex.Maybe;
import io.reactivex.Single;

import static com.maragues.menu_planner.test.factories.UserFactory.mockFirebaseUser;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by miguelaragues on 24/1/17.
 */
public class LoginPresenterTest extends BasePresenterTest<ILogin, LoginPresenter> {
  public LoginPresenterTest() {
    super(ILogin.class);

    doReturn(Single.just("")).when(view).invitationGroupIdObservable();
  }

  @NonNull
  @Override
  protected LoginPresenter createPresenter() {
    return new LoginPresenter();
  }

  /*
  ON USER SIGNED IN
   */
  @Test
  public void onUserSignedIn_invokesNavigateToHome() {
    initPresenter();

    presenter.onUserSignedIn(mock(User.class));

    verify(view).navigateToHome();
  }

  @Test
  public void onUserSignedIn_invokesHideProgressBar() {
    initPresenter();

    presenter.onUserSignedIn(mock(User.class));

    verify(view).hideProgressBar();
  }

  @Test
  public void onUserSignedIn_invokesFinish() {
    initPresenter();

    presenter.onUserSignedIn(mock(User.class));

    verify(view).finish();
  }

  /*
  ON INVITED TO GROUP LOADED
   */
  @Test
  public void onInvitedToGroupLoaded_storesGroupId() {
    initPresenter();

    String expected = "dads";
    presenter.onInvitedToGroupLoaded(expected);

    assertEquals(expected, presenter.invitedToGroupId);
  }

  /*
  FIREBASE USER ARRIVED, GOOGLE PROVIDES US WITH THE INFO
   */

  @Test
  public void firebaseUserArrives_exists_invokesOnUserSignedIn() {
    initPresenter();

    UserInfo userInfo = mockFirebaseUser();
    doReturn(UserFactory.DEFAULT_UID).when(userInfo).getUid();

    doReturn(Single.just(true)).when(App.appComponent.userProvider()).exists(any(User.class));
    doReturn(Maybe.just(UserFactory.base())).when(App.appComponent.userProvider()).get(eq(UserFactory.DEFAULT_UID));

    presenter.onFirebaseUserArrived(userInfo);

    verify(presenter).onUserSignedIn(any(User.class));
  }

  @Test
  public void firebaseUserArrives_exists_storesGroupId() {
    initPresenter();

    UserInfo userInfo = mockFirebaseUser();
    doReturn(UserFactory.DEFAULT_UID).when(userInfo).getUid();

    String expectedGroupId = "my best group";
    doReturn(Single.just(true)).when(App.appComponent.userProvider()).exists(any(User.class));
    doReturn(Maybe.just(UserFactory.base().withGroupId(expectedGroupId).withEmail("pepe")))
            .when(App.appComponent.userProvider())
            .get(eq(UserFactory.DEFAULT_UID));

    presenter.onFirebaseUserArrived(userInfo);

    verify(App.appComponent.signInPreferences()).saveGroupId(eq(expectedGroupId));
  }

  @Test
  public void firebaseUserArrives_notExists_createsNewUser() {
    initPresenter();

    UserInfo userInfo = mockFirebaseUser();
    doReturn(Single.just(false)).when(App.appComponent.userProvider()).exists(any(User.class));

    presenter.onFirebaseUserArrived(userInfo);

    verify(App.appComponent.userProvider()).create(any(User.class));
  }

  @Test
  public void firebaseUserArrives_notExists_withInvitation_userWantsToJoin_createsNewUserWithAssignedGroupId() {
    initPresenter();

    FirebaseUser firebaseUserMock = mockFirebaseUser();
    doReturn(Single.just(false)).when(App.appComponent.userProvider()).exists(any(User.class));

    String expectedGroupId = "Rockeros ko";

    presenter.onInvitedToGroupLoaded(expectedGroupId);

    presenter.userWantsToJoinTeam = true;

    presenter.onFirebaseUserArrived(firebaseUserMock);

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

    verify(App.appComponent.userProvider()).create(captor.capture());

    assertEquals(expectedGroupId, captor.getValue().groupId());
  }

  @Test
  public void firebaseUserArrives_notExists_withInvitation_userDoesNotWantToJoin_createsNewUserWithNoGroupId() {
    initPresenter();

    FirebaseUser firebaseUserMock = mockFirebaseUser();
    doReturn(Single.just(false)).when(App.appComponent.userProvider()).exists(any(User.class));

    String expectedGroupId = "Rockeros ko";

    presenter.onInvitedToGroupLoaded(expectedGroupId);

    presenter.onUserClickedSignIn();

    presenter.onFirebaseUserArrived(firebaseUserMock);

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

    verify(App.appComponent.userProvider()).create(captor.capture());

    assertNotSame(expectedGroupId, captor.getValue().groupId());
  }

  @Test
  public void firebaseUserArrives_null_doesNotNavigateToHome() {
    initPresenter();

    presenter.onFirebaseUserArrived(null);

    verify(view, never()).navigateToHome();
  }

  @Test
  public void firebaseUserArrives_null_doesNotFinish() {
    initPresenter();

    presenter.onFirebaseUserArrived(null);

    verify(view, never()).finish();
  }

  /*
  ON USER CLICKED SIGN IN
   */
  @Test
  public void onUserClickedSignIn_noInvitation_invokesOnSignInClicked() {
    initPresenter();

    assertNull(presenter.invitedToGroupId);

    presenter.onUserClickedSignIn();

    verify(view).signIn();
  }

  @Test
  public void onUserClickedSignIn_noInvitation_neverinvokesShowJoinTeamDialog() {
    initPresenter();

    assertNull(presenter.invitedToGroupId);

    presenter.onUserClickedSignIn();

    verify(view, never()).showJoinTeamDialog(anyString());
  }

  @Test
  public void onUserClickedSignIn_invitation_invokesShowJoinTeamDialog() {
    initPresenter();

    String groupId = "dadas";
    presenter.invitedToGroupId = groupId;

    presenter.onUserClickedSignIn();

    verify(view).showJoinTeamDialog(eq(groupId));
  }

  @Test
  public void onUserClickedSignIn_invitation_neverinvokesOnSignInClicked() {
    initPresenter();

    String groupId = "dadas";
    presenter.invitedToGroupId = groupId;

    presenter.onUserClickedSignIn();

    verify(view, never()).signIn();
  }

  /*
  ATTACH VIEW
   */
  @Test
  public void attachView_checksForInvitationCode() {
    initPresenter(false);

    doNothing().when(presenter).checkForInvitation();

    attachView();

    verify(presenter).checkForInvitation();
  }

  /*
  DETACH VIEW
   */
  @Test
  public void detachView_removesAuthListener() {
    initPresenter();

    verify(presenter, never()).removeAuthListener();

    presenter.detachView();

    verify(presenter).removeAuthListener();
  }

  /*
  NUMBER OF TIMES LAUNCHED
   */
  @Test
  public void checkForInvitation_invokesInvitationGroupIdObservable() {
    App.appComponent.signInPreferences().clear();
    ((MockUserProvider) App.appComponent.userProvider()).setUid(null);

    initPresenter(false);

    /*
    this is ugly, since we depend on implementation details of attachView to invoke checkForInvitation
     */
    doNothing().when(presenter).addAuthListener();
    attachView();

    verify(view).invitationGroupIdObservable();
  }

  @Test
  public void checkForInvitation_firstPresenterLaunch_nonEmptyValue_invokesonInvitedToGroupLoadedWithValueFromSingle() {
    String invitedByUserId = "invited id";
    when(view.invitationGroupIdObservable()).thenReturn(Single.just(invitedByUserId));

    initPresenter(false);

    presenter.isFirstPresenterLaunch = true;

    doNothing().when(presenter).onInvitedToGroupLoaded(anyString());
    doNothing().when(presenter).addAuthListener();
    attachView();

    verify(presenter).onInvitedToGroupLoaded(eq(invitedByUserId));
  }

  @Test
  public void checkForInvitation_notFirstPresenterLaunch_nonEmptyValue_neverInvokesonInvitedToGroupLoadedWithValueFromSingle() {
    String invitedByUserId = "invited id";
    when(view.invitationGroupIdObservable()).thenReturn(Single.just(invitedByUserId));

    initPresenter(false);

    presenter.isFirstPresenterLaunch = false;

    doNothing().when(presenter).addAuthListener();
    attachView();

    verify(presenter, never()).onInvitedToGroupLoaded(anyString());
  }

  @Test
  public void checkForInvitation_FirstPresenterLaunch_EmptyValue_neverInvokesOnInvitedToGroupLoadedWithValueFromSingle() {
    String invitedByUserId = "";
    when(view.invitationGroupIdObservable()).thenReturn(Single.just(invitedByUserId));

    initPresenter(false);

    presenter.isFirstPresenterLaunch = false;

    doNothing().when(presenter).addAuthListener();
    attachView();

    verify(presenter, never()).onInvitedToGroupLoaded(anyString());
  }

  @Test
  public void checkForInvitation_firstLaunch_addsAuthListener() {
    initPresenter(false);

    doNothing().when(presenter).addAuthListener();
    presenter.isFirstPresenterLaunch = true;
    attachView();

    verify(presenter).addAuthListener();
  }

  @Test
  public void checkForInvitation_secondLaunch_addsAuthListener() {
    initPresenter(false);

    doNothing().when(presenter).addAuthListener();
    presenter.isFirstPresenterLaunch = false;
    attachView();

    verify(presenter).addAuthListener();
  }
}