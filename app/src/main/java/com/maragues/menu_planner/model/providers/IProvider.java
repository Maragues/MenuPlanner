package com.maragues.menu_planner.model.providers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maragues.menu_planner.model.ISynchronizable;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by maragues on 03/05/16.
 */
public interface IProvider<T extends ISynchronizable> {

  @NonNull
  Observable<List<T>> list();

  @Nullable
  Observable<T> get(long id);

  int count();
}
