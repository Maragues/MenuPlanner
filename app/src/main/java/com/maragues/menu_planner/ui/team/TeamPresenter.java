package com.maragues.menu_planner.ui.team;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Group;
import com.maragues.menu_planner.model.UserGroup;
import com.maragues.menu_planner.ui.common.BaseLoggedInPresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by miguelaragues on 27/1/17.
 */

public class TeamPresenter extends BaseLoggedInPresenter<ITeam> {

  Group group;

  @Override
  protected void onCreate() {
    super.onCreate();

    loadGroup();
  }

  void loadGroup() {
    disposables.add(App.appComponent.groupProvider().getUserGroup()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map(group1 -> {
              TeamPresenter.this.group = group1;

              return new ArrayList<>(group1.users().values());
            })
            .doOnNext(this::onUsersLoaded)
            .subscribe());
  }

  void onUsersLoaded(List<UserGroup> users) {
    if (getView() != null) {
      getView().showUsers(users);
    } else {
      sendToView(v -> v.showUsers(users));
    }
  }
}
