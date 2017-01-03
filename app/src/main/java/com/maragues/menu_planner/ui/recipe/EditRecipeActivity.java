package com.maragues.menu_planner.ui.recipe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.MenuItem;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.ui.BaseLoggedInActivity;

import butterknife.BindView;

/**
 * Created by miguelaragues on 30/12/16.
 */

public class EditRecipeActivity
        extends BaseLoggedInActivity<EditRecipePresenter, IEditRecipeView>
        implements IEditRecipeView {
  @BindView(R.id.recipe_title)
  TextInputEditText titleEditText;

  @BindView(R.id.recipe_description)
  TextInputEditText descriptionEditText;

  @NonNull
  @Override
  public EditRecipePresenter providePresenter() {
    return new EditRecipePresenter();
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
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
          getPresenter().onHomeClicked();

          return true;
      }
    }

    return parentResult;
  }

  @Override
  public void onBackPressed() {
    getPresenter().onBackPressed();

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
