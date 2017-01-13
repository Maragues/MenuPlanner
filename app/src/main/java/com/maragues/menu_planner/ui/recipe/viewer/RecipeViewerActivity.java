package com.maragues.menu_planner.ui.recipe.viewer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.ui.common.BaseLoggedInActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class RecipeViewerActivity extends BaseLoggedInActivity<ReciperViewerPresenter, IRecipeViewer>
        implements IRecipeViewer {

  private static final String EXTRA_RECIPE_ID = "extra_recipe_id";

  @BindView(R.id.reciper_viewer_name)
  TextView nameTextView;

  @NonNull
  @Override
  public ReciperViewerPresenter providePresenter() {
    return new ReciperViewerPresenter();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_viewer);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  @OnClick(R.id.recipe_viewer_fab)
  void onAddToPlannerClicked(){
    Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
  }

  @NonNull
  public static Intent createIntent(Context context, Recipe recipe) {
    Intent intent = new Intent(context, RecipeViewerActivity.class);

    intent.putExtra(EXTRA_RECIPE_ID, recipe.id());

    return intent;
  }
}
