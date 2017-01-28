package com.maragues.menu_planner.ui.team;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.UserGroup;
import com.maragues.menu_planner.ui.common.BaseLoggedInActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TeamActivity extends BaseLoggedInActivity<TeamPresenter, ITeam>
        implements ITeam {

  private static final int REQUEST_INVITE = 7;
  private static final String TAG = TeamActivity.class.getSimpleName();

  @BindView(R.id.team_recycler_view)
  RecyclerView teamRecyclerView;

  List<UserGroup> teamMembers = new ArrayList<>();
  TeamAdapter adapter = new TeamAdapter(teamMembers);

  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, TeamActivity.class);
  }

  @NonNull
  @Override
  public TeamPresenter providePresenter() {
    return new TeamPresenter();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_team);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    setupList();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_INVITE) {
      if (resultCode == RESULT_OK) {
        // Get the invitation IDs of all sent messages
        String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
        for (String id : ids) {
          Log.d(TAG, "onActivityResult: sent invitation " + id);
        }
      } else {
        // Sending failed or it was canceled, show failure message to the user
        // ...
      }
    }
  }

  private void setupList() {
    teamRecyclerView.setAdapter(adapter);
    teamRecyclerView.setLayoutManager(new LinearLayoutManager(this));
  }

  @OnClick(R.id.team_activity_fab)
  void onAddToTeamClicked() {
    // TODO: 28/1/17 Check Jointfully's Utils.shareApp 

    Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
            .setMessage(getString(R.string.invitation_message))
            .setDeepLink(Uri.parse(getString(R.string.invites_deeplink)))
            .setCallToActionText(getString(R.string.invitation_ca))
            .build();
    startActivityForResult(intent, REQUEST_INVITE);
  }

  @Override
  public void showUsers(List<UserGroup> users) {
    teamMembers.clear();
    teamMembers.addAll(users);

    // TODO: 27/1/17 Use DiffUtil
    adapter.notifyDataSetChanged();
  }
}
