package com.maragues.menu_planner.model;

import com.google.auto.value.AutoValue;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

/**
 * Created by miguelaragues on 28/12/16.
 */
@AutoValue
@FirebaseValue
public abstract class Ingredient implements ISynchronizable{
  public abstract String name();
}
