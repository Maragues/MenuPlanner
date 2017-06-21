package com.maragues.menu_planner.ui.invitation_received;

import android.support.annotation.NonNull;
import android.util.Log;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.ui.common.BasePresenter;

/**
 * Created by miguelaragues on 28/1/17.
 */

public class InvitationReceivedPresenter extends BasePresenter<IInvitationReceived> {
  private static final String TAG = InvitationReceivedPresenter.class.getSimpleName();

  @Override
  protected void onAttachView(@NonNull IInvitationReceived view) {
//    do nothing
  }

  void decideNextScreen(@NonNull IInvitationReceived view) {
    String invitedByUserId = view.getInvitedByUserId();
    if (invitedByUserId == null) {
      Log.d(TAG,"InvitationReceivedPresenter navigating to launcher");
      view.navigateToLauncher();
    } else {
      if (App.appComponent.signInPreferences().hasGroupId()) {
        Log.d(TAG,"InvitationReceivedPresenter navigating to accept invitation");
        view.navigateToAcceptInvitation(invitedByUserId);
      }else {
        Log.d(TAG,"InvitationReceivedPresenter navigating to login");
        view.navigateToLogin(invitedByUserId);
      }
    }

    view.finish();
  }
}
