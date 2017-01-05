package com.maragues.menu_planner.ui.recipe;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.ui.BaseLoggedInPresenter;

/**
 * Created by miguelaragues on 3/1/17.
 */
class EditRecipePresenter extends BaseLoggedInPresenter<IEditRecipe.View> {

  void onHomeClicked() {
    attemptSave();

    getView().finish();
  }

  void attemptSave() {
    if (getView() != null && validateOrNotifyErrors()) {
      Recipe recipe = Recipe.builder()
              .setName(getView().title())
              .setDescription(getView().description())
              .setUid(App.appComponent.userProvider().getUid())
              .build();

      App.appComponent.recipeProvider().create(recipe);
    }
  }

  boolean validateOrNotifyErrors() {
    if (getView() != null) {
      boolean validates = true;
      if (App.appComponent.textUtils().isEmpty(getView().title())) {
        getView().showTitleMissingError();

        validates = false;
      }

      return validates;
    }

    return true;
  }

  void onBackPressed() {
    attemptSave();
  }
}
