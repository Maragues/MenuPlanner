package com.maragues.menu_planner.ui.team;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.UserGroup;
import com.maragues.menu_planner.ui.common.BaseLoggedInActivity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TeamActivity extends BaseLoggedInActivity<TeamPresenter, ITeam>
        implements ITeam {

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

  private void setupList() {
    teamRecyclerView.setAdapter(adapter);
    teamRecyclerView.setLayoutManager(new LinearLayoutManager(this));
  }

  @OnClick(R.id.team_activity_fab)
  void onAddToTeamClicked() {
    getPresenter().onAddToTeamClicked();
  }

  @Override
  public void showUsers(List<UserGroup> users) {
    teamMembers.clear();
    teamMembers.addAll(users);

    // TODO: 27/1/17 Use DiffUtil
    adapter.notifyDataSetChanged();
  }

  @Override
  public void inviteUserWithId(String expectedUserId) {
    try {
      Intent sendIntent = new Intent();
      sendIntent.setAction(Intent.ACTION_SEND);

      sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.invitation_title));
      sendIntent.putExtra(
              Intent.EXTRA_TEXT,
              getString(R.string.invitation_message, TeamUtils.inviteUri(expectedUserId))
      );

      sendIntent.setType("text/plain");

      final Intent chooserIntent = Intent.createChooser(
              sendIntent,
              getString(R.string.send_to)
      );

      chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
              new Intent[]{new Intent(this, DummyStartInvitesActivity.class)});

      startActivity(chooserIntent);
    } catch (ActivityNotFoundException e) {
    /*
     * For the rare case where no app responds to this intent
     */
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }
}
