package com.maragues.menu_planner.ui.recipe.list;

import android.support.annotation.NonNull;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.model.providers.firebase.RecipeProviderFirebase;
import com.maragues.menu_planner.ui.BaseLoggedInPresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
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

    /*disposables.add(
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
    );*/

    FirebaseRecyclerAdapter<Recipe, RecipeListAdapter.ViewHolder> adapter =
            new FirebaseRecyclerAdapter<Recipe, RecipeListAdapter.ViewHolder>(
                    Recipe.class,
                    R.layout.item_recipe_list,
                    RecipeListAdapter.ViewHolder.class,
                    FirebaseDatabase.getInstance().getReference()
                            .child(RecipeProviderFirebase.USER_RECIPES_KEY)
                            .child(App.appComponent.userProvider().getUid())
            ) {
              @Override
              protected void populateViewHolder(RecipeListAdapter.ViewHolder viewHolder, Recipe recipe, int position) {
                viewHolder.setRecipe(recipe);

                viewHolder.itemView.setOnClickListener(v -> onRecipeClicked(recipe));
              }

              @Override
              protected Recipe parseSnapshot(DataSnapshot snapshot) {
                return Recipe.create(snapshot);
              }
            };

    if (getView() != null)
      getView().setAdapter(adapter);
    else
      sendToView(view -> view.setAdapter(adapter));
  }

  private void onRecipeClicked(@NonNull Recipe recipe) {
    getView().startRecipeViewer(recipe);
  }

}
