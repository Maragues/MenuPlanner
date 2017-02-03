package com.maragues.menu_planner.ui.team;

import com.maragues.menu_planner.model.UserGroup;
import com.maragues.menu_planner.ui.common.IBaseLoggedInView;

import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

import java.util.List;

/**
 * Created by miguelaragues on 27/1/17.
 */

public interface ITeam extends IBaseLoggedInView {
  void showUsers(List<UserGroup> users);

  @CallOnMainThread
  void inviteUserWithId(String expectedKey);
}
