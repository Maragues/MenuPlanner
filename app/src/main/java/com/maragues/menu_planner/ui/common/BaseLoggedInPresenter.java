package com.maragues.menu_planner.ui.common;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.User;

import io.reactivex.schedulers.Schedulers;

/**
 * Created by miguelaragues on 3/1/17.
 */

public abstract class BaseLoggedInPresenter<V extends IBaseLoggedInView> extends BasePresenter<V> {
  @Override
  protected void onCreate() {
    super.onCreate();

    if (App.appComponent.textUtils().isEmpty(App.appComponent.userProvider().getGroupId())) {
      String userId = App.appComponent.userProvider().getUid();
      if (App.appComponent.textUtils().isEmpty(userId)) {
        //log out
      } else {
        disposables.add(App.appComponent.userProvider().get(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .doOnSuccess(this::onUserLoaded)
                .doAfterTerminate(this::onUserLoadCompleted)
                .subscribe());
      }
    }
  }

  void onUserLoadCompleted() {
    if (App.appComponent.textUtils().isEmpty(App.appComponent.userProvider().getGroupId())) {
      //no groupId, do something
    }
  }

  protected final void onUserLoaded(User user) {
    App.appComponent.signInPreferences().saveGroupId(user.groupId());
  }
}
