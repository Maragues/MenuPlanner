package com.maragues.menu_planner.model.providers;

import android.support.annotation.NonNull;

import com.google.firebase.auth.UserInfo;
import com.maragues.menu_planner.model.User;

import io.reactivex.Single;


/**
 * Created by miguelaragues on 30/12/16.
 */

public interface IUserProvider {

  Single<User> create(@NonNull UserInfo userInfo);
}
