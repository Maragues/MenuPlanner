package com.maragues.menu_planner.ui.recipe;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.ui.BaseLoggedInPresenter;

/**
 * Created by miguelaragues on 3/1/17.
 */
class EditRecipePresenter extends BaseLoggedInPresenter<IEditRecipeView> {

  void onHomeClicked() {
    attemptSave();

    getView().finish();
  }

  void attemptSave() {
    if (getView() != null && validates()) {
      Recipe recipe = Recipe.builder()
              .setName(getView().getRecipeTitle())
              .setDescription(getView().getRecipeDescription())
              .setUid(FirebaseAuth.getInstance().getCurrentUser().getUid())
              .build();

      App.appComponent.recipeProvider().create(recipe);
    }
  }

  boolean validates() {
    return getView() != null && !TextUtils.isEmpty(getView().getRecipeTitle());
  }

  void onBackPressed() {
    attemptSave();
  }
}
