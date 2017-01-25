package com.maragues.menu_planner.model.providers.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maragues.menu_planner.model.ISynchronizable;
import com.maragues.menu_planner.model.providers.IProvider;

/**
 * Created by miguelaragues on 28/12/16.
 */

public abstract class BaseProviderFirebase<T extends ISynchronizable> implements IProvider<T> {
  static final String ID_KEY = "id";

  protected final Class<T> clazz;

  protected BaseProviderFirebase(Class<T> clazz) {
    this.clazz = clazz;
  }

  public DatabaseReference getReference() {
    return FirebaseDatabase.getInstance().getReference();
  }
}
