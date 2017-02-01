package com.maragues.menu_planner.ui.meal_instance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.R;
import com.maragues.menu_planner.ui.common.BaseLoggedInActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MealInstanceViewerActivity
        extends BaseLoggedInActivity<MealInstanceViewerPresenter, IMealInstanceViewer>
        implements IMealInstanceViewer {
  public static final String EXTRA_MEAL_INSTANCE_ID = "extra_meal_instance_id";

  @BindView(R.id.meal_instance_viewer_textview)
  TextView textView;

  public static Intent createIntent(@NonNull Context context, @NonNull String mealInstanceId) {
    if (App.appComponent.textUtils().isEmpty(mealInstanceId))
      throw new IllegalArgumentException("mealInstanceId can't be empty");

    Intent intent = new Intent(context, MealInstanceViewerActivity.class);

    intent.putExtra(EXTRA_MEAL_INSTANCE_ID, mealInstanceId);

    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_meal_instance_viewer);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  @OnClick(R.id.meal_instance_viewer_fab)
  void onFabClicked() {

  }

  @NonNull
  @Override
  public MealInstanceViewerPresenter providePresenter() {
    return new MealInstanceViewerPresenter(getIntent().getStringExtra(EXTRA_MEAL_INSTANCE_ID));
  }

  @Override
  public void showRecipes(String recipes) {
    textView.setText(recipes);
  }

  @Override
  public void showErrorFetchingMealInstance() {
    Toast.makeText(this, "Error loading meal", Toast.LENGTH_SHORT).show();
  }
}
