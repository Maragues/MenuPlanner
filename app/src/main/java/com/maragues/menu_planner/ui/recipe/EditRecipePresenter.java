package com.maragues.menu_planner.ui.recipe;

import android.util.Patterns;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.ui.BaseLoggedInPresenter;

/**
 * Created by miguelaragues on 3/1/17.
 */
class EditRecipePresenter extends BaseLoggedInPresenter<IEditRecipe.View> {

  boolean attemptSave() {
    if (getView() != null && validateOrNotifyErrors()) {
      Recipe recipe = Recipe.builder()
              .setName(getView().title())
              .setDescription(getView().description())
              .setUrl(getView().url())
              .setUid(App.appComponent.userProvider().getUid())
              .build();

      App.appComponent.recipeProvider().create(recipe);

      return true;
    }

    return false;
  }

  boolean validateOrNotifyErrors() {
    if (getView() != null) {
      boolean validates = true;
      if (App.appComponent.textUtils().isEmpty(getView().title())) {
        getView().showTitleMissingError();

        validates = false;
      }

      if (!App.appComponent.textUtils().isEmpty(getView().url())) {
        boolean validUrl = isValidUrl();

        if (!validUrl)
          getView().showWrongUrlError();

        validates &= validUrl;
      }

      return validates;
    }

    return true;
  }

  boolean isValidUrl() {
    return Patterns.WEB_URL.matcher(getView().url()).matches();
  }

  public void onSaveClicked() {
    if (attemptSave()) {
      getView().finish();
    }
  }
}
