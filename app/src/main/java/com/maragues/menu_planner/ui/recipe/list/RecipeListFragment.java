package com.maragues.menu_planner.ui.recipe.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.ui.common.BaseTiFragment;
import com.maragues.menu_planner.ui.recipe.editor.EditRecipeActivity;
import com.maragues.menu_planner.ui.recipe.viewer.RecipeViewerActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by miguelaragues on 13/1/17.
 */

public class RecipeListFragment extends BaseTiFragment<RecipeListPresenter, IRecipeList>
        implements IRecipeList {

  @BindView(R.id.recipe_list_recyclerview)
  RecyclerView recyclerView;

  public static Fragment newInstance() {
    return new RecipeListFragment();
  }

  @NonNull
  @Override
  public RecipeListPresenter providePresenter() {
    return new RecipeListPresenter();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflateView(inflater, container, R.layout.fragment_recipe_list);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    setupRecyclerView();
  }

  private void setupRecyclerView() {
    LinearLayoutManager manager = new LinearLayoutManager(getContext());
    recyclerView.setLayoutManager(manager);
  }

  @OnClick(R.id.recipe_list_add_fab)
  void onAddRecipeClicked() {
    getPresenter().onAddRecipeClicked();
  }

  @Override
  public void startAddRecipeActivity() {
    startActivity(EditRecipeActivity.createAddIntent(getContext()));
  }

  @Override
  public void startRecipeViewer(@NonNull Recipe recipe) {
    startActivity(RecipeViewerActivity.createIntent(getContext(), recipe));
  }

  @Override
  public void showIsLoading(boolean isLoading) {

  }

  @Override
  public void setAdapter(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
    recyclerView.setAdapter(adapter);
  }

  @Override
  public void onResume() {
    super.onResume();

    getPresenter().onViewDisplayed();
  }

  @Override
  public void onPause() {
    super.onPause();

    getPresenter().onViewHidden();
  }
}
