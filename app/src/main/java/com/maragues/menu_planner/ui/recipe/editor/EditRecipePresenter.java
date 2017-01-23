package com.maragues.menu_planner.ui.recipe.editor;

import android.support.annotation.NonNull;
import android.util.Patterns;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.ui.common.BaseLoggedInPresenter;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by miguelaragues on 3/1/17.
 */
class EditRecipePresenter extends BaseLoggedInPresenter<IEditRecipe.View> {

  private static class RecipeNotValidException extends Throwable {
  }

  @NonNull
  Single<Recipe> attemptSave() {
    if (getView() != null && validateOrNotifyErrors()) {
      Recipe recipe = Recipe.builder()
              .setName(getView().title())
              .setDescription(getView().description())
              .setUrl(getView().url())
              .setUserId(App.appComponent.userProvider().getUid())
              .build();

      return App.appComponent.recipeProvider().create(recipe);
    }

    return Single.error(new RecipeNotValidException());
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
    attemptSave()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onSaveSuccess, this::onSaveFailed);
  }

  private void onSaveSuccess(Recipe recipe) {
    getView().storeResult(recipe);

    getView().finish();
  }

  private void onSaveFailed(Throwable t) {
    if (t instanceof RecipeNotValidException) {

    } else {
      // TODO: 20/1/17 Handle error
    }
  }
}
