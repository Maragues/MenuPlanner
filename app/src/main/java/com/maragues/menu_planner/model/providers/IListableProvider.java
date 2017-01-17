package com.maragues.menu_planner.model.providers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maragues.menu_planner.model.ISynchronizable;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by miguelaragues on 17/1/17.
 */

public interface IListableProvider<T extends ISynchronizable> extends IProvider<T> {
  @NonNull
  Flowable<List<T>> list();

  @Nullable
  Flowable<T> get(@NonNull String id);
}
