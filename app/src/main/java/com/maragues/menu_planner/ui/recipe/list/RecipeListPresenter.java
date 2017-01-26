package com.maragues.menu_planner.ui.recipe.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.model.providers.firebase.RecipeProviderFirebase;
import com.maragues.menu_planner.ui.common.BaseLoggedInPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by miguelaragues on 5/1/17.
 */
class RecipeListPresenter extends BaseLoggedInPresenter<IRecipeList> {
  private static final String TAG = RecipeListPresenter.class.getSimpleName();

  private BehaviorSubject<Boolean> isLoadingSubject = BehaviorSubject.createDefault(false);

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
    if (visibilityDisposables.size() == 0) {
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

  void onLoadingStateChanged(boolean isLoading) {
    if (getView() != null)
      getView().showIsLoading(isLoading);
    else
      sendToView(view -> view.showIsLoading(isLoading));
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

    if (getView() != null)
      getView().setAdapter(createAdapter());
    else
      sendToView(view -> view.setAdapter(createAdapter()));
  }

  RecyclerView.Adapter<ViewHolder> adapter;

  /*
  TODO it would be better if Presenter implemented an interface and only the
  implementation used a Firebase dependency
   */
  @NonNull
  RecyclerView.Adapter<ViewHolder> createAdapter() {
    if (adapter == null) {
      //TODO should this be static or extracted? It doesn't hold a reference to Context or View. Adapter handles view, it shouldn't be here
      adapter = new FirebaseRecyclerAdapter<Recipe, ViewHolder>(
              Recipe.class,
              R.layout.item_recipe_list,
              ViewHolder.class,
              recipesQuery()
      ) {
        @Override
        protected void populateViewHolder(ViewHolder viewHolder, Recipe recipe, int position) {
          viewHolder.setRecipe(recipe);

          viewHolder.itemView.setOnClickListener(v -> onRecipeClicked(recipe));

          if (position % 2 == 0) {
            viewHolder.itemView.setBackgroundResource(R.drawable.sel_grid_even);
          } else {
            viewHolder.itemView.setBackgroundResource(R.drawable.sel_grid_odd);
          }
        }

        @Override
        protected Recipe parseSnapshot(DataSnapshot snapshot) {
          return Recipe.create(snapshot);
        }

        @Override
        protected void onCancelled(DatabaseError error) {
          super.onCancelled(error);

          isLoadingSubject.onNext(false);
        }

        @Override
        protected void onDataChanged() {
          super.onDataChanged();

          isLoadingSubject.onNext(false);
        }
      };
    }

    return adapter;
  }

  DatabaseReference recipesQuery() {
    return FirebaseDatabase.getInstance().getReference()
            .child(RecipeProviderFirebase.GROUP_RECIPES_KEY)
            .child(App.appComponent.userProvider().getGroupId());
  }

  private void onRecipeClicked(@NonNull Recipe recipe) {
    if (getView() != null)
      getView().startRecipeViewer(recipe);
  }

  @SuppressWarnings("WeakerAccess")
  public static class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_recipe_name)
    TextView titleTextView;

    public ViewHolder(View itemView) {
      super(itemView);

      ButterKnife.bind(this, itemView);
    }

    public void setRecipe(@NonNull Recipe recipe) {
      titleTextView.setText(recipe.name());
    }
  }

}
