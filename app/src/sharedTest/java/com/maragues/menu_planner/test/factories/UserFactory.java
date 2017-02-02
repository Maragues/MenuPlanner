package com.maragues.menu_planner.test.factories;

import com.google.firebase.auth.FirebaseUser;
import com.maragues.menu_planner.model.User;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by miguelaragues on 23/1/17.
 */

public abstract class UserFactory {
  public static final String DEFAULT_UID = "E7uvIt9HU3dJk5ozpFfc8u2DUk72";
  public static final String DEFAULT_NAME = "Kokito kolega";
  public static final String DEFAULT_EMAIL = "ko@ko.ko";
  public static final String DEFAULT_PROVIDER = "firebase";

  private UserFactory() {
  }

  public static User base() {
    return User.empty().withId(DEFAULT_UID).withName(DEFAULT_NAME);
  }


  public static FirebaseUser mockFirebaseUser() {
    FirebaseUser mockedFirebaseUser = mock(FirebaseUser.class);

    doReturn(UserFactory.DEFAULT_NAME).when(mockedFirebaseUser).getDisplayName();
    doReturn(UserFactory.DEFAULT_EMAIL).when(mockedFirebaseUser).getEmail();
    doReturn(UserFactory.DEFAULT_UID).when(mockedFirebaseUser).getUid();
    doReturn(UserFactory.DEFAULT_PROVIDER).when(mockedFirebaseUser).getProviderId();

    return mockedFirebaseUser;
  }

  public static User base(String id) {
    return base().withId(id);
  }
}
