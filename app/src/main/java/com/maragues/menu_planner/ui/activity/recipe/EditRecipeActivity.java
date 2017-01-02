package com.maragues.menu_planner.ui.activity.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.MenuItem;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.maragues.menu_planner.R;
import com.maragues.menu_planner.presentation.presenter.recipe.EditRecipePresenter;
import com.maragues.menu_planner.presentation.view.recipe.EditRecipeView;
import com.maragues.menu_planner.ui.BaseLoggedInActivity;

import butterknife.BindView;

public class EditRecipeActivity extends BaseLoggedInActivity implements EditRecipeView {
  public static final String TAG = "EditRecipeActivity";

  @InjectPresenter
  EditRecipePresenter editRecipePresenter;

  @BindView(R.id.recipe_title)
  TextInputEditText titleEditText;

  @BindView(R.id.recipe_description)
  TextInputEditText descriptionEditText;

  public static Intent getIntent(final Context context) {
    Intent intent = new Intent(context, EditRecipeActivity.class);

    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_create_recipe);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    boolean parentResult = super.onOptionsItemSelected(item);

    if (!parentResult) {
      switch (item.getItemId()) {
        case android.R.id.home:
          editRecipePresenter.onHomeClicked();

          return true;
      }
    }

    return parentResult;
  }

  @Override
  public void onBackPressed() {
    editRecipePresenter.onBackPressed();

    super.onBackPressed();
  }

  @Override
  public String getRecipeTitle() {
    return titleEditText.getText().toString();
  }

  @Override
  public String getRecipeDescription() {
    return descriptionEditText.getText().toString();
  }
}
