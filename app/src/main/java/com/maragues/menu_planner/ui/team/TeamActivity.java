package com.maragues.menu_planner.ui.team;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TeamActivity extends BaseLoggedInActivity<TeamPresenter, ITeam>
        implements ITeam {

  @BindView(R.id.team_recycler_view)
  RecyclerView teamRecyclerView;

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

  List<UserGroup> teamMembers = new ArrayList<>();
  TeamAdapter adapter = new TeamAdapter(teamMembers);

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
}
