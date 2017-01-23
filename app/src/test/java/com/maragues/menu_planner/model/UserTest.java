package com.maragues.menu_planner.model;

import android.net.Uri;

import com.google.firebase.auth.UserInfo;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by miguelaragues on 23/1/17.
 */
public class UserTest {

  @Test
  public void testEmpty() {
    assertNotNull(User.empty());
  }

  @Test
  public void filled_fromUserInfo(){
    String expectedName = "kokito kolega";
    String expectedEmail = "ko@ko.ko";
    String expectedId = "my id";
    String expectedProviderId = "provider id";
    String expectedPhotoUrl = "https://www.google.es";

    UserInfo userInfo = mock(UserInfo.class);
    doReturn(expectedEmail).when(userInfo).getEmail();
    doReturn(expectedName).when(userInfo).getDisplayName();
    doReturn(expectedId).when(userInfo).getUid();
    doReturn(expectedProviderId).when(userInfo).getProviderId();

    Uri uri = mock(Uri.class);
    doReturn(expectedPhotoUrl).when(uri).toString();
    doReturn(uri).when(userInfo).getPhotoUrl();

    User user = User.fromUserInfo(userInfo);

    assertNotNull(user);

    assertEquals(expectedEmail, user.email());
    assertEquals(expectedId, user.id());
    assertEquals(expectedName, user.name());
    assertEquals(expectedProviderId, user.providerId());
    assertEquals(expectedPhotoUrl, user.photoUrl());
  }
}