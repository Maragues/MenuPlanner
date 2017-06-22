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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;

import static com.maragues.menu_planner.test.factories.UserFactory.mockFirebaseUser;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
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
  public void onInvitedToGroupLoaded_storesGroupId(){
    initPresenter();

    String expected = "dads";
    presenter.onInvitedToGroupLoaded(expected);

    assertEquals(expected, presenter.invitedToGroupId);
  }

  /*
  NUMBER OF TIMES LAUNCHED
   */
  @Test
  public void firstLaunch_invokesInvitationGroupIdObservable(){
    App.appComponent.signInPreferences().clear();
    ((MockUserProvider) App.appComponent.userProvider()).setUid(null);

    initPresenter();

    verify(view).invitationGroupIdObservable();
  }

  @Test
  public void firstLaunch_invokesonInvitedToGroupLoadedWithValueFromSingle() {
    String invitedByUserId = "invited id";
    when(view.invitationGroupIdObservable()).thenReturn(Single.just(invitedByUserId));

    initPresenter();

    verify(presenter).onInvitedToGroupLoaded(eq(invitedByUserId));
  }

  @Test
  public void secondLaunch_neverInvokesInvitationGroupIdObservable(){
    App.appComponent.signInPreferences().touchFirstLaunch();
    ((MockUserProvider) App.appComponent.userProvider()).setUid("das");

    initPresenter();

    verify(view, never()).invitationGroupIdObservable();
  }

  @Test
  public void secondLaunch_addsAuthListener() {
    App.appComponent.signInPreferences().touchFirstLaunch();

    initPresenter();

    verify(view).addAuthListener();
  }

  /*
  1st launch, NOT INVITED
   */

  @Test
  public void firstLaunch_invitesFalse_touchesFirstAccess() {
    //in this case, Android takes care and launches our DeepLinkActivity
    App.appComponent.signInPreferences().clear();

    when(view.invitationGroupIdObservable()).thenReturn(Single.just(""));

    initPresenter();

    verify(App.appComponent.signInPreferences()).touchFirstLaunch();
  }

  @Test
  public void firstLaunch_invitesFalse_addsAuthListener() {
    //in this case, Android takes care and launches our DeepLinkActivity
    App.appComponent.signInPreferences().clear();

    when(view.invitationGroupIdObservable()).thenReturn(Single.just(""));

    initPresenter();

    verify(view).addAuthListener();
  }

  /*
  1st launch, INVITED
   */

  @Test
  public void firstLaunch_invitationPresent_addsAuthListener() {
    String invitedByUserId = "invited id";
    when(view.invitationGroupIdObservable()).thenReturn(Single.just(invitedByUserId));

    initPresenter();

    verify(view).addAuthListener();
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

    presenter.userClickedOnJoinTeam();

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

    presenter.userClickedOnStandardSignIn();

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
}