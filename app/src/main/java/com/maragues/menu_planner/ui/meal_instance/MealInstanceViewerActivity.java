package com.maragues.menu_planner.ui.meal_instance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.ui.common.BaseLoggedInActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MealInstanceViewerActivity
        extends BaseLoggedInActivity<MealInstanceViewerPresenter, IMealInstanceViewer>
        implements IMealInstanceViewer {
  public static final String EXTRA_MEAL_INSTANCE = "extra_meal_instance";

  @BindView(R.id.meal_instance_viewer_textview)
  TextView textView;

  public static Intent createIntent(@NonNull Context context, @NonNull MealInstance mealInstance) {
    Intent intent = new Intent(context, MealInstanceViewerActivity.class);

    intent.putExtra(EXTRA_MEAL_INSTANCE, mealInstance);

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
    return new MealInstanceViewerPresenter(getIntent().getParcelableExtra(EXTRA_MEAL_INSTANCE));
  }

  @Override
  public void showRecipes(String recipes) {
    textView.setText(recipes);
  }
}
