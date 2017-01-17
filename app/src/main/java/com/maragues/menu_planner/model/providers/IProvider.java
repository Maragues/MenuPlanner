package com.maragues.menu_planner.model.providers;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.ISynchronizable;

import io.reactivex.Flowable;


/**
 * Created by maragues on 03/05/16.
 */
public interface IProvider<T extends ISynchronizable> {

  Flowable<T> create(@NonNull T item);
}
