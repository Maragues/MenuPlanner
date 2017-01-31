package com.maragues.menu_planner.ui.login;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.ui.common.IBaseview;

import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;
import net.grandcentrix.thirtyinch.distinctuntilchanged.DistinctUntilChanged;

import io.reactivex.Observable;

/**
 * Created by miguelaragues on 13/1/17.
 */

public interface ILogin extends IBaseview {
  void hideProgressBar();

  @DistinctUntilChanged
  void navigateToHome();

  void finish();

  Observable<Boolean> invitationObservable();

  void checkInvitations();

  void addAuthListener();

  @Nullable
  String getInvitedByUserId();

  @CallOnMainThread
  void showInvitationLayout(@NonNull User invitedByUser);
}
