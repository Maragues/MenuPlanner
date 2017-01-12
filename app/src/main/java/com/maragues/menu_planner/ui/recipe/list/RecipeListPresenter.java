package com.maragues.menu_planner.ui.recipe.list;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.ui.BaseLoggedInPresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by miguelaragues on 5/1/17.
 */
class RecipeListPresenter extends BaseLoggedInPresenter<IRecipeList> {
  private BehaviorSubject<Boolean> isLoadingSubject = BehaviorSubject.createDefault(false);
  private BehaviorSubject<List<Recipe>> recipeSubject = BehaviorSubject.createDefault(new ArrayList<>());

  void onAddRecipeClicked() {
    getView().startAddRecipeActivity();
  }

  @Override
  protected void onAttachView(@NonNull IRecipeList view) {
    super.onAttachView(view);

  }

  public Observable<List<Recipe>> loadRecipes(){
    isViewAttached()
    // Don't try and load if we're already loading
    if (isLoadingSubject.getValue()){
      return Observable.empty();
    }

    isLoadingSubject.onNext(true);

    App.appComponent.recipeProvider().list()
            // Concatenate the new recipes to the current recipes list, then emit it via the recipe subject
            .doOnNext(list -> {
              List<Recipe> fullList = new ArrayList<>(recipeSubject.getValue());
              fullList.addAll(list);
              recipeSubject.onNext(fullList);
            })
            .doOnTerminate(() -> isLoadingSubject.onNext(false));
  }

  private static class RecipeViewHolder extends RecipeListAdapter.ViewHolder
}
