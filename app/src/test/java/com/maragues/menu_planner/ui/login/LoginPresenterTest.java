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

/**
 * Created by miguelaragues on 24/1/17.
 */
public class LoginPresenterTest extends BasePresenterTest<ILogin, LoginPresenter> {
  public LoginPresenterTest() {
    super(ILogin.class);
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
  INVITES
   */

  @Test
  public void noInvites_addsAuthListener() {
    doReturn(null).when(view).getInvitedByUserId();
    App.appComponent.signInPreferences().touchFirstLaunch();

    initPresenter();

    verify(view).addAuthListener();
  }

  @Test
  public void firstLaunch_subscribesToInvitationObservable() {
    App.appComponent.signInPreferences().clear();

    initPresenter();

    verify(view).invitationObservable();
  }

  @Test
  public void firstLaunch_checksForInvites() {
    App.appComponent.signInPreferences().clear();

    initPresenter();

    verify(view).checkInvitations();
  }

  @Test
  public void firstLaunch_invitesTrue_doesNotNavigateAnywhere() {
    //in this case, Android takes care and launches our DeepLinkActivity
    App.appComponent.signInPreferences().clear();

    BehaviorSubject<Boolean> invitesSubject = BehaviorSubject.create();
    doReturn(invitesSubject).when(view).invitationObservable();

    initPresenter();

    invitesSubject.onNext(true);

    verify(view, never()).navigateToHome();
  }

  @Test
  public void firstLaunch_invitesTrue_touchesFirstAccess() {
    //in this case, Android takes care and launches our DeepLinkActivity
    App.appComponent.signInPreferences().clear();

    BehaviorSubject<Boolean> invitesSubject = BehaviorSubject.create();
    doReturn(invitesSubject).when(view).invitationObservable();

    initPresenter();

    invitesSubject.onNext(true);

    verify(App.appComponent.signInPreferences()).touchFirstLaunch();
  }

  @Test
  public void firstLaunch_invitesFalse() {
    //in this case, Android takes care and launches our DeepLinkActivity
    App.appComponent.signInPreferences().clear();
    ((MockUserProvider) App.appComponent.userProvider()).setUid(null);

    BehaviorSubject<Boolean> invitesSubject = BehaviorSubject.create();
    doReturn(invitesSubject).when(view).invitationObservable();

    initPresenter();

    verify(presenter).checkInvites();

    invitesSubject.onNext(false);

    verify(presenter).onInvitationChecked(eq(false));
  }

  @Test
  public void firstLaunch_invitesFalse_touchesFirstAccess() {
    //in this case, Android takes care and launches our DeepLinkActivity
    App.appComponent.signInPreferences().clear();

    BehaviorSubject<Boolean> invitesSubject = BehaviorSubject.create();
    doReturn(invitesSubject).when(view).invitationObservable();

    initPresenter();

    invitesSubject.onNext(false);

    verify(App.appComponent.signInPreferences()).touchFirstLaunch();
  }

  @Test
  public void firstLaunch_invitesFalse_addsAuthListener() {
    //in this case, Android takes care and launches our DeepLinkActivity
    App.appComponent.signInPreferences().clear();

    BehaviorSubject<Boolean> invitesSubject = BehaviorSubject.create();
    doReturn(invitesSubject).when(view).invitationObservable();

    initPresenter();

    invitesSubject.onNext(false);

    verify(view).addAuthListener();
  }

  @Test
  public void firstLaunch_invitationPresent_loadsUser() {
    String invitedByUserId = "invited id";
    doReturn(invitedByUserId).when(view).getInvitedByUserId();

    initPresenter();

    verify(App.appComponent.userProvider()).get(eq(invitedByUserId));
  }

  @Test
  public void firstLaunch_invitationPresent_showsInvitationLayout() {
    String invitedByUserId = "invited id";
    doReturn(invitedByUserId).when(view).getInvitedByUserId();

    User expectedUser = UserFactory.base().withId(invitedByUserId);
    doReturn(Maybe.just(expectedUser))
            .when(App.appComponent.userProvider())
            .get(eq(invitedByUserId));

    initPresenter();

    verify(view).showInvitationLayout(eq(expectedUser));
  }

  @Test
  public void firstLaunch_invitationPresent_userNotPresent_checksInvitations() {
    String invitedByUserId = "invited id";
    doReturn(invitedByUserId).when(view).getInvitedByUserId();

    //we also need to fake that there are no invitations
    BehaviorSubject<Boolean> invitesSubject = BehaviorSubject.create();
    doReturn(invitesSubject).when(view).invitationObservable();

    doAnswer(new Answer<Maybe<User>>() {
      @Override
      public Maybe<User> answer(InvocationOnMock invocation) throws Throwable {
        return Maybe.create(MaybeEmitter::onComplete);
      }
    })
            .when(App.appComponent.userProvider())
            .get(anyString());

    initPresenter();

    verify(App.appComponent.userProvider()).get(eq(invitedByUserId));

    verify(presenter).checkInvites();
  }

  @Test
  public void firstLaunch_invitationPresent_userNotPresent_addsAuthListener() {
    String invitedByUserId = "invited id";
    doReturn(invitedByUserId).when(view).getInvitedByUserId();

    //we also need to fake that there are no invitations
    BehaviorSubject<Boolean> invitesSubject = BehaviorSubject.create();
    doReturn(invitesSubject).when(view).invitationObservable();

    doAnswer(new Answer<Maybe<User>>() {
      @Override
      public Maybe<User> answer(InvocationOnMock invocation) throws Throwable {
        return Maybe.create(MaybeEmitter::onComplete);
      }
    })
            .when(App.appComponent.userProvider())
            .get(anyString());

    initPresenter();

    verify(App.appComponent.userProvider()).get(eq(invitedByUserId));

    invitesSubject.onNext(false);

    verify(view).addAuthListener();
  }

  @Test
  public void secondLaunch_doesNotCheckForInvites() {
    App.appComponent.signInPreferences().touchFirstLaunch();

    initPresenter();

    verify(view, never()).checkInvitations();
  }

  @Test
  public void secondLaunch_doesNotSubscribeForInvites() {
    App.appComponent.signInPreferences().touchFirstLaunch();

    initPresenter();

    verify(view, never()).invitationObservable();
  }

  @Test
  public void secondLaunch_addsAuthListener() {
    App.appComponent.signInPreferences().touchFirstLaunch();

    initPresenter();

    verify(view).addAuthListener();
  }

  @Test
  public void secondLaunch_invitationPresent_loadsUser() {
    String invitedByUserId = "invited id";
    doReturn(invitedByUserId).when(view).getInvitedByUserId();

    initPresenter();

    verify(App.appComponent.userProvider()).get(eq(invitedByUserId));
  }

  @Test
  public void secondLaunch_invitationPresent_showsInvitationLayout() {
    String invitedByUserId = "invited id";
    doReturn(invitedByUserId).when(view).getInvitedByUserId();

    User expectedUser = UserFactory.base().withId(invitedByUserId);
    doReturn(Maybe.just(expectedUser))
            .when(App.appComponent.userProvider())
            .get(eq(invitedByUserId));

    initPresenter();

    verify(view).showInvitationLayout(eq(expectedUser));
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
    User invitedBy = UserFactory.base().withGroupId(expectedGroupId);

    presenter.onInvitedByUserLoaded(invitedBy);

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
    User invitedBy = UserFactory.base().withGroupId(expectedGroupId);

    presenter.onInvitedByUserLoaded(invitedBy);

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