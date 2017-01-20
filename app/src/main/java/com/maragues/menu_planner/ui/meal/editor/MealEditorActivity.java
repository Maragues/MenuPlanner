package com.maragues.menu_planner.ui.meal.editor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.ui.common.BaseLoggedInActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MealEditorActivity extends BaseLoggedInActivity<MealEditorPresenter, IMealEditor>
        implements IMealEditor {

  private static final String EXTRA_MEAL_ID = "extra_meal_id";
  private static final int ADD_RECIPE_CODE = 6;

  @BindView(R.id.meal_editor_empty)
  View emptyLayout;

  public static Intent createIntent(Context context) {
    return createIntent(context, null);
  }

  public static Intent createIntent(Context context, @Nullable MealInstance mealInstance) {
    Intent intent = new Intent(context, MealEditorActivity.class);

    return intent;
  }

  @Nullable
  public static String extractMealId(Intent data) {
    if (data == null || !data.hasExtra(EXTRA_MEAL_ID))
      return null;

    return data.getStringExtra(EXTRA_MEAL_ID);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_meal_editor);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if(requestCode == ADD_RECIPE_CODE && resultCode == RESULT_OK){
      getPresenter().onRecipeAdded(AddRecipeToMealActivity.extractRecipe(data));
    }
  }

  @OnClick({R.id.meal_editor_add, R.id.meal_editor_empty})
  void onAddRecipeClicked() {
    getPresenter().onAddRecipeClicked();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();

    inflater.inflate(R.menu.menu_meal_editor, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_save:
        getPresenter().onSaveClicked();

        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @NonNull
  @Override
  public MealEditorPresenter providePresenter() {
    return new MealEditorPresenter();
  }

  @Override
  public void navigateToAddRecipe() {
    startActivityForResult(AddRecipeToMealActivity.createIntent(this), ADD_RECIPE_CODE);
  }

  @Nullable
  @Override
  public String getMealId() {
    return null;
  }

  @Override
  public void hideEmptyLayout() {
    emptyLayout.setVisibility(View.GONE);
  }

  @Override
  public void showEmptyLayout() {
    emptyLayout.setVisibility(View.VISIBLE);
  }
}
