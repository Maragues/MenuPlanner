package com.maragues.menu_planner.presentation.view.recipe;

import com.arellomobile.mvp.MvpView;

public interface EditRecipeView extends MvpView {
  void finish();

  String getRecipeTitle();

  String getRecipeDescription();
}
