package com.maragues.menu_planner.presentation.presenter.recipe;


import com.google.firebase.auth.FirebaseAuth;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.presentation.view.recipe.EditRecipeView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class EditRecipePresenter extends MvpPresenter<EditRecipeView> {

  public void onHomeClicked() {
    attemptSave();

    getViewState().finish();
  }


  private void attemptSave() {
    if (validates()) {
      Recipe recipe = Recipe.builder()
              .setName(getViewState().getRecipeTitle())
              .setDescription(getViewState().getRecipeDescription())
              .setUid(FirebaseAuth.getInstance().getCurrentUser().getUid())
              .build();

      App.appComponent.recipeProvider().create(recipe);
    }
  }

  private boolean validates() {
    return !App.appComponent.textUtils().isEmpty(getViewState().getRecipeTitle());
  }

  public void onBackPressed() {
    attemptSave();
  }
}
