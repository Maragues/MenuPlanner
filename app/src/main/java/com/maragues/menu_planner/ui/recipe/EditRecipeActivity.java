package com.maragues.menu_planner.ui.recipe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.ui.BaseLoggedInActivity;

import butterknife.BindView;

/**
 * Created by miguelaragues on 30/12/16.
 */

public class EditRecipeActivity extends BaseLoggedInActivity {
  @BindView(R.id.recipe_title)
  TextInputEditText titleEditText;

  @BindView(R.id.recipe_description)
  TextInputEditText descriptionEditText;

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
          attemptSave();

          finish();

          return true;
      }
    }

    return parentResult;
  }

  @Override
  public void onBackPressed() {
    attemptSave();

    super.onBackPressed();
  }

  private void attemptSave() {
    if (validates()) {
      Recipe recipe = Recipe.builder()
              .setName(titleEditText.getText().toString())
              .setDescription(descriptionEditText.getText().toString())
              .setUid(FirebaseAuth.getInstance().getCurrentUser().getUid())
              .build();

      App.appComponent.recipeProvider().create(recipe);
    }
  }

  private boolean validates() {
    return !TextUtils.isEmpty(titleEditText.getText());
  }
}
