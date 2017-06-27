package com.maragues.menu_planner.model.providers.firebase;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maragues.menu_planner.model.ISynchronizable;
import com.maragues.menu_planner.model.providers.IProvider;

/**
 * Created by miguelaragues on 28/12/16.
 */

public abstract class BaseProviderFirebase<T extends ISynchronizable> implements IProvider<T> {
  protected final Class<T> clazz;

  protected BaseProviderFirebase(Class<T> clazz) {
    this.clazz = clazz;
  }

  public DatabaseReference getReference() {
    return FirebaseDatabase.getInstance().getReference();
  }

  protected boolean handleDatabasError(DatabaseError databaseError) {
    switch (databaseError.getCode()) {
      case DatabaseError.DATA_STALE:
      case DatabaseError.OPERATION_FAILED:
      case DatabaseError.PERMISSION_DENIED:
      case DatabaseError.DISCONNECTED:
      case DatabaseError.EXPIRED_TOKEN:
      case DatabaseError.INVALID_TOKEN:
      case DatabaseError.UNAVAILABLE:
      case DatabaseError.OVERRIDDEN_BY_SET:
      case DatabaseError.WRITE_CANCELED:
      case DatabaseError.UNKNOWN_ERROR:
      case DatabaseError.MAX_RETRIES:
        return false;
      case DatabaseError.USER_CODE_EXCEPTION:
      case DatabaseError.NETWORK_ERROR:
    }

    return true;
  }
}
