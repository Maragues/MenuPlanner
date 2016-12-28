package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maragues.menu_planner.model.ISynchronizable;
import com.maragues.menu_planner.model.providers.IProvider;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by miguelaragues on 28/12/16.
 */

public abstract class BaseProviderFirebase<T extends ISynchronizable> implements IProvider<T> {
  protected final DatabaseReference getReference(){
    return FirebaseDatabase.getInstance().getReference();
  }

  @NonNull
  @Override
  public Observable<List<T>> list() {
    return null;
  }

  @Nullable
  @Override
  public Observable<T> get(long id) {
    return null;
  }

  @Override
  public int count() {
    return 0;
  }
}
