package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.maragues.menu_planner.model.ISynchronizable;
import com.maragues.menu_planner.model.providers.IListableProvider;

import java.util.List;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Flowable;

/**
 * Created by miguelaragues on 17/1/17.
 */

public abstract class BaseListableFirebaseProvider<T extends ISynchronizable>
        extends BaseProviderFirebase<T>
        implements IListableProvider<T> {

  protected BaseListableFirebaseProvider(Class<T> clazz) {
    super(clazz);
  }

  @NonNull
  @Override
  public Flowable<List<T>> list() {
    return RxFirebaseDatabase
            .observeSingleValueEvent(listQuery(), getDataSnapshotListDataSnapshotMapper());
  }

  @NonNull
  private DataSnapshotMapper<DataSnapshot, List<T>> getDataSnapshotListDataSnapshotMapper() {
    return DataSnapshotMapper.listOf(clazz);
  }

  @Nullable
  @Override
  public Flowable<T> get(@NonNull String id) {
    return RxFirebaseDatabase.observeSingleValueEvent(getReference(), this::snapshotToInstance);
  }

  protected abstract Query listQuery();

  protected abstract T snapshotToInstance(DataSnapshot dataSnapshot);
}
