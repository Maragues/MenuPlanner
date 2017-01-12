package com.maragues.menu_planner.ui.recipe.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.ui.BaseLoggedInActivity;
import com.maragues.menu_planner.ui.recipe.editor.EditRecipeActivity;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;

public class RecipeListActivity extends BaseLoggedInActivity<RecipeListPresenter, IRecipeList>
        implements IRecipeList {

  @BindView(R.id.recipe_list_recyclerview)
  RecyclerView recyclerView;

  /** Hold active loading observable subscriptions, so that they can be unsubscribed from when the activity is destroyed */
  private CompositeDisposable disposables;

  @NonNull
  @Override
  public RecipeListPresenter providePresenter() {
    return new RecipeListPresenter();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_list);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    setupRecyclerView();
  }

  private void setupRecyclerView() {
    LinearLayoutManager manager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(manager);
  }

  @OnClick(R.id.recipe_list_add_fab)
  void onAddRecipeClicked() {
    getPresenter().onAddRecipeClicked();
  }

  @Override
  public void startAddRecipeActivity() {
    startActivity(EditRecipeActivity.createAddIntent(this));
  }
}
