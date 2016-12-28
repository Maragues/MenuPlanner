package com.maragues.menu_planner.model;

import com.google.auto.value.AutoValue;

import java.util.List;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

/**
 * Created by miguelaragues on 22/12/16.
 */
@AutoValue
@FirebaseValue
public abstract class Recipe implements ISynchronizable {
  String name, url, description;
  List<String> ingredients;
}
