package com.maragues.menu_planner.ui.invitation_received;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.ui.common.BasePresenter;

/**
 * Created by miguelaragues on 28/1/17.
 */

public class InvitationReceivedPresenter extends BasePresenter<IInvitationReceived> {
  @Override
  protected void onAttachView(@NonNull IInvitationReceived view) {
//    do nothing
  }

  void decideNextScreen(@NonNull IInvitationReceived view) {
    String invitedByUserId = view.getInvitedByUserId();
    if (invitedByUserId == null) {
      view.navigateToLauncher();
    } else {
      if (App.appComponent.signInPreferences().hasGroupId())
        view.navigateToAcceptInvitation(invitedByUserId);
      else
        view.navigateToLogin(invitedByUserId);
    }

    view.finish();
  }
}
