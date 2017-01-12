package com.maragues.menu_planner.ui.recipe.list;

import android.support.annotation.NonNull;
import android.util.Log;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.ui.BaseLoggedInPresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by miguelaragues on 5/1/17.
 */
class RecipeListPresenter extends BaseLoggedInPresenter<IRecipeList> {
  private static final String TAG = RecipeListPresenter.class.getSimpleName();

  private BehaviorSubject<Boolean> isLoadingSubject = BehaviorSubject.createDefault(false);
  private BehaviorSubject<List<Recipe>> recipeSubject = BehaviorSubject.createDefault(new ArrayList<>());

  CompositeDisposable disposables = new CompositeDisposable();

  CompositeDisposable visibilityDisposables = new CompositeDisposable();

  void onAddRecipeClicked() {
    getView().startAddRecipeActivity();
  }

  @Override
  protected void onAttachView(@NonNull IRecipeList view) {
    super.onAttachView(view);

    loadRecipes();
  }

  void onViewDisplayed() {
    //only add if no previous Disposables
    if (visibilityDisposables.isDisposed()) {
      visibilityDisposables.add(recipeSubject
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(this::setRecipes));

      visibilityDisposables.add(isLoadingSubject
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(this::onLoadingStateChanged)
      );
    }
  }

  void onViewHidden() {
    visibilityDisposables.clear();
  }

  @Override
  protected void onDetachView() {
    super.onDetachView();

    disposables.clear();
  }

  private void onLoadingStateChanged(boolean isLoading) {

  }

  private void setRecipes(final List<Recipe> recipes) {
    if (getView() != null)
      getView().setRecipes(recipes);
    else
      sendToView(recipeView -> recipeView.setRecipes(recipes));
  }

  void loadRecipes() {
    // Don't try and load if we're already loading
    if (isLoadingSubject.getValue()) {
      return;
    }

    isLoadingSubject.onNext(true);

    disposables.add(
            App.appComponent.recipeProvider().list()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    // Concatenate the new recipes to the current recipes list, then emit it via the recipe subject
                    .doOnNext(list -> {
                      List<Recipe> fullList = new ArrayList<>(recipeSubject.getValue());
                      fullList.addAll(list);
                      recipeSubject.onNext(fullList);
                    })
                    .doOnError(throwable -> Log.d(TAG, "Error"))
                    .doOnTerminate(() -> isLoadingSubject.onNext(false))
                    .subscribe()
    );
  }

}
