package com.maragues.menu_planner.ui.invitation_received;

import android.support.annotation.Nullable;

import com.maragues.menu_planner.ui.common.IBaseview;

import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

/**
 * Created by miguelaragues on 28/1/17.
 */

public interface IInvitationReceived extends IBaseview{
  @Nullable
  String getInvitedByUserId();

  @CallOnMainThread
  void navigateToLogin(String invitedByUserId);

  @CallOnMainThread
  void navigateToAcceptInvitation(String invitedByUserId);

  @CallOnMainThread
  void navigateToLauncher();

  void finish();
}
