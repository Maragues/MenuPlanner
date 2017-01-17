package com.maragues.menu_planner.model.providers;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.ISynchronizable;


/**
 * Created by maragues on 03/05/16.
 */
public interface IProvider<T extends ISynchronizable> {

  void create(@NonNull T item);
}
